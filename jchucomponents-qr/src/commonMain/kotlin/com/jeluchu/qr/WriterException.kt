/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.qr

class WriterException : Exception {
    constructor()
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
}