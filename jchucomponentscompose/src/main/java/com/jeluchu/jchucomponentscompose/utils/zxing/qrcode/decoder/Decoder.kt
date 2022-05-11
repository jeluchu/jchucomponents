/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.decoder

import com.jeluchu.jchucomponentscompose.utils.zxing.ChecksumException
import com.jeluchu.jchucomponentscompose.utils.zxing.ChecksumException.Companion.checksumInstance
import com.jeluchu.jchucomponentscompose.utils.zxing.DecodeHintType
import com.jeluchu.jchucomponentscompose.utils.zxing.FormatException
import com.jeluchu.jchucomponentscompose.utils.zxing.common.BitMatrix
import com.jeluchu.jchucomponentscompose.utils.zxing.common.BitMatrix.Companion.parse
import com.jeluchu.jchucomponentscompose.utils.zxing.common.DecoderResult
import com.jeluchu.jchucomponentscompose.utils.zxing.common.reedsolomon.GenericGF
import com.jeluchu.jchucomponentscompose.utils.zxing.common.reedsolomon.ReedSolomonDecoder
import com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.decoder.DataBlock.Companion.getDataBlocks
import com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.decoder.DecodedBitStreamParser.decode
import okhttp3.internal.and

/**
 *
 * The main class which implements QR Code decoding -- as opposed to locating and extracting
 * the QR Code from an image.
 */
class Decoder {

    private val rsDecoder: ReedSolomonDecoder = ReedSolomonDecoder(GenericGF.QR_CODE_FIELD_256)

    /**
     *
     * Convenience method that can decode a QR Code represented as a 2D array of booleans.
     * "true" is taken to mean a black module.
     *
     * @param image booleans representing white/black QR Code modules
     * @param hints decoding hints that should be used to influence decoding
     * @return text and bytes encoded within the QR Code
     * @throws FormatException   if the QR Code cannot be decoded
     * @throws ChecksumException if error correction fails
     */
    @JvmOverloads
    @Throws(ChecksumException::class, FormatException::class)
    fun decode(
        image: Array<BooleanArray?>?,
        hints: Map<DecodeHintType?, *>? = null
    ): DecoderResult = decode(parse(image), hints)

    /**
     *
     * Decodes a QR Code represented as a [BitMatrix]. A 1 or "true" is taken to mean a black module.
     *
     * @param bits  booleans representing white/black QR Code modules
     * @param hints decoding hints that should be used to influence decoding
     * @return text and bytes encoded within the QR Code
     * @throws FormatException   if the QR Code cannot be decoded
     * @throws ChecksumException if error correction fails
     */
    @JvmOverloads
    @Throws(FormatException::class, ChecksumException::class)
    fun decode(bits: BitMatrix?, hints: Map<DecodeHintType?, *>? = null): DecoderResult {

        // Construct a parser and read version, error-correction level
        val parser = BitMatrixParser(bits!!)
        var fe: FormatException? = null
        var ce: ChecksumException? = null
        try {
            return decode(parser, hints)
        } catch (e: FormatException) {
            fe = e
        } catch (e: ChecksumException) {
            ce = e
        }
        return try {

            // Revert the bit matrix
            parser.remask()

            // Will be attempting a mirrored reading of the version and format info.
            parser.setMirror(true)

            // Preemptively read the version.
            parser.readVersion()

            // Preemptively read the format information.
            parser.readFormatInformation()

            /*
             * Since we're here, this means we have successfully detected some kind
             * of version and format information when mirrored. This is a good sign,
             * that the QR code may be mirrored, and we should try once more with a
             * mirrored content.
             */
            // Prepare for a mirrored reading.
            parser.mirror()
            val result = decode(parser, hints)

            // Success! Notify the caller that the code was mirrored.
            result.other = QRCodeDecoderMetaData(true)
            result
        } catch (e: FormatException) {
            // Throw the exception from the original reading
            if (fe != null) {
                throw fe
            }
            throw ce!! // If fe is null, this can't be
        } catch (e: ChecksumException) {
            if (fe != null) {
                throw fe
            }
            throw ce!!
        }
    }

    @Throws(FormatException::class, ChecksumException::class)
    private fun decode(parser: BitMatrixParser, hints: Map<DecodeHintType?, *>?): DecoderResult {
        val version = parser.readVersion()
        val ecLevel = parser.readFormatInformation().errorCorrectionLevel

        // Read codewords
        val codewords = parser.readCodewords()
        // Separate into data blocks
        val dataBlocks = getDataBlocks(codewords, version, ecLevel)

        // Count total number of data bytes
        var totalBytes = 0
        for (dataBlock in dataBlocks) {
            totalBytes += dataBlock!!.numDataCodewords
        }
        val resultBytes = ByteArray(totalBytes)
        var resultOffset = 0

        // Error-correct and copy data blocks together into a stream of bytes
        for (dataBlock in dataBlocks) {
            val codewordBytes = dataBlock!!.codewords
            val numDataCodewords = dataBlock.numDataCodewords
            correctErrors(codewordBytes, numDataCodewords)
            for (i in 0 until numDataCodewords) {
                resultBytes[resultOffset++] = codewordBytes[i]
            }
        }

        // Decode the contents of that stream of bytes
        return decode(resultBytes, version, ecLevel, hints!!)
    }

    /**
     *
     * Given data and error-correction codewords received, possibly corrupted by errors, attempts to
     * correct the errors in-place using Reed-Solomon error correction.
     *
     * @param codewordBytes    data and error correction codewords
     * @param numDataCodewords number of codewords that are data bytes
     * @throws ChecksumException if error correction fails
     */
    @Throws(ChecksumException::class)
    private fun correctErrors(codewordBytes: ByteArray, numDataCodewords: Int) {
        val numCodewords = codewordBytes.size
        // First read into an array of ints
        val codewordsInts = IntArray(numCodewords)
        for (i in 0 until numCodewords) {
            codewordsInts[i] = codewordBytes[i] and 0xFF
        }
        runCatching {
            rsDecoder.decode(codewordsInts, codewordBytes.size - numDataCodewords)
        }.getOrElse { throw checksumInstance }
        // Copy back into array of bytes -- only need to worry about the bytes that were data
        // We don't care about errors in the error-correction codewords
        for (i in 0 until numDataCodewords) {
            codewordBytes[i] = codewordsInts[i].toByte()
        }
    }

}