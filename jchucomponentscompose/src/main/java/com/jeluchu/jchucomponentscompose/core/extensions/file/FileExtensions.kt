package com.jeluchu.jchucomponentscompose.core.extensions.file

import java.io.File

val File.extension: String
    get() = name.substringAfterLast('.', "")
