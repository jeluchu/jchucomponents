package com.jeluchu.jchucomponents.foundation.text

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ValidationExtensionsTest {
    @Test
    fun validatesStringContent() {
        assertTrue("abc123".containsLetters)
        assertTrue("abc123".containsNumbers)
        assertEquals("123", "a1-b2_c3".onlyDigits())
        assertTrue("12.5".isNumber())
        assertFalse("hello".isNumber())
    }

    @Test
    fun transformsStrings() {
        assertEquals("ell", "hello".removeFirstLastChar())
        assertEquals("1234 5678 ", "12345678".addSpaceAfterEvery4Chars())
        assertEquals("Hello World", "hello world".capitalizeWords())
        assertEquals("hello", "hello-world".remove("-world"))
        assertEquals(3, "one two  three".wordCount())
    }

    @Test
    fun handlesNullableStrings() {
        assertNull("".returnNullIfEmpty())
        assertEquals("https://example.com", "example.com".toHttpsUrl())
        assertEquals("http://example.com", "http://example.com".toHttpsUrl())
    }
}
