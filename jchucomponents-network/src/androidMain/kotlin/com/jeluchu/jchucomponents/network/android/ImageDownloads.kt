package com.jeluchu.jchucomponents.network.android

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

public fun String.saveImage(destinationFile: File) {
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

public fun String.getBitmapFromUrl(): Bitmap? = runCatching {
    val connection = URL(this).openConnection() as HttpURLConnection
    try {
        connection.doInput = true
        connection.connect()
        connection.inputStream.use(BitmapFactory::decodeStream)
    } finally {
        connection.disconnect()
    }
}.getOrNull()
