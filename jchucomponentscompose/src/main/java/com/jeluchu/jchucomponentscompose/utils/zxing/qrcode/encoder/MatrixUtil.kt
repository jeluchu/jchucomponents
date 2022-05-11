/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.encoder

import com.jeluchu.jchucomponentscompose.utils.zxing.WriterException
import com.jeluchu.jchucomponentscompose.utils.zxing.common.BitArray
import com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.decoder.Version
import com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.encoder.MaskUtil.getDataMaskBit
import com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.encoder.QRCode.Companion.isValidMaskPattern

internal object MatrixUtil {
    private val POSITION_DETECTION_PATTERN = arrayOf(
        intArrayOf(1, 1, 1, 1, 1, 1, 1),
        intArrayOf(1, 0, 0, 0, 0, 0, 1),
        intArrayOf(1, 0, 1, 1, 1, 0, 1),
        intArrayOf(1, 0, 1, 1, 1, 0, 1),
        intArrayOf(1, 0, 1, 1, 1, 0, 1),
        intArrayOf(1, 0, 0, 0, 0, 0, 1),
        intArrayOf(1, 1, 1, 1, 1, 1, 1)
    )
    private val POSITION_ADJUSTMENT_PATTERN = arrayOf(
        intArrayOf(1, 1, 1, 1, 1),
        intArrayOf(1, 0, 0, 0, 1),
        intArrayOf(1, 0, 1, 0, 1),
        intArrayOf(1, 0, 0, 0, 1),
        intArrayOf(1, 1, 1, 1, 1)
    )

    // From Appendix E. Table 1, JIS0510X:2004 (p 71). The table was double-checked by komatsu.
    private val POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE = arrayOf(
        intArrayOf(-1, -1, -1, -1, -1, -1, -1),
        intArrayOf(6, 18, -1, -1, -1, -1, -1),
        intArrayOf(6, 22, -1, -1, -1, -1, -1),
        intArrayOf(6, 26, -1, -1, -1, -1, -1),
        intArrayOf(6, 30, -1, -1, -1, -1, -1),
        intArrayOf(6, 34, -1, -1, -1, -1, -1),
        intArrayOf(6, 22, 38, -1, -1, -1, -1),
        intArrayOf(6, 24, 42, -1, -1, -1, -1),
        intArrayOf(6, 26, 46, -1, -1, -1, -1),
        intArrayOf(6, 28, 50, -1, -1, -1, -1),
        intArrayOf(6, 30, 54, -1, -1, -1, -1),
        intArrayOf(6, 32, 58, -1, -1, -1, -1),
        intArrayOf(6, 34, 62, -1, -1, -1, -1),
        intArrayOf(6, 26, 46, 66, -1, -1, -1),
        intArrayOf(6, 26, 48, 70, -1, -1, -1),
        intArrayOf(6, 26, 50, 74, -1, -1, -1),
        intArrayOf(6, 30, 54, 78, -1, -1, -1),
        intArrayOf(6, 30, 56, 82, -1, -1, -1),
        intArrayOf(6, 30, 58, 86, -1, -1, -1),
        intArrayOf(6, 34, 62, 90, -1, -1, -1),
        intArrayOf(6, 28, 50, 72, 94, -1, -1),
        intArrayOf(6, 26, 50, 74, 98, -1, -1),
        intArrayOf(6, 30, 54, 78, 102, -1, -1),
        intArrayOf(6, 28, 54, 80, 106, -1, -1),
        intArrayOf(6, 32, 58, 84, 110, -1, -1),
        intArrayOf(6, 30, 58, 86, 114, -1, -1),
        intArrayOf(6, 34, 62, 90, 118, -1, -1),
        intArrayOf(6, 26, 50, 74, 98, 122, -1),
        intArrayOf(6, 30, 54, 78, 102, 126, -1),
        intArrayOf(6, 26, 52, 78, 104, 130, -1),
        intArrayOf(6, 30, 56, 82, 108, 134, -1),
        intArrayOf(6, 34, 60, 86, 112, 138, -1),
        intArrayOf(6, 30, 58, 86, 114, 142, -1),
        intArrayOf(6, 34, 62, 90, 118, 146, -1),
        intArrayOf(6, 30, 54, 78, 102, 126, 150),
        intArrayOf(6, 24, 50, 76, 102, 128, 154),
        intArrayOf(6, 28, 54, 80, 106, 132, 158),
        intArrayOf(6, 32, 58, 84, 110, 136, 162),
        intArrayOf(6, 26, 54, 82, 110, 138, 166),
        intArrayOf(6, 30, 58, 86, 114, 142, 170)
    )

