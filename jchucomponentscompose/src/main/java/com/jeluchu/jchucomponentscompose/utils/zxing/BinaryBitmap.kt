package com.jeluchu.jchucomponentscompose.utils.zxing

import com.jeluchu.jchucomponentscompose.utils.zxing.common.BitMatrix

class BinaryBitmap(binarizer: Binarizer?) {
    private val binarizer: Binarizer
    private var matrix: BitMatrix? = null

    /**
     * @return The width of the bitmap.
     */
    val width: Int
        get() = binarizer.width

    /**
     * @return The height of the bitmap.
     */
    val height: Int
        get() = binarizer.height

    /**
     * Converts a 2D array of luminance data to 1 bit. As above, assume this method is expensive
     * and do not call it repeatedly. This method is intended for decoding 2D barcodes and may or
     * may not apply sharpening. Therefore, a row from this matrix may not be identical to one
     * fetched using getBlackRow(), so don't mix and match between them.
     *
     * @return The 2D array of bits for the image (true means black).
     * @throws NotFoundException if image can't be binarized to make a matrix
     */
    @get:Throws(NotFoundException::class)
    val blackMatrix: BitMatrix?
        get() {
            // The matrix is created on demand the first time it is requested, then cached. There are two
            // reasons for this:
            // 1. This work will never be done if the caller only installs 1D Reader objects, or if a
            //    1D Reader finds a barcode before the 2D Readers run.
            // 2. This work will only be done once even if the caller installs multiple 2D Readers.
            if (matrix == null) {
                matrix = binarizer.blackMatrix
            }
            return matrix
        }

    /**
     * @return Whether this bitmap can be cropped.
     */
    val isCropSupported: Boolean
        get() = binarizer.luminanceSource.isCropSupported

    /**
     * Returns a new object with cropped image data. Implementations may keep a reference to the
     * original data rather than a copy. Only callable if isCropSupported() is true.
     *
     * @param left   The left coordinate, which must be in [0,getWidth())
     * @param top    The top coordinate, which must be in [0,getHeight())
     * @param width  The width of the rectangle to crop.
     * @param height The height of the rectangle to crop.
     * @return A cropped version of this object.
     */
    fun crop(left: Int, top: Int, width: Int, height: Int): BinaryBitmap {
        val newSource = binarizer.luminanceSource.crop(left, top, width, height)
        return BinaryBitmap(binarizer.createBinarizer(newSource))
    }

    /**
     * @return Whether this bitmap supports counter-clockwise rotation.
     */
    val isRotateSupported: Boolean
        get() = binarizer.luminanceSource.isRotateSupported

    /**
     * Returns a new object with rotated image data by 90 degrees counterclockwise.
     * Only callable if [.isRotateSupported] is true.
     *
     * @return A rotated version of this object.
     */
    fun rotateCounterClockwise(): BinaryBitmap {
        val newSource = binarizer.luminanceSource.rotateCounterClockwise()
        return BinaryBitmap(binarizer.createBinarizer(newSource))
    }

    override fun toString(): String {
        return try {
            blackMatrix.toString()
        } catch (e: NotFoundException) {
            ""
        }
    }

    init {
        requireNotNull(binarizer) { "Binarizer must be non-null." }
        this.binarizer = binarizer
    }
}