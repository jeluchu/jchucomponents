/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.file

import com.jeluchu.jchucomponents.ktx.coroutines.noCrash
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