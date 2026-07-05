package com.jeluchu.qr.common

internal expect fun qrDefaultEncoding(): String

internal expect fun qrDecodeBytes(
    bytes: ByteArray,
    encoding: String
): String

internal expect fun qrEncodeText(
    text: String,
    encoding: String
): ByteArray
