/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.utils.zxing

class FormatException : ReaderException {
    companion object {
        private val INSTANCE = FormatException()
        val formatInstance: FormatException
            get() = if (isStackTrace) FormatException() else INSTANCE

        fun getFormatInstance(cause: Throwable): FormatException =
            if (isStackTrace) FormatException(cause) else INSTANCE


        init {
            INSTANCE.stackTrace = NO_TRACE
        }
    }

    private constructor()
    private constructor(cause: Throwable) : super(cause)

}