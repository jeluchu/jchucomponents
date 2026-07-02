package com.jeluchu.jchucomponents.foundation

import kotlin.test.Test
import kotlin.test.assertEquals

class JchuComponentsTest {

    @Test
    fun exposesCurrentLibraryVersion() {
        assertEquals("3.0.0-alpha03", JchuComponents.VERSION)
    }
}
