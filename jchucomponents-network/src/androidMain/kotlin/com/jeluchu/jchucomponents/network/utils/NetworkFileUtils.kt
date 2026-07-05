package com.jeluchu.jchucomponents.network.utils

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 *
 * Author: @Jeluchu
 *
 * Save response body to specific file with progressing callback
 * [saveResponseBodyToFile] @param filePath Path of file that store the response body
 * [saveResponseBodyToFile] @param responseBody Response body from network call
 * [saveResponseBodyToFile] @param progress Progressing percent callback
 *
 */

suspend fun saveResponseBodyToFile(
    filePath: String,
    response: HttpResponse,
    progress: (percent: Long) -> Unit
) {
    val contentLength = response.headers[HttpHeaders.ContentLength]?.toLongOrNull()
    val channel = response.bodyAsChannel()
    withContext(Dispatchers.IO) {
        FileOutputStream(File(filePath)).use { outputStream ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var totalBytesRead = 0L
            while (true) {
                val bytesRead = channel.readAvailable(buffer)
                if (bytesRead < 0) break
                if (bytesRead == 0) continue
                outputStream.write(buffer, 0, bytesRead)
                totalBytesRead += bytesRead
                if (contentLength != null && contentLength > 0) {
                    progress((totalBytesRead * 100 / contentLength).coerceAtMost(maximumValue = 100))
                }
            }
            outputStream.flush()
            if (contentLength == null || contentLength <= 0) progress(100)
        }
    }
}
