/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils.network.interceptors

import android.content.Context
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer

class DebugInterceptor(val context: Context) : Interceptor {

    private val debugFile: FileOutputStream by lazy {
        context.openFileOutput("netdebug", Context.MODE_PRIVATE or Context.MODE_APPEND)
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val requestBodyBuffer = Buffer()
        val requestBody = requestBodyBuffer.readUtf8()
        var response = chain.proceed(request)
        val responseEncoding = response.header("Content-Encoding")
        val isCompressResponse = responseEncoding != null && responseEncoding == "gzip"

        val method = request.method
        val url = request.url
        val headers = response.headers
        val t1 = System.nanoTime()
        val responseTime = (System.nanoTime() - t1) / 1e6
        val responseCode = response.code

        request.newBuilder().build().body?.writeTo(requestBodyBuffer)

        if (response.header("Content-Type")?.startsWith("application/json") == true) {
            val responseBody =
                if (isCompressResponse) response.body?.bytes() else response.body?.string() ?: ""
            val responseContent =
                if (isCompressResponse) decompress(responseBody as ByteArray) else responseBody as String
            debugFile.bufferedWriter().append(
                String.format(
                    "%s %s^%s^%s^%.3fms^%d^%s`",
                    method,
                    url,
                    headers,
                    requestBody,
                    responseTime,
                    responseCode,
                    responseContent
                )
            ).flush()

            response = response.newBuilder()
                .body(
                    ResponseBody.create(
                        response.body?.contentType(),
                        if (!isCompressResponse) responseContent.toByteArray() else compress(
                            responseContent
                        )
                    )
                )
                .build()

        } else {
            debugFile.bufferedWriter().append(
                String.format(
                    "%s %s^%s^%s^%.3fms^%d^%s`",
                    method,
                    url,
                    headers,
                    requestBody,
                    responseTime,
                    responseCode,
                    null
                )
            ).flush()
        }

        return response

    }

    @Throws(IOException::class)
    fun decompress(compressed: ByteArray): String {
        GZIPInputStream(ByteArrayInputStream(compressed)).use { gzipIn ->
            return convertInputStreamToString(gzipIn)
        }
    }

    @Throws(IOException::class)
    fun compress(string: String): ByteArray {
        val os = ByteArrayOutputStream(string.length)
        val gos = GZIPOutputStream(os)
        gos.write(string.toByteArray())
        gos.close()
        val compressed = os.toByteArray()
        os.close()
        return compressed
    }

    @Throws(IOException::class)
    fun convertInputStreamToString(`is`: InputStream): String {
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(8192)
        var length: Int
        while (`is`.read(buffer).also { length = it } != -1) {
            result.write(buffer, 0, length)
        }
        return result.toString(StandardCharsets.UTF_8.name())
    }

}