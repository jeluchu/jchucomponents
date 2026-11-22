package com.jeluchu.jchucomponents.foundation.text

import com.jeluchu.jchucomponents.foundation.extensions.lists.search
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StringAvailabilityExtensionsTest {
    @Test
    fun availabilityCodes() {
        listOf("", "0", "na", "NA", "nts", "NFS").forEach { assertTrue(it.isBuyNotAvailability(), it) }
        assertFalse("12.99".isBuyNotAvailability())
    }

    @Test
    fun isNotZeroAndDefault() {
        assertFalse("0".isNotZero())
        assertTrue("5".isNotZero())
        assertEquals("x", "".getTranslateOrDefault("x"))
        assertEquals("a", "a".getTranslateOrDefault("x"))
    }

    @Test
    fun searchIgnoresCaseAndAccents() {
        val items = listOf("Canción", "Ñandú", "Other")
        assertEquals(listOf("Canción"), items.search("CANCION") { it })
        assertEquals(listOf("Ñandú"), items.search("nandu") { it })
        assertEquals(items, items.search("") { it })
    }
}
