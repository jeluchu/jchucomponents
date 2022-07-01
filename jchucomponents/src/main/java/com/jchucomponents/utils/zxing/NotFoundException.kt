/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.utils.zxing


object NotFoundException : ReaderException() {

    val notFoundInstance = NotFoundException

    init {
        notFoundInstance.stackTrace = NO_TRACE
    }

}