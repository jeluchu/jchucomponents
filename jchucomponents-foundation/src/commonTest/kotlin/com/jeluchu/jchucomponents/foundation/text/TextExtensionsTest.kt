package com.jeluchu.jchucomponents.foundation.text

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TextExtensionsTest {
    @Test
    fun getLastBitFromUrlDropsQueryAndFragment() {
        assertEquals("photo.png", "https://example.com/a/b/photo.png?size=2#top".getLastBitFromUrl())
        assertEquals("photo.png", "https://example.com/photo.png".getLastBitFromUrl())
    }

    @Test
    fun extractYTIdReadsEveryKnownUrlShape() {
        assertEquals("dQw4w9WgXcQ", "https://youtu.be/dQw4w9WgXcQ".extractYTId())
        assertEquals("dQw4w9WgXcQ", "https://www.youtube.com/embed/dQw4w9WgXcQ".extractYTId())
        assertEquals("dQw4w9WgXcQ", "https://www.youtube.com/watch?v=dQw4w9WgXcQ".extractYTId())
        assertEquals("dQw4w9WgXcQ", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&t=10".extractYTId())
        assertNull("not a url".extractYTId())
    }

    @Test
    fun isIpFindsAnIpv4Address() {
        assertTrue("192.168.1.10".isIp)
        assertTrue("host 10.0.0.1 down".isIp)
        assertFalse("example.com".isIp)
    }

    @Test
    fun cleaningHelpers() {
        assertEquals("12", "12.50".deleteDouble())
        assertEquals("fallback", (null as String?).safeValue("fallback"))
        assertEquals("ab", "a\nb\r".removeCharactersWeirds())
        assertEquals("", (null as String?).removeCharactersWeirds())
        assertTrue("null".isEmptyString())
        assertTrue("".isEmptyString())
        assertFalse("x".isEmptyString())
    }

    @Test
    fun currencySymbols() {
        assertEquals("$", "USD".getCurrencyString())
        assertEquals("kr", "DKK".getCurrencyString())
        assertEquals("€", "XXX".getCurrencyString())
    }
}
