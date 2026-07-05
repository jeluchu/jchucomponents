package com.jeluchu.qr.common

internal actual fun qrDefaultEncoding(): String = "UTF8"

internal actual fun qrDecodeBytes(
    bytes: ByteArray,
    encoding: String
): String = bytes.decodeToString()

internal actual fun qrEncodeText(
    text: String,
    encoding: String
): ByteArray = text.encodeToByteArray()
