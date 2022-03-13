package com.jeluchu.jchucomponentscompose.utils.zxing

/**
 * Encapsulates a type of hint that a caller may pass to a barcode reader to help it
 * more quickly or accurately decode it. It is up to implementations to decide what,
 * if anything, to do with the information that is supplied.
 *
 * @see Reader.decode
 */
enum class DecodeHintType {

    /**
     * Image is a pure monochrome image of a barcode. Doesn't matter what it maps to;
     * use [Boolean.TRUE].
     */
    PURE_BARCODE,

    /**
     * Spend more time to try to find a barcode; optimize for accuracy, not speed.
     * Doesn't matter what it maps to; use [Boolean.TRUE].
     */
    TRY_HARDER,

    /**
     * Specifies what character encoding to use when decoding, where applicable (type String)
     */
    CHARACTER_SET,

    /**
     * The caller needs to be notified via callback when a possible [ResultPoint]
     * is found. Maps to a [ResultPointCallback].
     */
    NEED_RESULT_POINT_CALLBACK;
    // End of enumeration values.

}