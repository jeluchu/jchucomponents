/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.utilities.zxing

abstract class ReaderException : Exception {

    internal constructor()
    internal constructor(cause: Throwable?) : super(cause)

    // Prevent stack traces from being taken
    @Synchronized
    override fun fillInStackTrace(): Throwable? = null

    companion object {
        // disable stack traces when not running inside test units
        @JvmField
        val isStackTrace = System.getProperty("surefire.test.class.path") != null

        @JvmField
        val NO_TRACE = arrayOfNulls<StackTraceElement>(0)
    }

}