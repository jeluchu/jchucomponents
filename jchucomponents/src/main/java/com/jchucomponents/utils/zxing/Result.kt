/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.utils.zxing

import java.util.*

class Result(
    /**
     * @return raw text encoded by the barcode
     */
    val text: String,
    /**
     * @return raw bytes encoded by the barcode, if applicable, otherwise `null`
     */
    val rawBytes: ByteArray?,
    /**
     * @return how many bits of [.getRawBytes] are valid; typically 8 times its length
     * @since 3.3.0
     */
    val numBits: Int,
    /**
     * @return points related to the barcode in the image. These are typically points
     * identifying finder patterns or the corners of the barcode. The exact meaning is
     * specific to the type of barcode that was decoded.
     */
    var resultPoints: Array<ResultPoint?>,
    /**
     * @return [BarcodeFormat] representing the format of the barcode that was decoded
     */
    val barcodeFormat: BarcodeFormat?,
    val timestamp: Long
) {

    private var resultMetadata: MutableMap<ResultMetadataType, Any>? = null

    @JvmOverloads
    constructor(
        text: String,
        rawBytes: ByteArray?,
        resultPoints: Array<ResultPoint?>,
        format: BarcodeFormat?,
        timestamp: Long = System.currentTimeMillis()
    ) : this(
        text, rawBytes, if (rawBytes == null) 0 else 8 * rawBytes.size,
        resultPoints, format, timestamp
    )

    fun putMetadata(type: ResultMetadataType, value: Any) {
        if (resultMetadata == null) {
            resultMetadata = EnumMap(ResultMetadataType::class.java)
        }
        resultMetadata!![type] = value
    }

    override fun toString(): String = text

}