    // Type info cells at the left top corner.
    private val TYPE_INFO_COORDINATES = arrayOf(
        intArrayOf(8, 0),
        intArrayOf(8, 1),
        intArrayOf(8, 2),
        intArrayOf(8, 3),
        intArrayOf(8, 4),
        intArrayOf(8, 5),
        intArrayOf(8, 7),
        intArrayOf(8, 8),
        intArrayOf(7, 8),
        intArrayOf(5, 8),
        intArrayOf(4, 8),
        intArrayOf(3, 8),
        intArrayOf(2, 8),
        intArrayOf(1, 8),
        intArrayOf(0, 8)
    )

    // From Appendix D in JISX0510:2004 (p. 67)
    private const val VERSION_INFO_POLY = 0x1f25 // 1 1111 0010 0101

    // From Appendix C in JISX0510:2004 (p.65).
    private const val TYPE_INFO_POLY = 0x537
    private const val TYPE_INFO_MASK_PATTERN = 0x5412

    // Set all cells to -1.  -1 means that the cell is empty (not set yet).
    //
    // JAVAPORT: We shouldn't need to do this at all. The code should be rewritten to begin encoding
    // with the ByteMatrix initialized all to zero.
    private fun clearMatrix(matrix: ByteMatrix) = matrix.clear((-1).toByte())

    // Build 2D matrix of QR Code from "dataBits" with "ecLevel", "version" and "getMaskPattern". On
    // success, store the result in "matrix" and return true.
    @Throws(WriterException::class)
    fun buildMatrix(
        dataBits: BitArray,
        ecLevel: ErrorCorrectionLevel,
        version: Version,
        maskPattern: Int,
        matrix: ByteMatrix
    ) {
        clearMatrix(matrix)
        embedBasicPatterns(version, matrix)
        // Type information appear with any version.
        embedTypeInfo(ecLevel, maskPattern, matrix)
        // Version info appear if version >= 7.
        maybeEmbedVersionInfo(version, matrix)
        // Data should be embedded at end.
        embedDataBits(dataBits, maskPattern, matrix)
    }

    // Embed basic patterns. On success, modify the matrix and return true.
    // The basic patterns are:
    // - Position detection patterns
    // - Timing patterns
    // - Dark dot at the left bottom corner
    // - Position adjustment patterns, if need be
    @Throws(WriterException::class)
    fun embedBasicPatterns(version: Version, matrix: ByteMatrix) {
        // Let's get started with embedding big squares at corners.
        embedPositionDetectionPatternsAndSeparators(matrix)
        // Then, embed the dark dot at the left bottom corner.
        embedDarkDotAtLeftBottomCorner(matrix)

        // Position adjustment patterns appear if version >= 2.
        maybeEmbedPositionAdjustmentPatterns(version, matrix)
        // Timing patterns should be embedded after position adj. patterns.
        embedTimingPatterns(matrix)
    }

