/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils.network

import com.jeluchu.jchucomponents.utils.network.NetworkUtils.saveResponseBodyToFile
import okhttp3.ResponseBody
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

object NetworkUtils {

    fun saveResponseBodyToFile(
        filePath: String,
        responseBody: ResponseBody,
        progress: (percent: Long) -> Unit
    ) {
        responseBody.let { body ->
            File(filePath).let { file ->
                val outputStream = FileOutputStream(file)
                body.source().also {
                    val buffer = ByteArray(4096)
                    var totalBytesRead = 0L
                    while (true) {
                        val byteRead = it.read(buffer)
                        if (byteRead < 0) {
                            break
                        }
                        outputStream.write(buffer, 0, byteRead)
                        // Downloading progress in percent
                        totalBytesRead += byteRead
                        val percent = (totalBytesRead * 100) / body.contentLength()
                        progress(percent)
                    }
                }
                outputStream.flush()
                outputStream.close()
            }
        }
    }

}