/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.utils.zxing.qrcode.encoder

import com.jchucomponents.utils.zxing.EncodeHintType
import com.jchucomponents.utils.zxing.WriterException
import com.jchucomponents.utils.zxing.common.BitArray
import com.jchucomponents.utils.zxing.common.CharacterSetECI
import com.jchucomponents.utils.zxing.common.reedsolomon.GenericGF
import com.jchucomponents.utils.zxing.common.reedsolomon.ReedSolomonEncoder
import com.jchucomponents.utils.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.jchucomponents.utils.zxing.qrcode.decoder.Mode
import com.jchucomponents.utils.zxing.qrcode.decoder.Version
import com.jchucomponents.utils.zxing.qrcode.decoder.Version.Companion.getVersionForNumber
import com.jchucomponents.utils.zxing.qrcode.encoder.MaskUtil.applyMaskPenaltyRule1
import com.jchucomponents.utils.zxing.qrcode.encoder.MaskUtil.applyMaskPenaltyRule2
import com.jchucomponents.utils.zxing.qrcode.encoder.MaskUtil.applyMaskPenaltyRule3
import com.jchucomponents.utils.zxing.qrcode.encoder.MaskUtil.applyMaskPenaltyRule4
import com.jchucomponents.utils.zxing.qrcode.encoder.QRCode.Companion.isValidMaskPattern
import okhttp3.internal.and
import kotlin.math.max

object Encoder {

    private const val DEFAULT_BYTE_MODE_ENCODING = "ISO-8859-1"

    // The original table is defined in the table 5 of JISX0510:2004 (p.19).
    private val ALPHANUMERIC_TABLE = intArrayOf(
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  // 0x00-0x0f
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  // 0x10-0x1f
        36, -1, -1, -1, 37, 38, -1, -1, -1, -1, 39, 40, -1, 41, 42, 43,  // 0x20-0x2f
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 44, -1, -1, -1, -1, -1,  // 0x30-0x3f
        -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,  // 0x40-0x4f
        25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1
    )

    // The mask penalty calculation is complicated.  See Table 21 of JISX0510:2004 (p.45) for details.
    // Basically it applies four rules and summate all penalties.
    private fun calculateMaskPenalty(matrix: ByteMatrix): Int {
        return (applyMaskPenaltyRule1(matrix)
                + applyMaskPenaltyRule2(matrix)
                + applyMaskPenaltyRule3(matrix)
                + applyMaskPenaltyRule4(matrix))
    }

