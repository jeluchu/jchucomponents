package com.jeluchu.qr.common

import java.nio.charset.Charset

internal actual fun qrDefaultEncoding(): String = Charset.defaultCharset().name()

internal actual fun qrDecodeBytes(bytes: ByteArray, encoding: String): String =
    String(bytes, Charset.forName(encoding))

internal actual fun qrEncodeText(text: String, encoding: String): ByteArray =
    text.toByteArray(Charset.forName(encoding))
