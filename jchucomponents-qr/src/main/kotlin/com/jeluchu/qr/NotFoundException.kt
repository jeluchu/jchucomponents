/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.qr


object NotFoundException : ReaderException() {

    val notFoundInstance = NotFoundException

    init {
        notFoundInstance.stackTrace = NO_TRACE
    }

}