    /**
     * @param content text to encode
     * @param ecLevel error correction level to use
     * @return [QRCode] representing the encoded QR code
     * @throws WriterException if encoding can't succeed, because of for example invalid content
     * or configuration
     */
    @JvmOverloads
    @Throws(WriterException::class)
    fun encode(
        content: String,
        ecLevel: ErrorCorrectionLevel,
        hints: HashMap<EncodeHintType, Any?>? = null
    ): QRCode {

        // Determine what character encoding has been specified by the caller, if any
        var encoding = DEFAULT_BYTE_MODE_ENCODING
        val hasEncodingHint = hints != null && hints.containsKey(EncodeHintType.CHARACTER_SET)
        if (hasEncodingHint) {
            encoding = hints!![EncodeHintType.CHARACTER_SET].toString()
        }

        // Pick an encoding mode appropriate for the content. Note that this will not attempt to use
        // multiple modes / segments even if that were more efficient. Twould be nice.
        val mode = chooseMode(content, encoding)

        // This will store the header information, like mode and
        // length, as well as "header" segments like an ECI segment.
        val headerBits = BitArray()

        // Append ECI segment if applicable
        if (mode === Mode.BYTE && hasEncodingHint) {
            val eci = CharacterSetECI.getCharacterSetECIByName(encoding)
            if (eci != null) {
                appendECI(eci, headerBits)
            }
        }

        // Append the FNC1 mode header for GS1 formatted data if applicable
        val hasGS1FormatHint = hints != null && hints.containsKey(EncodeHintType.GS1_FORMAT)
        if (hasGS1FormatHint && java.lang.Boolean.parseBoolean(hints!![EncodeHintType.GS1_FORMAT].toString())) {
            // GS1 formatted codes are prefixed with a FNC1 in first position mode header
            appendModeInfo(Mode.FNC1_FIRST_POSITION, headerBits)
        }

        // (With ECI in place,) Write the mode marker
        appendModeInfo(mode, headerBits)

        // Collect data within the main segment, separately, to count its size if needed. Don't add it to
        // main payload yet.
        val dataBits = BitArray()
        appendBytes(content, mode, dataBits, encoding)
        val version: Version
        if (hints != null && hints.containsKey(EncodeHintType.QR_VERSION)) {
            val versionNumber = hints[EncodeHintType.QR_VERSION].toString().toInt()
            version = getVersionForNumber(versionNumber)
            val bitsNeeded = calculateBitsNeeded(mode, headerBits, dataBits, version)
            if (!willFit(bitsNeeded, version, ecLevel)) {
                throw WriterException("Data too big for requested version")
            }
        } else {
            version = recommendVersion(ecLevel, mode, headerBits, dataBits)
        }
        val headerAndDataBits = BitArray()
        headerAndDataBits.appendBitArray(headerBits)
        // Find "length" of main segment and write it
        val numLetters = if (mode === Mode.BYTE) dataBits.sizeInBytes else content.length
        appendLengthInfo(numLetters, version, mode, headerAndDataBits)
        // Put data together into the overall payload
        headerAndDataBits.appendBitArray(dataBits)
        val ecBlocks = version.getECBlocksForLevel(ecLevel)
        val numDataBytes = version.totalCodewords - ecBlocks.totalECCodewords

        // Terminate the bits properly.
        terminateBits(numDataBytes, headerAndDataBits)

        // Interleave data bits with error correction code.
        val finalBits = interleaveWithECBytes(
            headerAndDataBits,
            version.totalCodewords,
            numDataBytes,
            ecBlocks.numBlocks
        )
        val qrCode = QRCode()
        qrCode.eCLevel = ecLevel
        qrCode.mode = mode
        qrCode.version = version

        //  Choose the mask pattern and set to "qrCode".
        val dimension = version.dimensionForVersion
        val matrix = ByteMatrix(dimension, dimension)

        // Enable manual selection of the pattern to be used via hint
        var maskPattern = -1
        if (hints != null && hints.containsKey(EncodeHintType.QR_MASK_PATTERN)) {
            val hintMaskPattern = hints[EncodeHintType.QR_MASK_PATTERN].toString().toInt()
            maskPattern = if (isValidMaskPattern(hintMaskPattern)) hintMaskPattern else -1
        }
        if (maskPattern == -1) {
            maskPattern = chooseMaskPattern(finalBits, ecLevel, version, matrix)
        }
        qrCode.maskPattern = maskPattern

        // Build the matrix and set it to "qrCode".
        MatrixUtil.buildMatrix(finalBits, ecLevel, version, maskPattern, matrix)
        qrCode.matrix = matrix
        return qrCode
    }

    /**
     * Decides the smallest version of QR code that will contain all of the provided data.
     *
     * @throws WriterException if the data cannot fit in any version
     */
    @Throws(WriterException::class)
    private fun recommendVersion(
        ecLevel: ErrorCorrectionLevel,
        mode: Mode,
        headerBits: BitArray,
        dataBits: BitArray
    ): Version {
        // Hard part: need to know version to know how many bits length takes. But need to know how many
        // bits it takes to know version. First we take a guess at version by assuming version will be
        // the minimum, 1:
        val provisionalBitsNeeded =
            calculateBitsNeeded(mode, headerBits, dataBits, getVersionForNumber(1))
        val provisionalVersion = chooseVersion(provisionalBitsNeeded, ecLevel)

        // Use that guess to calculate the right version. I am still not sure this works in 100% of cases.
        val bitsNeeded = calculateBitsNeeded(mode, headerBits, dataBits, provisionalVersion)
        return chooseVersion(bitsNeeded, ecLevel)
    }

