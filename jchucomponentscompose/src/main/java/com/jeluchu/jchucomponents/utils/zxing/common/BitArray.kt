/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils.zxing.common

class BitArray : Cloneable {
    /**
     * @return underlying array of ints. The first element holds the first 32 bits, and the least
     * significant bit is bit 0.
     */
    var bitArray: IntArray
        private set
    var size: Int
        private set

    constructor() {
        size = 0
        bitArray = IntArray(1)
    }

    internal constructor(bits: IntArray, size: Int) {
        bitArray = bits
        this.size = size
    }

    val sizeInBytes: Int
        get() = (size + 7) / 8

    private fun ensureCapacity(size: Int) {
        if (size > bitArray.size * 32) {
            val newBits = makeArray(size)
            System.arraycopy(bitArray, 0, newBits, 0, bitArray.size)
            bitArray = newBits
        }
    }

    /**
     * @param i bit to get
     * @return true iff bit i is set
     */
    operator fun get(i: Int): Boolean = bitArray[i / 32] and (1 shl (i and 0x1F)) != 0

    /**
     * Sets bit i.
     *
     * @param i bit to set
     */
    fun set(i: Int) {
        bitArray[i / 32] = bitArray[i / 32] or (1 shl (i and 0x1F))
    }

    fun appendBit(bit: Boolean) {
        ensureCapacity(size + 1)
        if (bit) {
            bitArray[size / 32] = bitArray[size / 32] or (1 shl (size and 0x1F))
        }
        size++
    }

    /**
     * Appends the least-significant bits, from value, in order from most-significant to
     * least-significant. For example, appending 6 bits from 0x000001E will append the bits
     * 0, 1, 1, 1, 1, 0 in that order.
     *
     * @param value `int` containing bits to append
     * @param numBits bits from value to append
     */
    fun appendBits(value: Int, numBits: Int) {
        require(!(numBits < 0 || numBits > 32)) { "Num bits must be between 0 and 32" }
        ensureCapacity(size + numBits)
        for (numBitsLeft in numBits downTo 1) {
            appendBit(value shr numBitsLeft - 1 and 0x01 == 1)
        }
    }

    fun appendBitArray(other: BitArray) {
        val otherSize = other.size
        ensureCapacity(size + otherSize)
        for (i in 0 until otherSize) {
            appendBit(other[i])
        }
    }

    fun xor(other: BitArray) {
        require(size == other.size) { "Sizes don't match" }
        for (i in bitArray.indices) {
            // The last int could be incomplete (i.e. not have 32 bits in
            // it) but there is no problem since 0 XOR 0 == 0.
            bitArray[i] = bitArray[i] xor other.bitArray[i]
        }
    }

    /**
     *
     * @param bitOffset first bit to start writing
     * @param array array to write into. Bytes are written most-significant byte first. This is the opposite
     * of the internal representation, which is exposed by [.getBitArray]
     * @param offset position in array to start writing
     * @param numBytes how many bytes to write
     */
    fun toBytes(bitOffset: Int, array: ByteArray, offset: Int, numBytes: Int) {
        var bitOff = bitOffset
        for (i in 0 until numBytes) {
            var theByte = 0
            for (j in 0..7) {
                if (get(bitOff)) {
                    theByte = theByte or (1 shl 7 - j)
                }
                bitOff++
            }
            array[offset + i] = theByte.toByte()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is BitArray) return false
        return size == other.size && bitArray.contentEquals(other.bitArray)
    }

    override fun hashCode(): Int = 31 * size + bitArray.contentHashCode()

    override fun toString(): String {
        val result = StringBuilder(size + size / 8 + 1)
        for (i in 0 until size) {
            if (i and 0x07 == 0) {
                result.append(' ')
            }
            result.append(if (get(i)) 'X' else '.')
        }
        return result.toString()
    }

    public override fun clone(): BitArray = BitArray(bitArray.clone(), size)

    companion object {
        private fun makeArray(size: Int): IntArray = IntArray((size + 31) / 32)
    }

}