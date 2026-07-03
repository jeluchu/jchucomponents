/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.qr

abstract class ReaderException : Exception {
    internal constructor()
    internal constructor(cause: Throwable?) : super(cause)

    companion object {
        val isStackTrace = false
    }
}
