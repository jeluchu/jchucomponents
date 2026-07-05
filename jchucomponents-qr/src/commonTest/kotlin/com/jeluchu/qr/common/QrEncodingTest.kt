package com.jeluchu.qr.common

import kotlin.test.Test
import kotlin.test.assertEquals

class QrEncodingTest {
    @Test
    fun roundTripsUnicodeTextUsingPlatformEncoding() {
        val value = "JchuComponents · QR · 你好"
        val encoding = qrDefaultEncoding()

        assertEquals(
            value,
            qrDecodeBytes(qrEncodeText(value, encoding), encoding)
        )
    }
}
