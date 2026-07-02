package com.jeluchu.jchucomponents.prefs

import kotlin.test.Test
import kotlin.test.assertEquals

class JchuPreferenceKeysTest {
    @Test
    fun keysKeepTheirNames() {
        assertEquals("user_name", JchuPreferenceKeys.string("user_name").name)
        assertEquals("enabled", JchuPreferenceKeys.boolean("enabled").name)
    }
}
