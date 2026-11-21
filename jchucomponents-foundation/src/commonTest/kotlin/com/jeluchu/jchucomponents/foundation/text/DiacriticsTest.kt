package com.jeluchu.jchucomponents.foundation.text

import kotlin.test.Test
import kotlin.test.assertEquals

class DiacriticsTest {
    @Test
    fun accentsAreRemovedAndOtherCharactersKept() {
        assertEquals("Cancion nandu", "Canción ñandú".removeDiacritics())
        assertEquals("aeiou AEIOU", "áéíóú ÁÉÍÓÚ".removeDiacritics())
        assertEquals("plain text 123", "plain text 123".removeDiacritics())
        assertEquals("", "".removeDiacritics())
    }
}
