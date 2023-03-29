/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.utilities.zxing.qrcode.decoder

/**
 *
 * Encapsulates a QR Code's format information, including the data mask used and
 * error correction level.
 *
 * @see DataMask
 *
 * @see ErrorCorrectionLevel
 */
internal class FormatInformation private constructor(formatInfo: Int) {
    val errorCorrectionLevel: ErrorCorrectionLevel =
        ErrorCorrectionLevel.forBits(formatInfo shr 3 and 0x03)
    val dataMask: Byte = (formatInfo and 0x07).toByte()
    override fun hashCode(): Int {
        return errorCorrectionLevel.ordinal shl 3 or dataMask.toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is FormatInformation) return false
        return errorCorrectionLevel == other.errorCorrectionLevel &&
                dataMask == other.dataMask
    }

    companion object {
        private const val FORMAT_INFO_MASK_QR = 0x5412

        /**
         * See ISO 18004:2006, Annex C, Table C.1
         */
        private val FORMAT_INFO_DECODE_LOOKUP = arrayOf(
            intArrayOf(0x5412, 0x00),
            intArrayOf(0x5125, 0x01),
            intArrayOf(0x5E7C, 0x02),
            intArrayOf(0x5B4B, 0x03),
            intArrayOf(0x45F9, 0x04),
            intArrayOf(0x40CE, 0x05),
            intArrayOf(0x4F97, 0x06),
            intArrayOf(0x4AA0, 0x07),
            intArrayOf(0x77C4, 0x08),
            intArrayOf(0x72F3, 0x09),
            intArrayOf(0x7DAA, 0x0A),
            intArrayOf(0x789D, 0x0B),
            intArrayOf(0x662F, 0x0C),
            intArrayOf(0x6318, 0x0D),
            intArrayOf(0x6C41, 0x0E),
            intArrayOf(0x6976, 0x0F),
            intArrayOf(0x1689, 0x10),
            intArrayOf(0x13BE, 0x11),
            intArrayOf(0x1CE7, 0x12),
            intArrayOf(0x19D0, 0x13),
            intArrayOf(0x0762, 0x14),
            intArrayOf(0x0255, 0x15),
            intArrayOf(0x0D0C, 0x16),
            intArrayOf(0x083B, 0x17),
            intArrayOf(0x355F, 0x18),
            intArrayOf(0x3068, 0x19),
            intArrayOf(0x3F31, 0x1A),
            intArrayOf(0x3A06, 0x1B),
            intArrayOf(0x24B4, 0x1C),
            intArrayOf(0x2183, 0x1D),
            intArrayOf(0x2EDA, 0x1E),
            intArrayOf(0x2BED, 0x1F)
        )

        fun numBitsDiffering(a: Int, b: Int): Int = Integer.bitCount(a xor b)

        /**
         * @param maskedFormatInfo1 format info indicator, with mask still applied
         * @param maskedFormatInfo2 second copy of same info; both are checked at the same time
         * to establish best match
         * @return information about the format it specifies, or `null`
         * if doesn't seem to match any known pattern
         */
        @JvmStatic
        fun decodeFormatInformation(
            maskedFormatInfo1: Int,
            maskedFormatInfo2: Int
        ): FormatInformation? {
            val formatInfo = doDecodeFormatInformation(maskedFormatInfo1, maskedFormatInfo2)
            return formatInfo
                ?: doDecodeFormatInformation(
                    maskedFormatInfo1 xor FORMAT_INFO_MASK_QR,
                    maskedFormatInfo2 xor FORMAT_INFO_MASK_QR
                )
            // Should return null, but, some QR codes apparently
            // do not mask this info. Try again by actually masking the pattern
            // first
        }

        private fun doDecodeFormatInformation(
            maskedFormatInfo1: Int,
            maskedFormatInfo2: Int
        ): FormatInformation? {
            // Find the int in FORMAT_INFO_DECODE_LOOKUP with fewest bits differing
            var bestDifference = Int.MAX_VALUE
            var bestFormatInfo = 0
            for (decodeInfo in FORMAT_INFO_DECODE_LOOKUP) {
                val targetInfo = decodeInfo[0]
                if (targetInfo == maskedFormatInfo1 || targetInfo == maskedFormatInfo2) {
                    // Found an exact match
                    return FormatInformation(decodeInfo[1])
                }
                var bitsDifference = numBitsDiffering(maskedFormatInfo1, targetInfo)
                if (bitsDifference < bestDifference) {
                    bestFormatInfo = decodeInfo[1]
                    bestDifference = bitsDifference
                }
                if (maskedFormatInfo1 != maskedFormatInfo2) {
                    // also try the other option
                    bitsDifference = numBitsDiffering(maskedFormatInfo2, targetInfo)
                    if (bitsDifference < bestDifference) {
                        bestFormatInfo = decodeInfo[1]
                        bestDifference = bitsDifference
                    }
                }
            }
            // Hamming distance of the 32 masked codes is 7, by construction, so <= 3 bits
            // differing means we found a match
            return if (bestDifference <= 3) {
                FormatInformation(bestFormatInfo)
            } else null
        }
    }

}