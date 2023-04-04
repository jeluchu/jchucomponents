/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.qr.qrcode

import com.jeluchu.qr.ChecksumException
import com.jeluchu.qr.FormatException
import com.jeluchu.qr.common.BitMatrix
import com.jeluchu.qr.common.DecoderResult
import com.jeluchu.qr.qrcode.decoder.Decoder
import com.jeluchu.qr.qrcode.decoder.QRCodeDecoderMetaData
import com.jeluchu.qr.qrcode.detector.Detector
import com.jeluchu.qr.BarcodeFormat
import com.jeluchu.qr.BinaryBitmap
import com.jeluchu.qr.DecodeHintType
import com.jeluchu.qr.NotFoundException
import com.jeluchu.qr.Reader
import com.jeluchu.qr.Result
import com.jeluchu.qr.ResultMetadataType
import com.jeluchu.qr.ResultPoint
import kotlin.math.roundToInt

class QRCodeReader : Reader {
    private val decoder = Decoder()

    /**
     * Locates and decodes a QR code in an image.
     *
     * @return a String representing the content encoded by the QR code
     * @throws NotFoundException if a QR code cannot be found
     * @throws FormatException   if a QR code cannot be decoded
     * @throws ChecksumException if error correction fails
     */
    @Throws(NotFoundException::class, ChecksumException::class, FormatException::class)
    override fun decode(image: BinaryBitmap?): Result = decode(image, null)

    @Throws(NotFoundException::class, ChecksumException::class, FormatException::class)
    override fun decode(image: BinaryBitmap?, hints: Map<DecodeHintType?, *>?): Result {
        val decoderResult: DecoderResult
        val points: Array<ResultPoint?>
        if (hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
            val bits = image!!.blackMatrix?.let { extractPureBits(it) }
            decoderResult = decoder.decode(bits, hints)
            points = emptyArray()
        } else {
            val detectorResult = image!!.blackMatrix?.let { Detector(it).detect(hints) }
            decoderResult = decoder.decode(detectorResult?.bits, hints)
            points = detectorResult?.points!!
        }

        // If the code was mirrored: swap the bottom-left and the top-right points.
        if (decoderResult.other is QRCodeDecoderMetaData) {
            (decoderResult.other as QRCodeDecoderMetaData?)!!.applyMirroredCorrection(points)
        }
        val result =
            Result(decoderResult.text, decoderResult.rawBytes, points, BarcodeFormat.QR_CODE)
        val byteSegments = decoderResult.byteSegments
        result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments)
        val ecLevel = decoderResult.eCLevel
        result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel)
        if (decoderResult.hasStructuredAppend()) {
            result.putMetadata(
                ResultMetadataType.STRUCTURED_APPEND_SEQUENCE,
                decoderResult.structuredAppendSequenceNumber
            )
            result.putMetadata(
                ResultMetadataType.STRUCTURED_APPEND_PARITY,
                decoderResult.structuredAppendParity
            )
        }
        return result
    }

    override fun reset() {}

    companion object {

        /**
         * This method detects a code in a "pure" image -- that is, pure monochrome image
         * which contains only an unrotated, unskewed, image of a code, with some white border
         * around it. This is a specialized method that works exceptionally fast in this special
         * case.
         */
        @Throws(NotFoundException::class)
        private fun extractPureBits(image: BitMatrix): BitMatrix {
            val leftTopBlack = image.topLeftOnBit
            val rightBottomBlack = image.bottomRightOnBit
            if (leftTopBlack == null || rightBottomBlack == null) {
                throw NotFoundException.notFoundInstance
            }
            val moduleSize = moduleSize(leftTopBlack, image)
            var top = leftTopBlack[1]
            val bottom = rightBottomBlack[1]
            var left = leftTopBlack[0]
            var right = rightBottomBlack[0]

            // Sanity check!
            if (left >= right || top >= bottom) {
                throw NotFoundException.notFoundInstance
            }
            if (bottom - top != right - left) {
                // Special case, where bottom-right module wasn't black so we found something else in the last row
                // Assume it's a square, so use height as the width
                right = left + (bottom - top)
                if (right >= image.width) {
                    // Abort if that would not make sense -- off image
                    throw NotFoundException.notFoundInstance
                }
            }
            val matrixWidth = ((right - left + 1) / moduleSize).roundToInt()
            val matrixHeight = ((bottom - top + 1) / moduleSize).roundToInt()
            if (matrixWidth <= 0 || matrixHeight <= 0) throw NotFoundException.notFoundInstance
            if (matrixHeight != matrixWidth) throw NotFoundException.notFoundInstance

            // Push in the "border" by half the module width so that we start
            // sampling in the middle of the module. Just in case the image is a
            // little off, this will help recover.
            val nudge = (moduleSize / 2.0f).toInt()
            top += nudge
            left += nudge

            // But careful that this does not sample off the edge
            // "right" is the farthest-right valid pixel location -- right+1 is not necessarily
            // This is positive by how much the inner x loop below would be too large
            val nudgedTooFarRight = left + ((matrixWidth - 1) * moduleSize).toInt() - right
            if (nudgedTooFarRight > 0) {
                if (nudgedTooFarRight > nudge) throw NotFoundException.notFoundInstance
                left -= nudgedTooFarRight
            }
            // See logic above
            val nudgedTooFarDown = top + ((matrixHeight - 1) * moduleSize).toInt() - bottom
            if (nudgedTooFarDown > 0) {
                if (nudgedTooFarDown > nudge) throw NotFoundException.notFoundInstance
                top -= nudgedTooFarDown
            }

            // Now just read off the bits
            val bits = BitMatrix(matrixWidth, matrixHeight, 1)
            for (y in 0 until matrixHeight) {
                val iOffset = top + (y * moduleSize).toInt()
                for (x in 0 until matrixWidth) {
                    if (image[left + (x * moduleSize).toInt(), iOffset]) bits[x] = y
                }
            }
            return bits
        }

        @Throws(NotFoundException::class)
        private fun moduleSize(leftTopBlack: IntArray, image: BitMatrix): Float {
            val height = image.height
            val width = image.width
            var x = leftTopBlack[0]
            var y = leftTopBlack[1]
            var inBlack = true
            var transitions = 0
            while (x < width && y < height) {
                if (inBlack != image[x, y]) {
                    if (++transitions == 5) break
                    inBlack = !inBlack
                }
                x++
                y++
            }
            if (x == width || y == height) throw NotFoundException.notFoundInstance
            return (x - leftTopBlack[0]) / 7.0f
        }
    }
}