    private fun calculateBitsNeeded(
        mode: Mode,
        headerBits: BitArray,
        dataBits: BitArray,
        version: Version
    ): Int {
        return headerBits.size + mode.getCharacterCountBits(version) + dataBits.size
    }

    /**
     * @return the code point of the table used in alphanumeric mode or
     * -1 if there is no corresponding code in the table.
     */
    private fun getAlphanumericCode(code: Int): Int {
        return if (code < ALPHANUMERIC_TABLE.size) {
            ALPHANUMERIC_TABLE[code]
        } else -1
    }

    /**
     * Choose the best mode by examining the content. Note that 'encoding' is used as a hint;
     * if it is Shift_JIS, and the input is only double-byte Kanji, then we return [Mode.KANJI].
     */
    private fun chooseMode(content: String, encoding: String?): Mode {
        if ("Shift_JIS" == encoding && isOnlyDoubleByteKanji(content)) {
            // Choose Kanji mode if all input are double-byte characters
            return Mode.KANJI
        }
        var hasNumeric = false
        var hasAlphanumeric = false
        for (element in content) {
            when {
                element in '0'..'9' -> hasNumeric = true
                getAlphanumericCode(element.code) != -1 -> hasAlphanumeric = true
                else -> return Mode.BYTE
            }
        }
        if (hasAlphanumeric) {
            return Mode.ALPHANUMERIC
        }
        return if (hasNumeric) {
            Mode.NUMERIC
        } else Mode.BYTE
    }

    private fun isOnlyDoubleByteKanji(content: String): Boolean {
        val bytes: ByteArray = runCatching {
            content.toByteArray(charset("Shift_JIS"))
        }.getOrElse { return false }
        val length = bytes.size
        if (length % 2 != 0) {
            return false
        }
        var i = 0
        while (i < length) {
            val byte1: Int = bytes[i] and 0xFF
            if ((byte1 < 0x81 || byte1 > 0x9F) && (byte1 < 0xE0 || byte1 > 0xEB)) {
                return false
            }
            i += 2
        }
        return true
    }

    @Throws(WriterException::class)
    private fun chooseMaskPattern(
        bits: BitArray,
        ecLevel: ErrorCorrectionLevel,
        version: Version,
        matrix: ByteMatrix
    ): Int {
        var minPenalty = Int.MAX_VALUE // Lower penalty is better.
        var bestMaskPattern = -1
        // We try all mask patterns to choose the best one.
        for (maskPattern in 0 until QRCode.NUM_MASK_PATTERNS) {
            MatrixUtil.buildMatrix(bits, ecLevel, version, maskPattern, matrix)
            val penalty = calculateMaskPenalty(matrix)
            if (penalty < minPenalty) {
                minPenalty = penalty
                bestMaskPattern = maskPattern
            }
        }
        return bestMaskPattern
    }

    @Throws(WriterException::class)
    private fun chooseVersion(numInputBits: Int, ecLevel: ErrorCorrectionLevel): Version {
        for (versionNum in 1..40) {
            val version = getVersionForNumber(versionNum)
            if (willFit(numInputBits, version, ecLevel)) {
                return version
            }
        }
        throw WriterException("Data too big")
    }

    /**
     * @return true if the number of input bits will fit in a code with the specified version and
     * error correction level.
     */
    private fun willFit(
        numInputBits: Int,
        version: Version,
        ecLevel: ErrorCorrectionLevel
    ): Boolean {
        // In the following comments, we use numbers of Version 7-H.
        // numBytes = 196
        val numBytes = version.totalCodewords
        // getNumECBytes = 130
        val ecBlocks = version.getECBlocksForLevel(ecLevel)
        val numEcBytes = ecBlocks.totalECCodewords
        // getNumDataBytes = 196 - 130 = 66
        val numDataBytes = numBytes - numEcBytes
        val totalInputBytes = (numInputBits + 7) / 8
        return numDataBytes >= totalInputBytes
    }

