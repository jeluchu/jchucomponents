package com.jeluchu.jchucomponentscompose.utils.zxing.common;

import java.util.Arrays;

public final class BitMatrix implements Cloneable {

    private final int width;
    private final int height;
    private final int rowSize;
    private final int[] bits;
    private final int multiple;

    /**
     * Creates an empty square {@code BitMatrix}.
     *
     * @param dimension height and width
     */
    public BitMatrix(int dimension) {
        this(dimension, dimension, 1);
    }

    /**
     * Creates an empty {@code BitMatrix}.
     *
     * @param width  bit matrix width
     * @param height bit matrix height
     */
    public BitMatrix(int width, int height, int multiple) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Both dimensions must be greater than 0");
        }
        this.width = width;
        this.height = height;
        this.multiple = multiple;
        this.rowSize = (width + 31) / 32;
        bits = new int[rowSize * height];
    }

    private BitMatrix(int width, int height, int rowSize, int[] bits) {
        this.width = width;
        this.height = height;
        this.rowSize = rowSize;
        this.bits = bits;
        this.multiple = 1;
    }

    /**
     * Interprets a 2D array of booleans as a {@code BitMatrix}, where "true" means an "on" bit.
     *
     * @param image bits of the image, as a row-major 2D array. Elements are arrays representing rows
     * @return {@code BitMatrix} representation of image
     */
    public static BitMatrix parse(boolean[][] image) {
        int height = image.length;
        int width = image[0].length;
        BitMatrix bits = new BitMatrix(width, height, 1);
        for (int i = 0; i < height; i++) {
            boolean[] imageI = image[i];
            for (int j = 0; j < width; j++) {
                if (imageI[j]) {
                    bits.set(j, i);
                }
            }
        }
        return bits;
    }

    public static BitMatrix parse(String stringRepresentation, String setString, String unsetString) {
        if (stringRepresentation == null) {
            throw new IllegalArgumentException();
        }

        boolean[] bits = new boolean[stringRepresentation.length()];
        int bitsPos = 0;
        int rowStartPos = 0;
        int rowLength = -1;
        int nRows = 0;
        int pos = 0;
        while (pos < stringRepresentation.length()) {
            if (stringRepresentation.charAt(pos) == '\n' ||
                    stringRepresentation.charAt(pos) == '\r') {
                if (bitsPos > rowStartPos) {
                    if (rowLength == -1) {
                        rowLength = bitsPos - rowStartPos;
                    } else if (bitsPos - rowStartPos != rowLength) {
                        throw new IllegalArgumentException("row lengths do not match");
                    }
                    rowStartPos = bitsPos;
                    nRows++;
                }
                pos++;
            } else if (stringRepresentation.startsWith(setString, pos)) {
                pos += setString.length();
                bits[bitsPos] = true;
                bitsPos++;
            } else if (stringRepresentation.startsWith(unsetString, pos)) {
                pos += unsetString.length();
                bits[bitsPos] = false;
                bitsPos++;
            } else {
                throw new IllegalArgumentException(
                        "illegal character encountered: " + stringRepresentation.substring(pos));
            }
        }

        // no EOL at end?
        if (bitsPos > rowStartPos) {
            if (rowLength == -1) {
                rowLength = bitsPos - rowStartPos;
            } else if (bitsPos - rowStartPos != rowLength) {
                throw new IllegalArgumentException("row lengths do not match");
            }
            nRows++;
        }

        BitMatrix matrix = new BitMatrix(rowLength, nRows, 1);
        for (int i = 0; i < bitsPos; i++) {
            if (bits[i]) {
                matrix.set(i % rowLength, i / rowLength);
            }
        }
        return matrix;
    }

    /**
     * <p>Gets the requested bit, where true means black.</p>
     *
     * @param x The horizontal component (i.e. which column)
     * @param y The vertical component (i.e. which row)
     * @return value of given bit in matrix
     */
    public boolean get(int x, int y) {
        int offset = y * rowSize + (x / 32);
        return ((bits[offset] >>> (x & 0x1f)) & 1) != 0;
    }

    /**
     * <p>Sets the given bit to true.</p>
     *
     * @param x The horizontal component (i.e. which column)
     * @param y The vertical component (i.e. which row)
     */
    public void set(int x, int y) {
        int offset = y * rowSize + (x / 32);
        bits[offset] |= 1 << (x & 0x1f);
    }

    /**
     * <p>Flips the given bit.</p>
     *
     * @param x The horizontal component (i.e. which column)
     * @param y The vertical component (i.e. which row)
     */
    public void flip(int x, int y) {
        int offset = y * rowSize + (x / 32);
        bits[offset] ^= 1 << (x & 0x1f);
    }

    /**
     * Clears all bits (sets to false).
     */
    public void clear() {
        int max = bits.length;
        for (int i = 0; i < max; i++) {
            bits[i] = 0;
        }
    }

    /**
     * <p>Sets a square region of the bit matrix to true.</p>
     *
     * @param left   The horizontal position to begin at (inclusive)
     * @param top    The vertical position to begin at (inclusive)
     * @param width  The width of the region
     * @param height The height of the region
     */
    public void setRegion(int left, int top, int width, int height) {
        if (top < 0 || left < 0) {
            throw new IllegalArgumentException("Left and top must be nonnegative");
        }
        if (height < 1 || width < 1) {
            throw new IllegalArgumentException("Height and width must be at least 1");
        }
        int right = left + width;
        int bottom = top + height;
        if (bottom > this.height || right > this.width) {
            throw new IllegalArgumentException("The region must fit inside the matrix");
        }
        for (int y = top; y < bottom; y++) {
            int offset = y * rowSize;
            for (int x = left; x < right; x++) {
                bits[offset + (x / 32)] |= 1 << (x & 0x1f);
            }
        }
    }

    /**
     * This is useful in detecting a corner of a 'pure' barcode.
     *
     * @return {@code x,y} coordinate of top-left-most 1 bit, or null if it is all white
     */
    public int[] getTopLeftOnBit() {
        int bitsOffset = 0;
        while (bitsOffset < bits.length && bits[bitsOffset] == 0) {
            bitsOffset++;
        }
        if (bitsOffset == bits.length) {
            return null;
        }
        int y = bitsOffset / rowSize;
        int x = (bitsOffset % rowSize) * 32;

        int theBits = bits[bitsOffset];
        int bit = 0;
        while ((theBits << (31 - bit)) == 0) {
            bit++;
        }
        x += bit;
        return new int[]{x, y};
    }

    public int[] getBottomRightOnBit() {
        int bitsOffset = bits.length - 1;
        while (bitsOffset >= 0 && bits[bitsOffset] == 0) {
            bitsOffset--;
        }
        if (bitsOffset < 0) {
            return null;
        }

        int y = bitsOffset / rowSize;
        int x = (bitsOffset % rowSize) * 32;

        int theBits = bits[bitsOffset];
        int bit = 31;
        while ((theBits >>> bit) == 0) {
            bit--;
        }
        x += bit;

        return new int[]{x, y};
    }

    /**
     * @return The width of the matrix
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return The height of the matrix
     */
    public int getHeight() {
        return height;
    }

    public int getMultiple() {
        return multiple;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BitMatrix)) {
            return false;
        }
        BitMatrix other = (BitMatrix) o;
        return width == other.width && height == other.height && rowSize == other.rowSize &&
                Arrays.equals(bits, other.bits);
    }

    @Override
    public int hashCode() {
        int hash = width;
        hash = 31 * hash + width;
        hash = 31 * hash + height;
        hash = 31 * hash + rowSize;
        hash = 31 * hash + Arrays.hashCode(bits);
        return hash;
    }

    /**
     * @return string representation using "X" for set and " " for unset bits
     */
    @Override
    public String toString() {
        return toString("X ", "  ");
    }

    /**
     * @param setString   representation of a set bit
     * @param unsetString representation of an unset bit
     * @return string representation of entire matrix utilizing given strings
     */
    public String toString(String setString, String unsetString) {
        return buildToString(setString, unsetString, "\n");
    }

    /**
     * @param setString     representation of a set bit
     * @param unsetString   representation of an unset bit
     * @param lineSeparator newline character in string representation
     * @return string representation of entire matrix utilizing given strings and line separator
     * @deprecated call {@link #toString(String, String)} only, which uses \n line separator always
     */
    @Deprecated
    public String toString(String setString, String unsetString, String lineSeparator) {
        return buildToString(setString, unsetString, lineSeparator);
    }

    private String buildToString(String setString, String unsetString, String lineSeparator) {
        StringBuilder result = new StringBuilder(height * (width + 1));
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result.append(get(x, y) ? setString : unsetString);
            }
            result.append(lineSeparator);
        }
        return result.toString();
    }

    @Override
    public BitMatrix clone() {
        return new BitMatrix(width, height, rowSize, bits.clone());
    }

}
