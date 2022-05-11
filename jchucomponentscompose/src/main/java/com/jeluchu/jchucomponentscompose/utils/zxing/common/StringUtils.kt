/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.utils.zxing.common

import com.jeluchu.jchucomponentscompose.utils.zxing.DecodeHintType
import java.nio.charset.Charset
import kotlin.experimental.and

object StringUtils {
    private val PLATFORM_DEFAULT_ENCODING = Charset.defaultCharset().name()
    const val SHIFT_JIS = "SJIS"
    const val GB2312 = "GB2312"
    private const val EUC_JP = "EUC_JP"
    private const val UTF8 = "UTF8"
    private const val ISO88591 = "ISO8859_1"
    private val ASSUME_SHIFT_JIS = SHIFT_JIS.equals(PLATFORM_DEFAULT_ENCODING, ignoreCase = true) ||
            EUC_JP.equals(PLATFORM_DEFAULT_ENCODING, ignoreCase = true)

    /**
     * @param bytes bytes encoding a string, whose encoding should be guessed
     * @param hints decode hints if applicable
     * @return name of guessed encoding; at the moment will only guess one of:
     * [.SHIFT_JIS], [.UTF8], [.ISO88591], or the platform
     * default encoding if none of these can possibly be correct
     */
    @JvmStatic
    fun guessEncoding(bytes: ByteArray, hints: Map<DecodeHintType?, *>?): String {
        if (hints != null && hints.containsKey(DecodeHintType.CHARACTER_SET)) {
            return hints[DecodeHintType.CHARACTER_SET].toString()
        }
        // For now, merely tries to distinguish ISO-8859-1, UTF-8 and Shift_JIS,
        // which should be by far the most common encodings.
        val length = bytes.size
        var canBeISO88591 = true
        var canBeShiftJIS = true
        var canBeUTF8 = true
        var utf8BytesLeft = 0
        var utf2BytesChars = 0
        var utf3BytesChars = 0
        var utf4BytesChars = 0
        var sjisBytesLeft = 0
        var sjisKatakanaChars = 0
        var sjisCurKatakanaWordLength = 0
        var sjisCurDoubleBytesWordLength = 0
        var sjisMaxKatakanaWordLength = 0
        var sjisMaxDoubleBytesWordLength = 0
        var isoHighOther = 0
        val utf8bom =
            bytes.size > 3 && bytes[0] == 0xEF.toByte() && bytes[1] == 0xBB.toByte() && bytes[2] == 0xBF.toByte()
        var i = 0
        while (i < length && (canBeISO88591 || canBeShiftJIS || canBeUTF8)) {
            val value: Int = (bytes[i] and 0xFF.toByte()).toInt()

            // UTF-8 stuff
            if (canBeUTF8) {
                if (utf8BytesLeft > 0) {
                    if (value and 0x80 == 0) {
                        canBeUTF8 = false
                    } else {
                        utf8BytesLeft--
                    }
                } else if (value and 0x80 != 0) {
                    if (value and 0x40 == 0) {
                        canBeUTF8 = false
                    } else {
                        utf8BytesLeft++
                        if (value and 0x20 == 0) {
                            utf2BytesChars++
                        } else {
                            utf8BytesLeft++
                            if (value and 0x10 == 0) {
                                utf3BytesChars++
                            } else {
                                utf8BytesLeft++
                                if (value and 0x08 == 0) {
                                    utf4BytesChars++
                                } else {
                                    canBeUTF8 = false
                                }
                            }
                        }
                    }
                }
            }

            // ISO-8859-1 stuff
            if (canBeISO88591) {
                if (value in 0x80..0x9f) {
                    canBeISO88591 = false
                } else if (value > 0x9F && (value < 0xC0 || value == 0xD7 || value == 0xF7)) {
                    isoHighOther++
                }
            }

            // Shift_JIS stuff
            if (canBeShiftJIS) {
                if (sjisBytesLeft > 0) {
                    if (value < 0x40 || value == 0x7F || value > 0xFC) {
                        canBeShiftJIS = false
                    } else {
                        sjisBytesLeft--
                    }
                } else if (value == 0x80 || value == 0xA0 || value > 0xEF) {
                    canBeShiftJIS = false
                } else if (value in 0xa1..0xdf) {
                    sjisKatakanaChars++
                    sjisCurDoubleBytesWordLength = 0
                    sjisCurKatakanaWordLength++
                    if (sjisCurKatakanaWordLength > sjisMaxKatakanaWordLength) {
                        sjisMaxKatakanaWordLength = sjisCurKatakanaWordLength
                    }
                } else if (value > 0x7F) {
                    sjisBytesLeft++
                    //sjisDoubleBytesChars++;
                    sjisCurKatakanaWordLength = 0
                    sjisCurDoubleBytesWordLength++
                    if (sjisCurDoubleBytesWordLength > sjisMaxDoubleBytesWordLength) {
                        sjisMaxDoubleBytesWordLength = sjisCurDoubleBytesWordLength
                    }
                } else {
                    //sjisLowChars++;
                    sjisCurKatakanaWordLength = 0
                    sjisCurDoubleBytesWordLength = 0
                }
            }
            i++
        }
        if (canBeUTF8 && utf8BytesLeft > 0) {
            canBeUTF8 = false
        }
        if (canBeShiftJIS && sjisBytesLeft > 0) {
            canBeShiftJIS = false
        }

        // Easy -- if there is BOM or at least 1 valid not-single byte character (and no evidence it can't be UTF-8), done
        if (canBeUTF8 && (utf8bom || utf2BytesChars + utf3BytesChars + utf4BytesChars > 0)) {
            return UTF8
        }
        // Easy -- if assuming Shift_JIS or >= 3 valid consecutive not-ascii characters (and no evidence it can't be), done
        if (canBeShiftJIS && (ASSUME_SHIFT_JIS || sjisMaxKatakanaWordLength >= 3 || sjisMaxDoubleBytesWordLength >= 3)) {
            return SHIFT_JIS
        }
        // Distinguishing Shift_JIS and ISO-8859-1 can be a little tough for short words. The crude heuristic is:
        // - If we saw
        //   - only two consecutive katakana chars in the whole text, or
        //   - at least 10% of bytes that could be "upper" not-alphanumeric Latin1,
        // - then we conclude Shift_JIS, else ISO-8859-1
        if (canBeISO88591 && canBeShiftJIS) {
            return if (sjisMaxKatakanaWordLength == 2 && sjisKatakanaChars == 2 || isoHighOther * 10 >= length) SHIFT_JIS else ISO88591
        }

        // Otherwise, try in order ISO-8859-1, Shift JIS, UTF-8 and fall back to default platform encoding
        if (canBeISO88591) {
            return ISO88591
        }
        if (canBeShiftJIS) {
            return SHIFT_JIS
        }
        return if (canBeUTF8) {
            UTF8
        } else PLATFORM_DEFAULT_ENCODING
        // Otherwise, we take a wild guess with platform encoding
    }
}