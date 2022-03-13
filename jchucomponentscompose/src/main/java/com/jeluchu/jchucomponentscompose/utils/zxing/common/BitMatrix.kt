package com.jeluchu.jchucomponentscompose.utils.zxing.common

class BitMatrix : Cloneable {
    /**
     * @return The width of the matrix
     */
    val width: Int

    /**
     * @return The height of the matrix
     */
    val height: Int
    private val rowSize: Int
    private val bits: IntArray
    private val multiple: Int

    /**
     * Creates an empty square `BitMatrix`.
     *
     * @param dimension height and width
     */
    constructor(dimension: Int) : this(dimension, dimension, 1)

    /**
     * Creates an empty `BitMatrix`.
     *
     * @param width  bit matrix width
     * @param height bit matrix height
     */
    constructor(width: Int, height: Int, multiple: Int) {
        require(!(width < 1 || height < 1)) { "Both dimensions must be greater than 0" }
        this.width = width
        this.height = height
        this.multiple = multiple
        rowSize = (width + 31) / 32
        bits = IntArray(rowSize * height)
    }

    private constructor(width: Int, height: Int, rowSize: Int, bits: IntArray) {
        this.width = width
        this.height = height
        this.rowSize = rowSize
        this.bits = bits
        multiple = 1
    }

    /**
     *
     * Gets the requested bit, where true means black.
     *
     * @param x The horizontal component (i.e. which column)
     * @param y The vertical component (i.e. which row)
     * @return value of given bit in matrix
     */
    operator fun get(x: Int, y: Int): Boolean {
        val offset = y * rowSize + x / 32
        return bits[offset] ushr (x and 0x1f) and 1 != 0
    }

    /**
     *
     * Sets the given bit to true.
     *
     * @param x The horizontal component (i.e. which column)
     * @param y The vertical component (i.e. which row)
     */
    operator fun set(x: Int, y: Int) {
        val offset = y * rowSize + x / 32
        bits[offset] = bits[offset] or (1 shl (x and 0x1f))
    }

    /**
     *
     * Flips the given bit.
     *
     * @param x The horizontal component (i.e. which column)
     * @param y The vertical component (i.e. which row)
     */
    fun flip(x: Int, y: Int) {
        val offset = y * rowSize + x / 32
        bits[offset] = bits[offset] xor (1 shl (x and 0x1f))
    }

    /**
     * Clears all bits (sets to false).
     */
    fun clear() {
        val max = bits.size
        for (i in 0 until max) {
            bits[i] = 0
        }
    }

    /**
     *
     * Sets a square region of the bit matrix to true.
     *
     * @param left   The horizontal position to begin at (inclusive)
     * @param top    The vertical position to begin at (inclusive)
     * @param width  The width of the region
     * @param height The height of the region
     */
    fun setRegion(left: Int, top: Int, width: Int, height: Int) {
        require(!(top < 0 || left < 0)) { "Left and top must be nonnegative" }
        require(!(height < 1 || width < 1)) { "Height and width must be at least 1" }
        val right = left + width
        val bottom = top + height
        require(!(bottom > this.height || right > this.width)) { "The region must fit inside the matrix" }
        for (y in top until bottom) {
            val offset = y * rowSize
            for (x in left until right) {
                bits[offset + x / 32] = bits[offset + x / 32] or (1 shl (x and 0x1f))
            }
        }
    }

    /**
     * This is useful in detecting a corner of a 'pure' barcode.
     *
     * @return `x,y` coordinate of top-left-most 1 bit, or null if it is all white
     */
    val topLeftOnBit: IntArray?
        get() {
            var bitsOffset = 0
            while (bitsOffset < bits.size && bits[bitsOffset] == 0) {
                bitsOffset++
            }
            if (bitsOffset == bits.size) {
                return null
            }
            val y = bitsOffset / rowSize
            var x = bitsOffset % rowSize * 32
            val theBits = bits[bitsOffset]
            var bit = 0
            while (theBits shl 31 - bit == 0) {
                bit++
            }
            x += bit
            return intArrayOf(x, y)
        }
    val bottomRightOnBit: IntArray?
        get() {
            var bitsOffset = bits.size - 1
            while (bitsOffset >= 0 && bits[bitsOffset] == 0) {
                bitsOffset--
            }
            if (bitsOffset < 0) {
                return null
            }
            val y = bitsOffset / rowSize
            var x = bitsOffset % rowSize * 32
            val theBits = bits[bitsOffset]
            var bit = 31
            while (theBits ushr bit == 0) {
                bit--
            }
            x += bit
            return intArrayOf(x, y)
        }

