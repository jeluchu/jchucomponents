/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils.zxing

class ChecksumException private constructor() : ReaderException() {
    companion object {
        private val INSTANCE = ChecksumException()
        val checksumInstance: ChecksumException
            get() = if (isStackTrace) ChecksumException() else INSTANCE

        init {
            INSTANCE.stackTrace = NO_TRACE // since it's meaningless
        }
    }
}