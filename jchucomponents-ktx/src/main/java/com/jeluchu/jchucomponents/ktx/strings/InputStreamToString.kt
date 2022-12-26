/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.strings

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

class InputStreamToString {

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