    override fun equals(o: Any?): Boolean {
        if (o !is BitMatrix) {
            return false
        }
        return width == o.width && height == o.height && rowSize == o.rowSize &&
                bits.contentEquals(o.bits)
    }

    override fun hashCode(): Int {
        var hash = width
        hash = 31 * hash + width
        hash = 31 * hash + height
        hash = 31 * hash + rowSize
        hash = 31 * hash + bits.contentHashCode()
        return hash
    }

    /**
     * @return string representation using "X" for set and " " for unset bits
     */
    override fun toString(): String {
        return toString("X ", "  ")
    }

    /**
     * @param setString   representation of a set bit
     * @param unsetString representation of an unset bit
     * @return string representation of entire matrix utilizing given strings
     */
    fun toString(setString: String, unsetString: String): String {
        return buildToString(setString, unsetString, "\n")
    }

    /**
     * @param setString     representation of a set bit
     * @param unsetString   representation of an unset bit
     * @param lineSeparator newline character in string representation
     * @return string representation of entire matrix utilizing given strings and line separator
     */
    @Deprecated("call {@link #toString(String, String)} only, which uses \n line separator always")
    fun toString(setString: String, unsetString: String, lineSeparator: String): String {
        return buildToString(setString, unsetString, lineSeparator)
    }

    private fun buildToString(
        setString: String,
        unsetString: String,
        lineSeparator: String
    ): String {
        val result = StringBuilder(height * (width + 1))
        for (y in 0 until height) {
            for (x in 0 until width) {
                result.append(if (get(x, y)) setString else unsetString)
            }
            result.append(lineSeparator)
        }
        return result.toString()
    }

    public override fun clone(): BitMatrix {
        return BitMatrix(width, height, rowSize, bits.clone())
    }

    companion object {
        /**
         * Interprets a 2D array of booleans as a `BitMatrix`, where "true" means an "on" bit.
         *
         * @param image bits of the image, as a row-major 2D array. Elements are arrays representing rows
         * @return `BitMatrix` representation of image
         */
        fun parse(image: Array<BooleanArray?>?): BitMatrix {
            val height = image?.size ?: 0
            val width: Int = image?.get(0)?.size ?: 0
            val bits = BitMatrix(width, height, 1)
            for (i in 0 until height) {
                val imageI = image?.get(i)
                for (j in 0 until width) {
                    if (imageI?.get(j) == true) {
                        bits[j] = i
                    }
                }
            }
            return bits
        }

        fun parse(
            stringRepresentation: String?,
            setString: String,
            unsetString: String
        ): BitMatrix {
            requireNotNull(stringRepresentation)
            val bits = BooleanArray(stringRepresentation.length)
            var bitsPos = 0
            var rowStartPos = 0
            var rowLength = -1
            var nRows = 0
            var pos = 0
            while (pos < stringRepresentation.length) {
                if (stringRepresentation[pos] == '\n' ||
                    stringRepresentation[pos] == '\r'
                ) {
                    if (bitsPos > rowStartPos) {
                        if (rowLength == -1) {
                            rowLength = bitsPos - rowStartPos
                        } else require(bitsPos - rowStartPos == rowLength) { "row lengths do not match" }
                        rowStartPos = bitsPos
                        nRows++
                    }
                    pos++
                } else if (stringRepresentation.startsWith(setString, pos)) {
                    pos += setString.length
                    bits[bitsPos] = true
                    bitsPos++
                } else if (stringRepresentation.startsWith(unsetString, pos)) {
                    pos += unsetString.length
                    bits[bitsPos] = false
                    bitsPos++
                } else {
                    throw IllegalArgumentException(
                        "illegal character encountered: " + stringRepresentation.substring(pos)
                    )
                }
            }

            // no EOL at end?
            if (bitsPos > rowStartPos) {
                if (rowLength == -1) {
                    rowLength = bitsPos - rowStartPos
                } else require(bitsPos - rowStartPos == rowLength) { "row lengths do not match" }
                nRows++
            }
            val matrix = BitMatrix(rowLength, nRows, 1)
            for (i in 0 until bitsPos) {
                if (bits[i]) {
                    matrix[i % rowLength] = i / rowLength
                }
            }
            return matrix
        }
    }
}