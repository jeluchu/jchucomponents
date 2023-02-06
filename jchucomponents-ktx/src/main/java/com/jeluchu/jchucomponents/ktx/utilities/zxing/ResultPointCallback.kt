/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.utilities.zxing

/**
 * Callback which is invoked when a possible result point (significant
 * point in the barcode image such as a corner) is found.
 *
 * @see DecodeHintType.NEED_RESULT_POINT_CALLBACK
 */
interface ResultPointCallback {
    fun foundPossibleResultPoint(point: ResultPoint?)
}