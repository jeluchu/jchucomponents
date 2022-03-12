/*
 * Copyright 2007 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeluchu.jchucomponentscompose.utils.zxing

/**
 * Encapsulates a type of hint that a caller may pass to a barcode reader to help it
 * more quickly or accurately decode it. It is up to implementations to decide what,
 * if anything, to do with the information that is supplied.
 *
 * @author Sean Owen
 * @author dswitkin@google.com (Daniel Switkin)
 * @see Reader.decode
 */
enum class DecodeHintType(
    /**
     * Data type the hint is expecting.
     * Among the possible values the [Void] stands out as being used for
     * hints that do not expect a value to be supplied (flag hints). Such hints
     * will possibly have their value ignored, or replaced by a
     * [Boolean.TRUE]. Hint suppliers should probably use
     * [Boolean.TRUE] as directed by the actual hint documentation.
     */
    val valueType: Class<*>
) {
    /**
     * Unspecified, application-specific hint. Maps to an unspecified [Object].
     */
    OTHER(Any::class.java),

    /**
     * Image is a pure monochrome image of a barcode. Doesn't matter what it maps to;
     * use [Boolean.TRUE].
     */
    PURE_BARCODE(Void::class.java),

    /**
     * Image is known to be of one of a few possible formats.
     * Maps to a [List] of [BarcodeFormat]s.
     */
    POSSIBLE_FORMATS(MutableList::class.java),

    /**
     * Spend more time to try to find a barcode; optimize for accuracy, not speed.
     * Doesn't matter what it maps to; use [Boolean.TRUE].
     */
    TRY_HARDER(Void::class.java),

    /**
     * Specifies what character encoding to use when decoding, where applicable (type String)
     */
    CHARACTER_SET(String::class.java),

    /**
     * Allowed lengths of encoded data -- reject anything else. Maps to an `int[]`.
     */
    ALLOWED_LENGTHS(IntArray::class.java),

    /**
     * Assume Code 39 codes employ a check digit. Doesn't matter what it maps to;
     * use [Boolean.TRUE].
     */
    ASSUME_CODE_39_CHECK_DIGIT(Void::class.java),

    /**
     * Assume the barcode is being processed as a GS1 barcode, and modify behavior as needed.
     * For example this affects FNC1 handling for Code 128 (aka GS1-128). Doesn't matter what it maps to;
     * use [Boolean.TRUE].
     */
    ASSUME_GS1(Void::class.java),

    /**
     * If true, return the start and end digits in a Codabar barcode instead of stripping them. They
     * are alpha, whereas the rest are numeric. By default, they are stripped, but this causes them
     * to not be. Doesn't matter what it maps to; use [Boolean.TRUE].
     */
    RETURN_CODABAR_START_END(Void::class.java),

    /**
     * The caller needs to be notified via callback when a possible [ResultPoint]
     * is found. Maps to a [ResultPointCallback].
     */
    NEED_RESULT_POINT_CALLBACK(ResultPointCallback::class.java),

    /**
     * Allowed extension lengths for EAN or UPC barcodes. Other formats will ignore this.
     * Maps to an `int[]` of the allowed extension lengths, for example [2], [5], or [2, 5].
     * If it is optional to have an extension, do not set this hint. If this is set,
     * and a UPC or EAN barcode is found but an extension is not, then no result will be returned
     * at all.
     */
    ALLOWED_EAN_EXTENSIONS(IntArray::class.java);
    // End of enumeration values.

}