    // Embed type information. On success, modify the matrix.
    @Throws(WriterException::class)
    fun embedTypeInfo(ecLevel: ErrorCorrectionLevel, maskPattern: Int, matrix: ByteMatrix) {
        val typeInfoBits = BitArray()
        makeTypeInfoBits(ecLevel, maskPattern, typeInfoBits)
        for (i in 0 until typeInfoBits.size) {
            // Place bits in LSB to MSB order.  LSB (least significant bit) is the last value in
            // "typeInfoBits".
            val bit = typeInfoBits[typeInfoBits.size - 1 - i]

            // Type info bits at the left top corner. See 8.9 of JISX0510:2004 (p.46).
            val coordinates = TYPE_INFO_COORDINATES[i]
            val x1 = coordinates[0]
            val y1 = coordinates[1]
            matrix[x1, y1] = bit
            if (i < 8) {
                // Right top corner.
                val x2 = matrix.width - i - 1
                val y2 = 8
                matrix[x2, y2] = bit
            } else {
                // Left bottom corner.
                val x2 = 8
                val y2 = matrix.height - 7 + (i - 8)
                matrix[x2, y2] = bit
            }
        }
    }

    // Embed version information if need be. On success, modify the matrix and return true.
    // See 8.10 of JISX0510:2004 (p.47) for how to embed version information.
    @Throws(WriterException::class)
    fun maybeEmbedVersionInfo(version: Version, matrix: ByteMatrix) {
        if (version.versionNumber < 7) {  // Version info is necessary if version >= 7.
            return  // Don't need version info.
        }
        val versionInfoBits = BitArray()
        makeVersionInfoBits(version, versionInfoBits)
        var bitIndex = 6 * 3 - 1 // It will decrease from 17 to 0.
        for (i in 0..5) {
            for (j in 0..2) {
                // Place bits in LSB (least significant bit) to MSB order.
                val bit = versionInfoBits[bitIndex]
                bitIndex--
                // Left bottom corner.
                matrix[i, matrix.height - 11 + j] = bit
                // Right bottom corner.
                matrix[matrix.height - 11 + j, i] = bit
            }
        }
    }

    // Embed "dataBits" using "getMaskPattern". On success, modify the matrix and return true.
    // For debugging purposes, it skips masking process if "getMaskPattern" is -1.
    // See 8.7 of JISX0510:2004 (p.38) for how to embed data bits.
    @Throws(WriterException::class)
    fun embedDataBits(dataBits: BitArray, maskPattern: Int, matrix: ByteMatrix) {
        var bitIndex = 0
        var direction = -1
        // Start from the right bottom cell.
        var x = matrix.width - 1
        var y = matrix.height - 1
        while (x > 0) {
            // Skip the vertical timing pattern.
            if (x == 6) {
                x -= 1
            }
            while (y >= 0 && y < matrix.height) {
                for (i in 0..1) {
                    val xx = x - i
                    // Skip the cell if it's not empty.
                    if (!isEmpty(matrix[xx, y].toInt())) {
                        continue
                    }
                    var bit: Boolean
                    if (bitIndex < dataBits.size) {
                        bit = dataBits[bitIndex]
                        ++bitIndex
                    } else {
                        // Padding bit. If there is no bit left, we'll fill the left cells with 0, as described
                        // in 8.4.9 of JISX0510:2004 (p. 24).
                        bit = false
                    }

                    // Skip masking if mask_pattern is -1.
                    if (maskPattern != -1 && getDataMaskBit(maskPattern, xx, y)) {
                        bit = !bit
                    }
                    matrix[xx, y] = bit
                }
                y += direction
            }
            direction = -direction // Reverse the direction.
            y += direction
            x -= 2 // Move to the left.
        }
        // All bits should be consumed.
        if (bitIndex != dataBits.size) {
            throw WriterException("Not all bits consumed: " + bitIndex + '/' + dataBits.size)
        }
    }

    // Return the position of the most significant bit set (to one) in the "value". The most
    // significant bit is position 32. If there is no bit set, return 0. Examples:
    // - findMSBSet(0) => 0
    // - findMSBSet(1) => 1
    // - findMSBSet(255) => 8
    private fun findMSBSet(value: Int): Int = 32 - Integer.numberOfLeadingZeros(value)

