package com.jeluchu.jchucomponents.foundation.text

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StringExtensionsTest {
    @Test
    fun formatsStringInGroups() {
        assertEquals("1234-5678-90", "1234567890".formatInGroups())
        assertEquals("12 34 56", "123456".formatInGroups(groupSize = 2, separator = " "))
    }

    @Test
    fun preservesEmptyString() {
        assertEquals("", "".formatInGroups())
    }

    @Test
    fun rejectsInvalidGroupSize() {
        assertFailsWith<IllegalArgumentException> {
            "1234".formatInGroups(groupSize = 0)
        }
    }
}
