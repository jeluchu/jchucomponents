/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.utils.zxing.qrcode.decoder

import com.jchucomponents.utils.zxing.DecodeHintType
import com.jchucomponents.utils.zxing.FormatException
import com.jchucomponents.utils.zxing.FormatException.Companion.formatInstance
import com.jchucomponents.utils.zxing.common.BitSource
import com.jchucomponents.utils.zxing.common.CharacterSetECI
import com.jchucomponents.utils.zxing.common.DecoderResult
import com.jchucomponents.utils.zxing.common.StringUtils
import com.jchucomponents.utils.zxing.common.StringUtils.guessEncoding
import com.jchucomponents.utils.zxing.qrcode.decoder.Mode.Companion.forBits

/**
 *
 * QR Codes can encode text as bits in one of several modes, and can use multiple modes
 * in one QR Code. This class decodes the bits back into text.
 *
 *
 * See ISO 18004:2006, 6.4.3 - 6.4.7
 */
internal object DecodedBitStreamParser {
    /**
     * See ISO 18004:2006, 6.4.4 Table 5
     */
    private val ALPHANUMERIC_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ $%*+-./:".toCharArray()
    private const val GB2312_SUBSET = 1

    @JvmStatic
    @Throws(FormatException::class)
    fun decode(
        bytes: ByteArray?,
        version: Version?,
        ecLevel: ErrorCorrectionLevel?,
        hints: Map<DecodeHintType?, *>
    ): DecoderResult {
        val bits = BitSource(bytes!!)
        val result = StringBuilder(50)
        val byteSegments: MutableList<ByteArray> = ArrayList(1)
        var symbolSequence = -1
        var parityData = -1
        runCatching {
            var currentCharacterSetECI: CharacterSetECI? = null
            var fc1InEffect = false
            var mode: Mode
            do {
                // While still another segment to read...
                mode = if (bits.available() < 4) {
                    // OK, assume we're done. Really, a TERMINATOR mode should have been recorded here
                    Mode.TERMINATOR
                } else {
                    forBits(bits.readBits(4)) // mode is encoded by 4 bits
                }
                when (mode) {
                    Mode.TERMINATOR -> {}
                    Mode.FNC1_FIRST_POSITION, Mode.FNC1_SECOND_POSITION ->                         // We do little with FNC1 except alter the parsed result a bit according to the spec
                        fc1InEffect = true
                    Mode.STRUCTURED_APPEND -> {
                        if (bits.available() < 16) {
                            throw formatInstance
                        }
                        // sequence number and parity is added later to the result metadata
                        // Read next 8 bits (symbol sequence #) and 8 bits (parity data), then continue
                        symbolSequence = bits.readBits(8)
                        parityData = bits.readBits(8)
                    }
                    Mode.ECI -> {
                        // Count doesn't apply to ECI
                        val value = parseECIValue(bits)
                        currentCharacterSetECI = CharacterSetECI.getCharacterSetECIByValue(value)
                        if (currentCharacterSetECI == null) {
                            throw formatInstance
                        }
                    }
                    Mode.HANZI -> {
                        // First handle Hanzi mode which does not start with character count
                        // Chinese mode contains a sub set indicator right after mode indicator
                        val subset = bits.readBits(4)
                        val countHanzi = bits.readBits(mode.getCharacterCountBits(version!!))
                        if (subset == GB2312_SUBSET) {
                            decodeHanziSegment(bits, result, countHanzi)
                        }
                    }
                    else -> {
                        // "Normal" QR code modes:
                        // How many characters will follow, encoded in this mode?
                        val count = bits.readBits(mode.getCharacterCountBits(version!!))
                        when (mode) {
                            Mode.NUMERIC -> decodeNumericSegment(bits, result, count)
                            Mode.ALPHANUMERIC -> decodeAlphanumericSegment(
                                bits,
                                result,
                                count,
                                fc1InEffect
                            )
                            Mode.BYTE -> decodeByteSegment(
                                bits,
                                result,
                                count,
                                currentCharacterSetECI,
                                byteSegments,
                                hints
                            )
                            Mode.KANJI -> decodeKanjiSegment(bits, result, count)
                            else -> throw formatInstance
                        }
                    }
                }
            } while (mode !== Mode.TERMINATOR)
        }.getOrElse { throw formatInstance }

        return DecoderResult(
            bytes,
            result.toString(),
            (if (byteSegments.isEmpty()) null else byteSegments)!!,
            ecLevel?.toString()!!,
            symbolSequence,
            parityData
        )
    }

