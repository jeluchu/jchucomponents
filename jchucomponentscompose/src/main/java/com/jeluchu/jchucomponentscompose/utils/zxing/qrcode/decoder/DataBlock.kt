/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.decoder

/**
 *
 * Encapsulates a block of data within a QR Code. QR Codes may split their data into
 * multiple blocks, each of which is a unit of data and error-correction codewords. Each
 * is represented by an instance of this class.
 *
 */
internal class DataBlock private constructor(val numDataCodewords: Int, val codewords: ByteArray) {

    companion object {
        /**
         *
         * When QR Codes use multiple data blocks, they are actually interleaved.
         * That is, the first byte of data block 1 to n is written, then the second bytes, and so on. This
         * method will separate the data into original blocks.
         *
         * @param rawCodewords bytes as read directly from the QR Code
         * @param version      version of the QR Code
         * @param ecLevel      error-correction level of the QR Code
         * @return DataBlocks containing original bytes, "de-interleaved" from representation in the
         * QR Code
         */
        @JvmStatic
        fun getDataBlocks(
            rawCodewords: ByteArray,
            version: Version,
            ecLevel: ErrorCorrectionLevel?
        ): Array<DataBlock?> {
            require(rawCodewords.size == version.totalCodewords)

            // Figure out the number and size of data blocks used by this version and
            // error correction level
            val ecBlocks = ecLevel?.let { version.getECBlocksForLevel(it) }

            // First count the total number of data blocks
            var totalBlocks = 0
            val ecBlockArray = ecBlocks?.eCBlocks
            if (ecBlockArray != null) {
                for (ecBlock in ecBlockArray) {
                    totalBlocks += ecBlock.count
                }
            }

            // Now establish DataBlocks of the appropriate size and number of data codewords
            val result = arrayOfNulls<DataBlock>(totalBlocks)
            var numResultBlocks = 0
            if (ecBlockArray != null) {
                for (ecBlock in ecBlockArray) {
                    for (i in 0 until ecBlock.count) {
                        val numDataCodewords = ecBlock.dataCodewords
                        val numBlockCodewords = ecBlocks.eCCodewordsPerBlock + numDataCodewords
                        result[numResultBlocks++] =
                            DataBlock(numDataCodewords, ByteArray(numBlockCodewords))
                    }
                }
            }

            // All blocks have the same amount of data, except that the last n
            // (where n may be 0) have 1 more byte. Figure out where these start.
            val shorterBlocksTotalCodewords = result[0]!!.codewords.size
            var longerBlocksStartAt = result.size - 1
            while (longerBlocksStartAt >= 0) {
                val numCodewords = result[longerBlocksStartAt]!!.codewords.size
                if (numCodewords == shorterBlocksTotalCodewords) {
                    break
                }
                longerBlocksStartAt--
            }
            longerBlocksStartAt++
            val shorterBlocksNumDataCodewords =
                shorterBlocksTotalCodewords - (ecBlocks?.eCCodewordsPerBlock ?: 0)
            // The last elements of result may be 1 element longer;
            // first fill out as many elements as all of them have
            var rawCodewordsOffset = 0
            for (i in 0 until shorterBlocksNumDataCodewords) {
                for (j in 0 until numResultBlocks) {
                    result[j]!!.codewords[i] = rawCodewords[rawCodewordsOffset++]
                }
            }
            // Fill out the last data block in the longer ones
            for (j in longerBlocksStartAt until numResultBlocks) {
                result[j]!!.codewords[shorterBlocksNumDataCodewords] =
                    rawCodewords[rawCodewordsOffset++]
            }
            // Now add in error correction blocks
            val max = result[0]!!.codewords.size
            for (i in shorterBlocksNumDataCodewords until max) {
                for (j in 0 until numResultBlocks) {
                    val iOffset = if (j < longerBlocksStartAt) i else i + 1
                    result[j]!!.codewords[iOffset] = rawCodewords[rawCodewordsOffset++]
                }
            }
            return result
        }
    }
}