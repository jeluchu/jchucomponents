package com.jeluchu.jchucomponentscompose.core.extensions.file

import com.jeluchu.jchucomponentscompose.core.extensions.coroutines.noCrash
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