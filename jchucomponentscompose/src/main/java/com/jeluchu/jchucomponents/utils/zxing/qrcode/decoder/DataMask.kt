/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils.zxing.qrcode.decoder

import com.jeluchu.jchucomponents.utils.zxing.common.BitMatrix

/**
 *
 * Encapsulates data masks for the data bits in a QR code, per ISO 18004:2006 6.8. Implementations
 * of this class can un-mask a raw BitMatrix. For simplicity, they will unmask the entire BitMatrix,
 * including areas used for finder patterns, timing patterns, etc. These areas should be unused
 * after the point they are unmasked anyway.
 *
 *
 * Note that the diagram in section 6.8.1 is misleading since it indicates that i is column position
 * and j is row position. In fact, as the text says, i is row position and j is column position.
 *
 */
internal enum class DataMask {
    // See ISO 18004:2006 6.8.1
    /**
     * 000: mask bits for which (x + y) mod 2 == 0
     */
    DATA_MASK_000 {
        override fun isMasked(i: Int, j: Int): Boolean {
            return i + j and 0x01 == 0
        }
    },

    /**
     * 001: mask bits for which x mod 2 == 0
     */
    DATA_MASK_001 {
        override fun isMasked(i: Int, j: Int): Boolean {
            return i and 0x01 == 0
        }
    },

    /**
     * 010: mask bits for which y mod 3 == 0
     */
    DATA_MASK_010 {
        override fun isMasked(i: Int, j: Int): Boolean {
            return j % 3 == 0
        }
    },

    /**
     * 011: mask bits for which (x + y) mod 3 == 0
     */
    DATA_MASK_011 {
        override fun isMasked(i: Int, j: Int): Boolean {
            return (i + j) % 3 == 0
        }
    },

    /**
     * 100: mask bits for which (x/2 + y/3) mod 2 == 0
     */
    DATA_MASK_100 {
        override fun isMasked(i: Int, j: Int): Boolean {
            return i / 2 + j / 3 and 0x01 == 0
        }
    },

    /**
     * 101: mask bits for which xy mod 2 + xy mod 3 == 0
     * equivalently, such that xy mod 6 == 0
     */
    DATA_MASK_101 {
        override fun isMasked(i: Int, j: Int): Boolean {
            return i * j % 6 == 0
        }
    },

    /**
     * 110: mask bits for which (xy mod 2 + xy mod 3) mod 2 == 0
     * equivalently, such that xy mod 6 < 3
     */
    DATA_MASK_110 {
        override fun isMasked(i: Int, j: Int): Boolean {
            return i * j % 6 < 3
        }
    },

    /**
     * 111: mask bits for which ((x+y)mod 2 + xy mod 3) mod 2 == 0
     * equivalently, such that (x + y + xy mod 3) mod 2 == 0
     */
    DATA_MASK_111 {
        override fun isMasked(i: Int, j: Int): Boolean {
            return i + j + i * j % 3 and 0x01 == 0
        }
    };
    // End of enum constants.
    /**
     *
     * Implementations of this method reverse the data masking process applied to a QR Code and
     * make its bits ready to read.
     *
     * @param bits      representation of QR Code bits
     * @param dimension dimension of QR Code, represented by bits, being unmasked
     */
    fun unmaskBitMatrix(bits: BitMatrix, dimension: Int) {
        for (i in 0 until dimension) {
            for (j in 0 until dimension) {
                if (isMasked(i, j)) {
                    bits.flip(j, i)
                }
            }
        }
    }

    abstract fun isMasked(i: Int, j: Int): Boolean
}