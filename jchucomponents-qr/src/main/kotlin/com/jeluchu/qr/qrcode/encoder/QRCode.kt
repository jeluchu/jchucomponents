/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.qr.qrcode.encoder

import com.jeluchu.qr.qrcode.decoder.ErrorCorrectionLevel
import com.jeluchu.qr.qrcode.decoder.Mode
import com.jeluchu.qr.qrcode.decoder.Version

class QRCode {
    var mode: Mode? = null
    var eCLevel: ErrorCorrectionLevel? = null
    var version: Version? = null
    var maskPattern: Int = -1
    var matrix: ByteMatrix? = null
    override fun toString(): String {
        val result = StringBuilder(200)
        result.append("<<\n")
        result.append(" mode: ")
        result.append(mode)
        result.append("\n ecLevel: ")
        result.append(eCLevel)
        result.append("\n version: ")
        result.append(version)
        result.append("\n maskPattern: ")
        result.append(maskPattern)
        if (matrix == null) result.append("\n matrix: null\n")
        else {
            result.append("\n matrix:\n")
            result.append(matrix)
        }
        result.append(">>\n")
        return result.toString()
    }

    companion object {
        const val NUM_MASK_PATTERNS = 8

        // Check if "mask_pattern" is valid.
        @JvmStatic
        fun isValidMaskPattern(maskPattern: Int): Boolean = maskPattern in 0 until NUM_MASK_PATTERNS
    }

}