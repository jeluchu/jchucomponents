package com.jeluchu.jchucomponents.foundation.crypto

import kotlin.test.Test
import kotlin.test.assertEquals

class CaesarCipherTest {
    @Test
    fun encryptsAndDecryptsText() {
        val encrypted = CaesarCipher.encrypt("Hello, Zebra!", key = 8)

        assertEquals("Pmttw, Hmjzi!", encrypted)
        assertEquals("Hello, Zebra!", CaesarCipher.decrypt(encrypted, key = 8))
    }
}
