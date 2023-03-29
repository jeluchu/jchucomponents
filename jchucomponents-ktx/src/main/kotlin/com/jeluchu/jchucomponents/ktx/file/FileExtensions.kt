/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.file

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.jeluchu.jchucomponents.ktx.constants.QUALITY_MAX
import com.jeluchu.jchucomponents.ktx.constants.QUALITY_REDUCTION
import com.jeluchu.jchucomponents.ktx.constants.SIZE_2MB_BYTES
import com.jeluchu.jchucomponents.ktx.coroutines.noCrash
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

val File.extension: String
    get() = name.substringAfterLast('.', "")

fun File.writeFile(json: String) {
    noCrash {
        val fOut = FileOutputStream(this, true)
        OutputStreamWriter(fOut).apply { append(json) }.close()
        fOut.close()
    }
}

fun File.compress(sizeBytes: Int = SIZE_2MB_BYTES): File {
    if (length() > sizeBytes) {
        var streamLength = sizeBytes
        var compressQuality = QUALITY_MAX
        val bmpStream = ByteArrayOutputStream()
        while (streamLength >= sizeBytes && compressQuality > QUALITY_REDUCTION) {
            bmpStream.use {
                it.flush()
                it.reset()
            }
            compressQuality -= 5
            val bitmap = BitmapFactory.decodeFile(absolutePath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
        }

        FileOutputStream(this).use {
            it.write(bmpStream.toByteArray())
            it.flush()
            it.close()
        }
    }
    return this
}