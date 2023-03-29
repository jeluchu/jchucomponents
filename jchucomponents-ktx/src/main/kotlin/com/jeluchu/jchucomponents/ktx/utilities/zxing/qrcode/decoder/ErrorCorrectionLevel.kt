/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.utilities.zxing.qrcode.decoder

/**
 *
 * See ISO 18004:2006, 6.5.1. This enum encapsulates the four error correction levels
 * defined by the QR code standard.
 *
 */
enum class ErrorCorrectionLevel(val bits: Int) {
    /**
     * L = ~7% correction
     */
    L(0x01),

    /**
     * M = ~15% correction
     */
    M(0x00),

    /**
     * Q = ~25% correction
     */
    Q(0x03),

    /**
     * H = ~30% correction
     */
    H(0x02);

    companion object {
        private val FOR_BITS = arrayOf(M, L, H, Q)

        /**
         * @param bits int containing the two bits encoding a QR Code's error correction level
         * @return ErrorCorrectionLevel representing the encoded error correction level
         */
        fun forBits(bits: Int): ErrorCorrectionLevel {
            require(!(bits < 0 || bits >= FOR_BITS.size))
            return FOR_BITS[bits]
        }
    }
}