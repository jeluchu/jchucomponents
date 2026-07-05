package com.jeluchu.jchucomponents.network.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

fun String.saveImage(destinationFile: File) {
    runCatching {
        Thread {
            URL(this).openStream().use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }
        }.start()
    }
}

fun String.getBitmapFromUrl(): Bitmap? =
    runCatching {
        val connection = URL(this).openConnection() as HttpURLConnection
        try {
            connection.doInput = true
            connection.connect()
            connection.inputStream.use(block = BitmapFactory::decodeStream)
        } finally {
            connection.disconnect()
        }
    }.getOrNull()
