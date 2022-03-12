package com.jeluchu.jchucomponentscompose.utils.zxing.qrcode

import com.jeluchu.jchucomponentscompose.utils.zxing.*
import com.jeluchu.jchucomponentscompose.utils.zxing.common.BitMatrix
import com.jeluchu.jchucomponentscompose.utils.zxing.common.DecoderResult
import com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.decoder.Decoder
import com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.decoder.QRCodeDecoderMetaData
import com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.detector.Detector
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
    override fun decode(image: BinaryBitmap?): Result? {
        return decode(image, null)
    }

    @Throws(NotFoundException::class, ChecksumException::class, FormatException::class)
    override fun decode(image: BinaryBitmap?, hints: Map<DecodeHintType?, *>?): Result? {
        val decoderResult: DecoderResult
        val points: Array<ResultPoint?>
        if (hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
            val bits = image!!.blackMatrix?.let { extractPureBits(it) }
            decoderResult = decoder.decode(bits, hints)
            points = emptyArray()
        } else {
            val detectorResult = Detector(image!!.blackMatrix).detect(hints)
            decoderResult = decoder.decode(detectorResult.bits, hints)
            points = detectorResult.points
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

    override fun reset() {
        // do nothing
    }

    companion object {
        private val NO_POINTS = arrayOfNulls<ResultPoint>(0)

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
                throw NotFoundException.getNotFoundInstance()
            }
            val moduleSize = moduleSize(leftTopBlack, image)
            var top = leftTopBlack[1]
            val bottom = rightBottomBlack[1]
            var left = leftTopBlack[0]
            var right = rightBottomBlack[0]

            // Sanity check!
            if (left >= right || top >= bottom) {
                throw NotFoundException.getNotFoundInstance()
            }
            if (bottom - top != right - left) {
                // Special case, where bottom-right module wasn't black so we found something else in the last row
                // Assume it's a square, so use height as the width
                right = left + (bottom - top)
                if (right >= image.width) {
                    // Abort if that would not make sense -- off image
                    throw NotFoundException.getNotFoundInstance()
                }
            }
            val matrixWidth = ((right - left + 1) / moduleSize).roundToInt()
            val matrixHeight = ((bottom - top + 1) / moduleSize).roundToInt()
            if (matrixWidth <= 0 || matrixHeight <= 0) {
                throw NotFoundException.getNotFoundInstance()
            }
            if (matrixHeight != matrixWidth) {
                // Only possibly decode square regions
                throw NotFoundException.getNotFoundInstance()
            }

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
                if (nudgedTooFarRight > nudge) {
                    // Neither way fits; abort
                    throw NotFoundException.getNotFoundInstance()
                }
                left -= nudgedTooFarRight
            }
            // See logic above
            val nudgedTooFarDown = top + ((matrixHeight - 1) * moduleSize).toInt() - bottom
            if (nudgedTooFarDown > 0) {
                if (nudgedTooFarDown > nudge) {
                    // Neither way fits; abort
                    throw NotFoundException.getNotFoundInstance()
                }
                top -= nudgedTooFarDown
            }

            // Now just read off the bits
            val bits = BitMatrix(matrixWidth, matrixHeight, 1)
            for (y in 0 until matrixHeight) {
                val iOffset = top + (y * moduleSize).toInt()
                for (x in 0 until matrixWidth) {
                    if (image[left + (x * moduleSize).toInt(), iOffset]) {
                        bits[x] = y
                    }
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
                    if (++transitions == 5) {
                        break
                    }
                    inBlack = !inBlack
                }
                x++
                y++
            }
            if (x == width || y == height) {
                throw NotFoundException.getNotFoundInstance()
            }
            return (x - leftTopBlack[0]) / 7.0f
        }
    }
}