    /**
     * Terminate bits as described in 8.4.8 and 8.4.9 of JISX0510:2004 (p.24).
     */
    @Throws(WriterException::class)
    fun terminateBits(numDataBytes: Int, bits: BitArray) {
        val capacity = numDataBytes * 8
        if (bits.size > capacity) {
            throw WriterException(
                "data bits cannot fit in the QR Code" + bits.size + " > " +
                        capacity
            )
        }
        run {
            var i = 0
            while (i < 4 && bits.size < capacity) {
                bits.appendBit(false)
                ++i
            }
        }
        // Append termination bits. See 8.4.8 of JISX0510:2004 (p.24) for details.
        // If the last byte isn't 8-bit aligned, we'll add padding bits.
        val numBitsInLastByte = bits.size and 0x07
        if (numBitsInLastByte > 0) {
            for (i in numBitsInLastByte..7) {
                bits.appendBit(false)
            }
        }
        // If we have more space, we'll fill the space with padding patterns defined in 8.4.9 (p.24).
        val numPaddingBytes = numDataBytes - bits.sizeInBytes
        for (i in 0 until numPaddingBytes) {
            bits.appendBits(if (i and 0x01 == 0) 0xEC else 0x11, 8)
        }
        if (bits.size != capacity) {
            throw WriterException("Bits size does not equal capacity")
        }
    }

    /**
     * Get number of data bytes and number of error correction bytes for block id "blockID". Store
     * the result in "numDataBytesInBlock", and "numECBytesInBlock". See table 12 in 8.5.1 of
     * JISX0510:2004 (p.30)
     */
    @Throws(WriterException::class)
    fun getNumDataBytesAndNumECBytesForBlockID(
        numTotalBytes: Int,
        numDataBytes: Int,
        numRSBlocks: Int,
        blockID: Int,
        numDataBytesInBlock: IntArray,
        numECBytesInBlock: IntArray
    ) {
        if (blockID >= numRSBlocks) {
            throw WriterException("Block ID too large")
        }
        // numRsBlocksInGroup2 = 196 % 5 = 1
        val numRsBlocksInGroup2 = numTotalBytes % numRSBlocks
        // numRsBlocksInGroup1 = 5 - 1 = 4
        val numRsBlocksInGroup1 = numRSBlocks - numRsBlocksInGroup2
        // numTotalBytesInGroup1 = 196 / 5 = 39
        val numTotalBytesInGroup1 = numTotalBytes / numRSBlocks
        // numTotalBytesInGroup2 = 39 + 1 = 40
        val numTotalBytesInGroup2 = numTotalBytesInGroup1 + 1
        // numDataBytesInGroup1 = 66 / 5 = 13
        val numDataBytesInGroup1 = numDataBytes / numRSBlocks
        // numDataBytesInGroup2 = 13 + 1 = 14
        val numDataBytesInGroup2 = numDataBytesInGroup1 + 1
        // numEcBytesInGroup1 = 39 - 13 = 26
        val numEcBytesInGroup1 = numTotalBytesInGroup1 - numDataBytesInGroup1
        // numEcBytesInGroup2 = 40 - 14 = 26
        val numEcBytesInGroup2 = numTotalBytesInGroup2 - numDataBytesInGroup2
        // Sanity checks.
        // 26 = 26
        if (numEcBytesInGroup1 != numEcBytesInGroup2) {
            throw WriterException("EC bytes mismatch")
        }
        // 5 = 4 + 1.
        if (numRSBlocks != numRsBlocksInGroup1 + numRsBlocksInGroup2) {
            throw WriterException("RS blocks mismatch")
        }
        // 196 = (13 + 26) * 4 + (14 + 26) * 1
        if (numTotalBytes !=
            (numDataBytesInGroup1 + numEcBytesInGroup1) *
            numRsBlocksInGroup1 +
            (numDataBytesInGroup2 + numEcBytesInGroup2) *
            numRsBlocksInGroup2
        ) {
            throw WriterException("Total bytes mismatch")
        }
        if (blockID < numRsBlocksInGroup1) {
            numDataBytesInBlock[0] = numDataBytesInGroup1
            numECBytesInBlock[0] = numEcBytesInGroup1
        } else {
            numDataBytesInBlock[0] = numDataBytesInGroup2
            numECBytesInBlock[0] = numEcBytesInGroup2
        }
    }

