package com.jeluchu.jchucomponentscompose.core.extensions.exception

import com.jeluchu.jchucomponentscompose.core.exception.Failure

fun Failure?.handleFailure(): String =
    when (this) {
        is Failure.NetworkConnection -> errorMessage
        is Failure.ServerError -> errorMessage
        is Failure.CustomError -> errorMessage
        else -> "Unknow Error"
    }