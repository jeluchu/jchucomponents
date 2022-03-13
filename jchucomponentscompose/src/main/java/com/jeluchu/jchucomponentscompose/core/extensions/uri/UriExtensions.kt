package com.jeluchu.jchucomponentscompose.core.extensions.uri

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

fun Uri.getFileName(contentResolver: ContentResolver): String {
    var result: String? = null
    if (scheme == "content") {
        val cursor: Cursor? = contentResolver.query(this, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            cursor?.close()
        }
    }
    if (result == null) {
        result = path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) {
            result = result.substring(cut + 1)
        }
    }
    return result
}

fun Uri.stringToJson(context: Context): String {
    val inputStream: InputStream? = context.contentResolver.openInputStream(this)
    val r = BufferedReader(InputStreamReader(inputStream))
    return r.readText()
}