    /**
     * Interleave "bits" with corresponding error correction bytes. On success, store the result in
     * "result". The interleave rule is complicated. See 8.6 of JISX0510:2004 (p.37) for details.
     */
    @Throws(WriterException::class)
    fun interleaveWithECBytes(
        bits: BitArray,
        numTotalBytes: Int,
        numDataBytes: Int,
        numRSBlocks: Int
    ): BitArray {

        // "bits" must have "getNumDataBytes" bytes of data.
        if (bits.sizeInBytes != numDataBytes) {
            throw WriterException("Number of bits and data bytes does not match")
        }

        // Step 1.  Divide data bytes into blocks and generate error correction bytes for them. We'll
        // store the divided data bytes blocks and error correction bytes blocks into "blocks".
        var dataBytesOffset = 0
        var maxNumDataBytes = 0
        var maxNumEcBytes = 0

        // Since, we know the number of reedsolmon blocks, we can initialize the vector with the number.
        val blocks: MutableCollection<BlockPair> = ArrayList(numRSBlocks)
        for (i in 0 until numRSBlocks) {
            val numDataBytesInBlock = IntArray(1)
            val numEcBytesInBlock = IntArray(1)
            getNumDataBytesAndNumECBytesForBlockID(
                numTotalBytes, numDataBytes, numRSBlocks, i,
                numDataBytesInBlock, numEcBytesInBlock
            )
            val size = numDataBytesInBlock[0]
            val dataBytes = ByteArray(size)
            bits.toBytes(8 * dataBytesOffset, dataBytes, 0, size)
            val ecBytes = generateECBytes(dataBytes, numEcBytesInBlock[0])
            blocks.add(BlockPair(dataBytes, ecBytes))
            maxNumDataBytes = max(maxNumDataBytes, size)
            maxNumEcBytes = max(maxNumEcBytes, ecBytes.size)
            dataBytesOffset += numDataBytesInBlock[0]
        }
        if (numDataBytes != dataBytesOffset) {
            throw WriterException("Data bytes does not match offset")
        }
        val result = BitArray()

        // First, place data blocks.
        for (i in 0 until maxNumDataBytes) {
            for (block in blocks) {
                val dataBytes = block.dataBytes
                if (i < dataBytes.size) {
                    result.appendBits(dataBytes[i].toInt(), 8)
                }
            }
        }
        // Then, place error correction blocks.
        for (i in 0 until maxNumEcBytes) {
            for (block in blocks) {
                val ecBytes = block.errorCorrectionBytes
                if (i < ecBytes.size) {
                    result.appendBits(ecBytes[i].toInt(), 8)
                }
            }
        }
        if (numTotalBytes != result.sizeInBytes) {  // Should be same.
            throw WriterException(
                "Interleaving error: " + numTotalBytes + " and " +
                        result.sizeInBytes + " differ."
            )
        }
        return result
    }

    private fun generateECBytes(dataBytes: ByteArray, numEcBytesInBlock: Int): ByteArray {
        val numDataBytes = dataBytes.size
        val toEncode = IntArray(numDataBytes + numEcBytesInBlock)
        for (i in 0 until numDataBytes) {
            toEncode[i] = dataBytes[i] and 0xFF
        }
        ReedSolomonEncoder(GenericGF.QR_CODE_FIELD_256).encode(toEncode, numEcBytesInBlock)
        val ecBytes = ByteArray(numEcBytesInBlock)
        for (i in 0 until numEcBytesInBlock) {
            ecBytes[i] = toEncode[numDataBytes + i].toByte()
        }
        return ecBytes
    }

    /**
     * Append mode info. On success, store the result in "bits".
     */
    private fun appendModeInfo(mode: Mode, bits: BitArray) {
        bits.appendBits(mode.bits, 4)
    }

    /**
     * Append length info. On success, store the result in "bits".
     */
    @Throws(WriterException::class)
    fun appendLengthInfo(numLetters: Int, version: Version?, mode: Mode, bits: BitArray) {
        val numBits = mode.getCharacterCountBits(version!!)
        if (numLetters >= 1 shl numBits) {
            throw WriterException(numLetters.toString() + " is bigger than " + ((1 shl numBits) - 1))
        }
        bits.appendBits(numLetters, numBits)
    }