    // Calculate BCH (Bose-Chaudhuri-Hocquenghem) code for "value" using polynomial "poly". The BCH
    // code is used for encoding type information and version information.
    // Example: Calculation of version information of 7.
    // f(x) is created from 7.
    //   - 7 = 000111 in 6 bits
    //   - f(x) = x^2 + x^1 + x^0
    // g(x) is given by the standard (p. 67)
    //   - g(x) = x^12 + x^11 + x^10 + x^9 + x^8 + x^5 + x^2 + 1
    // Multiply f(x) by x^(18 - 6)
    //   - f'(x) = f(x) * x^(18 - 6)
    //   - f'(x) = x^14 + x^13 + x^12
    // Calculate the remainder of f'(x) / g(x)
    //         x^2
    //         __________________________________________________
    //   g(x) )x^14 + x^13 + x^12
    //         x^14 + x^13 + x^12 + x^11 + x^10 + x^7 + x^4 + x^2
    //         --------------------------------------------------
    //                              x^11 + x^10 + x^7 + x^4 + x^2
    //
    // The remainder is x^11 + x^10 + x^7 + x^4 + x^2
    // Encode it in binary: 110010010100
    // The return value is 0xc94 (1100 1001 0100)
    //
    // Since all coefficients in the polynomials are 1 or 0, we can do the calculation by bit
    // operations. We don't care if coefficients are positive or negative.
    private fun calculateBCHCode(value: Int, poly: Int): Int {
        var mValue = value
        require(poly != 0) { "0 polynomial" }
        // If poly is "1 1111 0010 0101" (version info poly), msbSetInPoly is 13. We'll subtract 1
        // from 13 to make it 12.
        val msbSetInPoly = findMSBSet(poly)
        mValue = mValue shl msbSetInPoly - 1
        // Do the division business using exclusive-or operations.
        while (findMSBSet(mValue) >= msbSetInPoly) {
            mValue = mValue xor (poly shl findMSBSet(mValue) - msbSetInPoly)
        }
        // Now the "value" is the remainder (i.e. the BCH code)
        return mValue
    }

    // Make bit vector of type information. On success, store the result in "bits" and return true.
    // Encode error correction level and mask pattern. See 8.9 of
    // JISX0510:2004 (p.45) for details.
    @Throws(WriterException::class)
    fun makeTypeInfoBits(ecLevel: ErrorCorrectionLevel, maskPattern: Int, bits: BitArray) {
        if (!isValidMaskPattern(maskPattern)) {
            throw WriterException("Invalid mask pattern")
        }
        val typeInfo = ecLevel.bits shl 3 or maskPattern
        bits.appendBits(typeInfo, 5)
        val bchCode = calculateBCHCode(typeInfo, TYPE_INFO_POLY)
        bits.appendBits(bchCode, 10)
        val maskBits = BitArray()
        maskBits.appendBits(TYPE_INFO_MASK_PATTERN, 15)
        bits.xor(maskBits)
        if (bits.size != 15) {  // Just in case.
            throw WriterException("should not happen but we got: " + bits.size)
        }
    }

    // Make bit vector of version information. On success, store the result in "bits" and return true.
    // See 8.10 of JISX0510:2004 (p.45) for details.
    @Throws(WriterException::class)
    fun makeVersionInfoBits(version: Version, bits: BitArray) {
        bits.appendBits(version.versionNumber, 6)
        val bchCode = calculateBCHCode(version.versionNumber, VERSION_INFO_POLY)
        bits.appendBits(bchCode, 12)
        if (bits.size != 18) {  // Just in case.
            throw WriterException("should not happen but we got: " + bits.size)
        }
    }

    // Check if "value" is empty.
    private fun isEmpty(value: Int): Boolean {
        return value == -1
    }

    private fun embedTimingPatterns(matrix: ByteMatrix) {
        // -8 is for skipping position detection patterns (size 7), and two horizontal/vertical
        // separation patterns (size 1). Thus, 8 = 7 + 1.
        for (i in 8 until matrix.width - 8) {
            val bit = (i + 1) % 2
            // Horizontal line.
            if (isEmpty(matrix[i, 6].toInt())) {
                matrix[i, 6] = bit
            }
            // Vertical line.
            if (isEmpty(matrix[6, i].toInt())) {
                matrix[6, i] = bit
            }
        }
    }

