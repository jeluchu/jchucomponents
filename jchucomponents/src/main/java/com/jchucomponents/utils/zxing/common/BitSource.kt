/*
 *
 *  Copyright 2022 Jeluchu
 *
 */
package com.jchucomponents.utils.zxing.common

import okhttp3.internal.and

/**
 *
 * This provides an easy abstraction to read bits at a time from a sequence of bytes, where the
 * number of bits read is not often a multiple of 8.
 *
 *
 * This class is thread-safe but not reentrant -- unless the caller modifies the bytes array
 * it passed in, in which case all bets are off.
 */
class BitSource
/**
 * @param bytes bytes from which this will read bits. Bits will be read from the first byte first.
 * Bits are read within a byte from most-significant to least-significant bit.
 */(private val bytes: ByteArray) {
    private var byteOffset = 0
    private var bitOffset = 0

    /**
     * @param numBits number of bits to read
     * @return int representing the bits read. The bits will appear as the least-significant
     * bits of the int
     * @throws IllegalArgumentException if numBits isn't in [1,32] or more than is available
     */
    fun readBits(numBits: Int): Int {
        var numBits = numBits
        require(!(numBits < 1 || numBits > 32 || numBits > available())) { numBits.toString() }
        var result = 0

        // First, read remainder from current byte
        if (bitOffset > 0) {
            val bitsLeft = 8 - bitOffset
            val toRead = Math.min(numBits, bitsLeft)
            val bitsToNotRead = bitsLeft - toRead
            val mask = 0xFF shr 8 - toRead shl bitsToNotRead
            result = bytes[byteOffset] and mask shr bitsToNotRead
            numBits -= toRead
            bitOffset += toRead
            if (bitOffset == 8) {
                bitOffset = 0
                byteOffset++
            }
        }

        // Next read whole bytes
        if (numBits > 0) {
            while (numBits >= 8) {
                result = result shl 8 or (bytes[byteOffset] and 0xFF)
                byteOffset++
                numBits -= 8
            }

            // Finally read a partial byte
            if (numBits > 0) {
                val bitsToNotRead = 8 - numBits
                val mask = 0xFF shr bitsToNotRead shl bitsToNotRead
                result = result shl numBits or (bytes[byteOffset] and mask shr bitsToNotRead)
                bitOffset += numBits
            }
        }
        return result
    }

    /**
     * @return number of bits that can be read successfully
     */
    fun available(): Int {
        return 8 * (bytes.size - byteOffset) - bitOffset
    }
}