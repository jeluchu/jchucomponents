/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.qr

import com.jeluchu.qr.common.BitMatrix

abstract class Binarizer protected constructor(private val luminanceSource: LuminanceSource) {

    /**
     * Converts a 2D array of luminance data to 1 bit data. As above, assume this method is expensive
     * and do not call it repeatedly. This method is intended for decoding 2D barcodes and may or
     * may not apply sharpening. Therefore, a row from this matrix may not be identical to one
     * fetched using getBlackRow(), so don't mix and match between them.
     *
     * @return The 2D array of bits for the image (true means black).
     * @throws NotFoundException if image can't be binarized to make a matrix
     */
    @get:Throws(NotFoundException::class)
    abstract val blackMatrix: BitMatrix?

    /**
     * Creates a new object with the same type as this Binarizer implementation, but with pristine
     * state. This is needed because Binarizer implementations may be stateful, e.g. keeping a cache
     * of 1 bit data. See Effective Java for why we can't use Java's clone() method.
     *
     * @return A new concrete Binarizer implementation object.
     */
    val width: Int
        get() = luminanceSource.width
    val height: Int
        get() = luminanceSource.height
}