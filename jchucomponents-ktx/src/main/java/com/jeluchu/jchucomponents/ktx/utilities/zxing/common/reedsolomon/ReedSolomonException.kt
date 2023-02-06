/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.utilities.zxing.common.reedsolomon

/**
 *
 * Thrown when an exception occurs during Reed-Solomon decoding, such as when
 * there are too many errors to correct.
 *
 */
class ReedSolomonException(message: String?) : Exception(message)