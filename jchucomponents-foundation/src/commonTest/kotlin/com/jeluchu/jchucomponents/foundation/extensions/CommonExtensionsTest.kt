package com.jeluchu.jchucomponents.foundation.extensions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CommonExtensionsTest {
    @Test
    fun nullableBooleansUseSafeDefaults() {
        assertFalse(null.orFalse())
        assertTrue(null.orFalse(defaultValue = true))
        assertTrue(true.isTrue())
        assertFalse(null.isTrue())
    }

    @Test
    fun nullableNumbersUseZeroOrProvidedDefaults() {
        assertEquals(0, (null as Int?).orZero())
        assertEquals(4L, (null as Long?).orZero(defaultValue = 4L))
        assertEquals(2.5f, 2.5f.orZero())
        assertEquals(0.0, (null as Double?).orZero())
    }

    @Test
    fun nullableStringsUseEmptyOrProvidedDefaults() {
        assertEquals("", null.orEmpty())
        assertEquals("fallback", null.orEmpty(defaultValue = "fallback"))
        assertEquals("value", "value".orEmpty())
    }

    @Test
    fun nullChecksUsePortableExtensions() {
        assertTrue((null as String?).isNull())
        assertFalse("value".isNull())
    }
}