    /**
     * See specification GBT 18284-2000
     */
    @Throws(FormatException::class)
    private fun decodeHanziSegment(
        bits: BitSource,
        result: StringBuilder,
        count: Int
    ) {
        // Don't crash trying to read more bits than we have available.
        var mCount = count
        if (mCount * 13 > bits.available()) {
            throw formatInstance
        }

        // Each character will require 2 bytes. Read the characters as 2-byte pairs
        // and decode as GB2312 afterwards
        val buffer = ByteArray(2 * mCount)
        var offset = 0
        while (mCount > 0) {
            // Each 13 bits encodes a 2-byte character
            val twoBytes = bits.readBits(13)
            var assembledTwoBytes = twoBytes / 0x060 shl 8 or twoBytes % 0x060
            assembledTwoBytes += if (assembledTwoBytes < 0x00A00) {
                // In the 0xA1A1 to 0xAAFE range
                0x0A1A1
            } else {
                // In the 0xB0A1 to 0xFAFE range
                0x0A6A1
            }
            buffer[offset] = (assembledTwoBytes shr 8 and 0xFF).toByte()
            buffer[offset + 1] = (assembledTwoBytes and 0xFF).toByte()
            offset += 2
            mCount--
        }
        runCatching {
            result.append(String(buffer, charset(StringUtils.GB2312)))
        }.getOrElse { throw formatInstance }
    }

    @Throws(FormatException::class)
    private fun decodeKanjiSegment(
        bits: BitSource,
        result: StringBuilder,
        count: Int
    ) {
        // Don't crash trying to read more bits than we have available.
        var mCount = count
        if (mCount * 13 > bits.available()) {
            throw formatInstance
        }

        // Each character will require 2 bytes. Read the characters as 2-byte pairs
        // and decode as Shift_JIS afterwards
        val buffer = ByteArray(2 * mCount)
        var offset = 0
        while (mCount > 0) {
            // Each 13 bits encodes a 2-byte character
            val twoBytes = bits.readBits(13)
            var assembledTwoBytes = twoBytes / 0x0C0 shl 8 or twoBytes % 0x0C0
            assembledTwoBytes += if (assembledTwoBytes < 0x01F00) {
                // In the 0x8140 to 0x9FFC range
                0x08140
            } else {
                // In the 0xE040 to 0xEBBF range
                0x0C140
            }
            buffer[offset] = (assembledTwoBytes shr 8).toByte()
            buffer[offset + 1] = assembledTwoBytes.toByte()
            offset += 2
            mCount--
        }
        // Shift_JIS may not be supported in some environments:
        runCatching {
            result.append(String(buffer, charset(StringUtils.SHIFT_JIS)))
        }.getOrElse { throw formatInstance }

    }

    @Throws(FormatException::class)
    private fun decodeByteSegment(
        bits: BitSource,
        result: StringBuilder,
        count: Int,
        currentCharacterSetECI: CharacterSetECI?,
        byteSegments: MutableCollection<ByteArray>,
        hints: Map<DecodeHintType?, *>
    ) {
        // Don't crash trying to read more bits than we have available.
        if (8 * count > bits.available()) {
            throw formatInstance
        }
        val readBytes = ByteArray(count)
        for (i in 0 until count) {
            readBytes[i] = bits.readBits(8).toByte()
        }
        val encoding: String = currentCharacterSetECI?.name
            ?: // The spec isn't clear on this mode; see
            // section 6.4.5: t does not say which encoding to assuming
            // upon decoding. I have seen ISO-8859-1 used as well as
            // Shift_JIS -- without anything like an ECI designator to
            // give a hint.
            guessEncoding(readBytes, hints)
        runCatching {
            result.append(String(readBytes, charset(encoding)))
        }.getOrElse { throw formatInstance }

        byteSegments.add(readBytes)
    }

