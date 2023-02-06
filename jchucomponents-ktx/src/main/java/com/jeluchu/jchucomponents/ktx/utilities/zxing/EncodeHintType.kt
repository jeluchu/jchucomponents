/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.utilities.zxing

/**
 * These are a set of hints that you may pass to Writers to specify their behavior.
 *
 */
enum class EncodeHintType {
    /**
     * Specifies what degree of error correction to use, for example in QR Codes.
     * Type depends on the encoder. For example for QR codes it's type
     * [ErrorCorrectionLevel][com.jeluchu.jchucomponents.utils.zxing.qrcode.decoder.ErrorCorrectionLevel].
     * For Aztec it is of type [Integer], representing the minimal percentage of error correction words.
     * For PDF417 it is of type [Integer], valid values being 0 to 8.
     * In all cases, it can also be a [String] representation of the desired value as well.
     * Note: an Aztec symbol should have a minimum of 25% EC words.
     */
    ERROR_CORRECTION,

    /**
     * Specifies what character encoding to use where applicable (type [String])
     */
    CHARACTER_SET,

    /**
     * Specifies margin, in pixels, to use when generating the barcode. The meaning can vary
     * by format; for example it controls margin before and after the barcode horizontally for
     * most 1D formats. (Type [Integer], or [String] representation of the integer value).
     */
    MARGIN,

    /**
     * Specifies the exact version of QR code to be encoded.
     * (Type [Integer], or [String] representation of the integer value).
     */
    QR_VERSION,

    /**
     * Specifies the QR code mask pattern to be used. Allowed values are
     * 0..QRCode.NUM_MASK_PATTERNS-1. By default the code will automatically select
     * the optimal mask pattern.
     * * (Type [Integer], or [String] representation of the integer value).
     */
    QR_MASK_PATTERN,

    /**
     * Specifies whether the data should be encoded to the GS1 standard (type [Boolean], or "true" or "false"
     * [String] value).
     */
    GS1_FORMAT
}