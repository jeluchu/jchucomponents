/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.utilities.zxing.common

/**
 *
 * Encapsulates the result of decoding a matrix of bits. This typically
 * applies to 2D barcode formats. For now it contains the raw bytes obtained,
 * as well as a String interpretation of those bytes, if applicable.
 *
 */
class DecoderResult(
    /**
     * @return raw bytes representing the result, or `null` if not applicable
     */
    val rawBytes: ByteArray,
    /**
     * @return text representation of the result
     */
    val text: String,
    /**
     * @return list of byte segments in the result, or `null` if not applicable
     */
    val byteSegments: List<ByteArray>,
    /**
     * @return name of error correction level used, or `null` if not applicable
     */
    val eCLevel: String,
    val structuredAppendSequenceNumber: Int,
    val structuredAppendParity: Int
) {

    /**
     * @return arbitrary additional metadata
     */
    var other: Any? = null
    fun hasStructuredAppend(): Boolean =
        structuredAppendParity >= 0 && structuredAppendSequenceNumber >= 0

}