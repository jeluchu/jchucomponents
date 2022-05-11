/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.utils.zxing

class WriterException : Exception {
    constructor()
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
}