    @Throws(FormatException::class)
    private fun toAlphaNumericChar(value: Int): Char {
        if (value >= ALPHANUMERIC_CHARS.size) {
            throw formatInstance
        }
        return ALPHANUMERIC_CHARS[value]
    }

    @Throws(FormatException::class)
    private fun decodeAlphanumericSegment(
        bits: BitSource,
        result: StringBuilder,
        count: Int,
        fc1InEffect: Boolean
    ) {
        // Read two characters at a time
        var mCount = count
        val start = result.length
        while (mCount > 1) {
            if (bits.available() < 11) {
                throw formatInstance
            }
            val nextTwoCharsBits = bits.readBits(11)
            result.append(toAlphaNumericChar(nextTwoCharsBits / 45))
            result.append(toAlphaNumericChar(nextTwoCharsBits % 45))
            mCount -= 2
        }
        if (mCount == 1) {
            // special case: one character left
            if (bits.available() < 6) {
                throw formatInstance
            }
            result.append(toAlphaNumericChar(bits.readBits(6)))
        }
        // See section 6.4.8.1, 6.4.8.2
        if (fc1InEffect) {
            // We need to massage the result a bit if in an FNC1 mode:
            for (i in start until result.length) {
                if (result[i] == '%') {
                    if (i < result.length - 1 && result[i + 1] == '%') {
                        // %% is rendered as %
                        result.deleteCharAt(i + 1)
                    } else {
                        // In alpha mode, % should be converted to FNC1 separator 0x1D
                        result.setCharAt(i, 0x1D.toChar())
                    }
                }
            }
        }
    }

    @Throws(FormatException::class)
    private fun decodeNumericSegment(
        bits: BitSource,
        result: StringBuilder,
        count: Int
    ) {
        // Read three digits at a time
        var mCount = count
        while (mCount >= 3) {
            // Each 10 bits encodes three digits
            if (bits.available() < 10) {
                throw formatInstance
            }
            val threeDigitsBits = bits.readBits(10)
            if (threeDigitsBits >= 1000) {
                throw formatInstance
            }
            result.append(toAlphaNumericChar(threeDigitsBits / 100))
            result.append(toAlphaNumericChar(threeDigitsBits / 10 % 10))
            result.append(toAlphaNumericChar(threeDigitsBits % 10))
            mCount -= 3
        }
        if (mCount == 2) {
            // Two digits left over to read, encoded in 7 bits
            if (bits.available() < 7) {
                throw formatInstance
            }
            val twoDigitsBits = bits.readBits(7)
            if (twoDigitsBits >= 100) {
                throw formatInstance
            }
            result.append(toAlphaNumericChar(twoDigitsBits / 10))
            result.append(toAlphaNumericChar(twoDigitsBits % 10))
        } else if (mCount == 1) {
            // One digit left over to read
            if (bits.available() < 4) {
                throw formatInstance
            }
            val digitBits = bits.readBits(4)
            if (digitBits >= 10) {
                throw formatInstance
            }
            result.append(toAlphaNumericChar(digitBits))
        }
    }

    @Throws(FormatException::class)
    private fun parseECIValue(bits: BitSource): Int {
        val firstByte = bits.readBits(8)
        if (firstByte and 0x80 == 0) {
            // just one byte
            return firstByte and 0x7F
        }
        if (firstByte and 0xC0 == 0x80) {
            // two bytes
            val secondByte = bits.readBits(8)
            return firstByte and 0x3F shl 8 or secondByte
        }
        if (firstByte and 0xE0 == 0xC0) {
            // three bytes
            val secondThirdBytes = bits.readBits(16)
            return firstByte and 0x1F shl 16 or secondThirdBytes
        }
        throw formatInstance
    }
}