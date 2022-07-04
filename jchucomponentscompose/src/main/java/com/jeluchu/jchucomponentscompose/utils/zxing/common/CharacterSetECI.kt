/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.utils.zxing.common

import com.jeluchu.jchucomponentscompose.utils.zxing.FormatException
import com.jeluchu.jchucomponentscompose.utils.zxing.FormatException.Companion.formatInstance

/**
 * Encapsulates a Character Set ECI, according to "Extended Channel Interpretations" 5.3.1.1
 * of ISO 18004.
 *
 */
enum class CharacterSetECI {
    // Enum name is a Java encoding valid for java.lang and java.io
    Cp437(intArrayOf(0, 2)), ISO8859_1(intArrayOf(1, 3), "ISO-8859-1"), ISO8859_2(
        4,
        "ISO-8859-2"
    ),
    ISO8859_3(5, "ISO-8859-3"), ISO8859_4(6, "ISO-8859-4"), ISO8859_5(7, "ISO-8859-5"), ISO8859_6(
        8,
        "ISO-8859-6"
    ),
    ISO8859_7(9, "ISO-8859-7"), ISO8859_8(10, "ISO-8859-8"), ISO8859_9(
        11,
        "ISO-8859-9"
    ),
    ISO8859_10(12, "ISO-8859-10"), ISO8859_11(13, "ISO-8859-11"), ISO8859_13(
        15,
        "ISO-8859-13"
    ),
    ISO8859_14(16, "ISO-8859-14"), ISO8859_15(17, "ISO-8859-15"), ISO8859_16(
        18,
        "ISO-8859-16"
    ),
    SJIS(20, "Shift_JIS"), Cp1250(21, "windows-1250"), Cp1251(22, "windows-1251"), Cp1252(
        23,
        "windows-1252"
    ),
    Cp1256(24, "windows-1256"), UnicodeBigUnmarked(25, "UTF-16BE", "UnicodeBig"), UTF8(
        26,
        "UTF-8"
    ),
    ASCII(intArrayOf(27, 170), "US-ASCII"), Big5(28), GB18030(
        29,
        "GB2312",
        "EUC_CN",
        "GBK"
    ),
    EUC_KR(30, "EUC-KR");

    companion object {
        private val VALUE_TO_ECI: MutableMap<Int, CharacterSetECI> = HashMap()
        private val NAME_TO_ECI: MutableMap<String, CharacterSetECI> = HashMap()

        /**
         * @param value character set ECI value
         * @return `CharacterSetECI` representing ECI of given value, or null if it is legal but
         * unsupported
         * @throws FormatException if ECI value is invalid
         */
        @Throws(FormatException::class)
        fun getCharacterSetECIByValue(value: Int): CharacterSetECI? {
            if (value < 0 || value >= 900) throw formatInstance
            return VALUE_TO_ECI[value]
        }

        /**
         * @param name character set ECI encoding name
         * @return CharacterSetECI representing ECI for character encoding, or null if it is legal
         * but unsupported
         */
        fun getCharacterSetECIByName(name: String): CharacterSetECI? = NAME_TO_ECI[name]

        init {
            for (eci in values()) {
                for (value in eci.values) {
                    VALUE_TO_ECI[value] = eci
                }
                NAME_TO_ECI[eci.name] = eci
                for (name in eci.otherEncodingNames) {
                    NAME_TO_ECI[name] = eci
                }
            }
        }
    }

    private val values: IntArray
    private val otherEncodingNames: Array<String>

    constructor(value: Int) : this(intArrayOf(value))
    constructor(value: Int, vararg otherEncodingNames: String) {
        values = intArrayOf(value)
        this.otherEncodingNames = otherEncodingNames as Array<String>
    }

    constructor(values: IntArray, vararg otherEncodingNames: String) {
        this.values = values
        this.otherEncodingNames = otherEncodingNames as Array<String>
    }

    val value: Int
        get() = values[0]
}