    /**
     * Append "bytes" in "mode" mode (encoding) into "bits". On success, store the result in "bits".
     */
    @Throws(WriterException::class)
    fun appendBytes(
        content: String,
        mode: Mode,
        bits: BitArray,
        encoding: String?
    ) {
        when (mode) {
            Mode.NUMERIC -> appendNumericBytes(content, bits)
            Mode.ALPHANUMERIC -> appendAlphanumericBytes(content, bits)
            Mode.BYTE -> append8BitBytes(content, bits, encoding)
            Mode.KANJI -> appendKanjiBytes(content, bits)
            else -> throw WriterException("Invalid mode: $mode")
        }
    }

    private fun appendNumericBytes(content: CharSequence, bits: BitArray) {
        val length = content.length
        var i = 0
        while (i < length) {
            val num1 = content[i] - '0'
            when {
                i + 2 < length -> {
                    // Encode three numeric letters in ten bits.
                    val num2 = content[i + 1] - '0'
                    val num3 = content[i + 2] - '0'
                    bits.appendBits(num1 * 100 + num2 * 10 + num3, 10)
                    i += 3
                }
                i + 1 < length -> {
                    // Encode two numeric letters in seven bits.
                    val num2 = content[i + 1] - '0'
                    bits.appendBits(num1 * 10 + num2, 7)
                    i += 2
                }
                else -> {
                    // Encode one numeric letter in four bits.
                    bits.appendBits(num1, 4)
                    i++
                }
            }
        }
    }

    @Throws(WriterException::class)
    fun appendAlphanumericBytes(content: CharSequence, bits: BitArray) {
        val length = content.length
        var i = 0
        while (i < length) {
            val code1 = getAlphanumericCode(
                content[i].code
            )
            if (code1 == -1) {
                throw WriterException()
            }
            if (i + 1 < length) {
                val code2 = getAlphanumericCode(
                    content[i + 1].code
                )
                if (code2 == -1) {
                    throw WriterException()
                }
                // Encode two alphanumeric letters in 11 bits.
                bits.appendBits(code1 * 45 + code2, 11)
                i += 2
            } else {
                // Encode one alphanumeric letter in six bits.
                bits.appendBits(code1, 6)
                i++
            }
        }
    }

    @Throws(WriterException::class)
    fun append8BitBytes(content: String, bits: BitArray, encoding: String?) {
        val bytes: ByteArray = runCatching {
            content.toByteArray(charset(encoding!!))
        }.getOrElse { throw WriterException(it) }
        for (b in bytes) {
            bits.appendBits(b.toInt(), 8)
        }
    }

    @Throws(WriterException::class)
    fun appendKanjiBytes(content: String, bits: BitArray) {
        val bytes: ByteArray = runCatching {
            content.toByteArray(charset("Shift_JIS"))
        }.getOrElse { throw WriterException(it) }
        if (bytes.size % 2 != 0) {
            throw WriterException("Kanji byte size not even")
        }
        val maxI = bytes.size - 1 // bytes.length must be even
        var i = 0
        while (i < maxI) {
            val byte1: Int = bytes[i] and 0xFF
            val byte2: Int = bytes[i + 1] and 0xFF
            val code = byte1 shl 8 or byte2
            var subtracted = -1
            if (code in 0x8140..0x9ffc) {
                subtracted = code - 0x8140
            } else if (code in 0xe040..0xebbf) {
                subtracted = code - 0xc140
            }
            if (subtracted == -1) {
                throw WriterException("Invalid byte sequence")
            }
            val encoded = (subtracted shr 8) * 0xc0 + (subtracted and 0xff)
            bits.appendBits(encoded, 13)
            i += 2
        }
    }

    private fun appendECI(eci: CharacterSetECI, bits: BitArray) {
        bits.appendBits(Mode.ECI.bits, 4)
        // This is correct for values up to 127, which is all we need now.
        bits.appendBits(eci.value, 8)
    }
}