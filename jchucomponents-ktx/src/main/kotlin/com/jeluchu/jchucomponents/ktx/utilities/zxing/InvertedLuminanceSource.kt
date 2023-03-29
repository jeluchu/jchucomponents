/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.utilities.zxing

import okhttp3.internal.and

class InvertedLuminanceSource(private val delegate: LuminanceSource?) : LuminanceSource(
    delegate!!.width, delegate.height
) {
    override fun getRow(y: Int, row: ByteArray?): ByteArray {
        var rowBytes = row
        rowBytes = delegate!!.getRow(y, rowBytes)
        val width = width
        for (i in 0 until width) {
            rowBytes[i] = (255 - (rowBytes[i] and 0xFF)).toByte()
        }
        return rowBytes
    }

    override val matrix: ByteArray
        get() {
            val matrix = delegate!!.matrix
            val length = width * height
            val invertedMatrix = ByteArray(length)
            for (i in 0 until length) {
                invertedMatrix[i] = (255 - (matrix!![i] and 0xFF)).toByte()
            }
            return invertedMatrix
        }
    override val isCropSupported: Boolean
        get() = delegate!!.isCropSupported

    override fun crop(left: Int, top: Int, width: Int, height: Int): LuminanceSource =
        InvertedLuminanceSource(delegate!!.crop(left, top, width, height))

    override val isRotateSupported: Boolean
        get() = delegate!!.isRotateSupported

    /**
     * @return original delegate [LuminanceSource] since invert undoes itself
     */
    override fun invert(): LuminanceSource? = delegate

    override fun rotateCounterClockwise(): LuminanceSource =
        InvertedLuminanceSource(delegate!!.rotateCounterClockwise())

    override fun rotateCounterClockwise45(): LuminanceSource =
        InvertedLuminanceSource(delegate!!.rotateCounterClockwise45())

}