    // Embed the lonely dark dot at left bottom corner. JISX0510:2004 (p.46)
    @Throws(WriterException::class)
    private fun embedDarkDotAtLeftBottomCorner(matrix: ByteMatrix) {
        if (matrix[8, matrix.height - 8].toInt() == 0) {
            throw WriterException()
        }
        matrix[8, matrix.height - 8] = 1
    }

    @Throws(WriterException::class)
    private fun embedHorizontalSeparationPattern(
        xStart: Int,
        yStart: Int,
        matrix: ByteMatrix
    ) {
        for (x in 0..7) {
            if (!isEmpty(matrix[xStart + x, yStart].toInt())) {
                throw WriterException()
            }
            matrix[xStart + x, yStart] = 0
        }
    }

    @Throws(WriterException::class)
    private fun embedVerticalSeparationPattern(
        xStart: Int,
        yStart: Int,
        matrix: ByteMatrix
    ) {
        for (y in 0..6) {
            if (!isEmpty(matrix[xStart, yStart + y].toInt())) {
                throw WriterException()
            }
            matrix[xStart, yStart + y] = 0
        }
    }

    private fun embedPositionAdjustmentPattern(xStart: Int, yStart: Int, matrix: ByteMatrix) {
        for (y in 0..4) {
            val patternY = POSITION_ADJUSTMENT_PATTERN[y]
            for (x in 0..4) {
                matrix[xStart + x, yStart + y] = patternY[x]
            }
        }
    }

    private fun embedPositionDetectionPattern(xStart: Int, yStart: Int, matrix: ByteMatrix) {
        for (y in 0..6) {
            val patternY = POSITION_DETECTION_PATTERN[y]
            for (x in 0..6) {
                matrix[xStart + x, yStart + y] = patternY[x]
            }
        }
    }

    // Embed position detection patterns and surrounding vertical/horizontal separators.
    @Throws(WriterException::class)
    private fun embedPositionDetectionPatternsAndSeparators(matrix: ByteMatrix) {
        // Embed three big squares at corners.
        val pdpWidth: Int = POSITION_DETECTION_PATTERN[0].size
        // Left top corner.
        embedPositionDetectionPattern(0, 0, matrix)
        // Right top corner.
        embedPositionDetectionPattern(matrix.width - pdpWidth, 0, matrix)
        // Left bottom corner.
        embedPositionDetectionPattern(0, matrix.width - pdpWidth, matrix)

        // Embed horizontal separation patterns around the squares.
        val hspWidth = 8
        // Left top corner.
        embedHorizontalSeparationPattern(0, hspWidth - 1, matrix)
        // Right top corner.
        embedHorizontalSeparationPattern(
            matrix.width - hspWidth,
            hspWidth - 1, matrix
        )
        // Left bottom corner.
        embedHorizontalSeparationPattern(0, matrix.width - hspWidth, matrix)

        // Embed vertical separation patterns around the squares.
        val vspSize = 7
        // Left top corner.
        embedVerticalSeparationPattern(vspSize, 0, matrix)
        // Right top corner.
        embedVerticalSeparationPattern(matrix.height - vspSize - 1, 0, matrix)
        // Left bottom corner.
        embedVerticalSeparationPattern(
            vspSize, matrix.height - vspSize,
            matrix
        )
    }

    // Embed position adjustment patterns if need be.
    private fun maybeEmbedPositionAdjustmentPatterns(version: Version, matrix: ByteMatrix) {
        if (version.versionNumber < 2) {  // The patterns appear if version >= 2
            return
        }
        val index = version.versionNumber - 1
        val coordinates = POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE[index]
        for (y in coordinates) {
            if (y >= 0) {
                for (x in coordinates) {
                    if (x >= 0 && isEmpty(matrix[x, y].toInt())) {
                        // If the cell is unset, we embed the position adjustment pattern here.
                        // -2 is necessary since the x/y coordinates point to the center of the pattern, not the
                        // left top corner.
                        embedPositionAdjustmentPattern(x - 2, y - 2, matrix)
                    }
                }
            }
        }
    }
}