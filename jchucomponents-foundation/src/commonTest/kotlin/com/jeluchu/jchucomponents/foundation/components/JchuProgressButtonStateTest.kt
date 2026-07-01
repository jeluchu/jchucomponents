package com.jeluchu.jchucomponents.foundation.components

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JchuProgressButtonStateTest {

    @Test
    fun defaultsToEnabledAndNotLoading() {
        val state = JchuProgressButtonState(title = "Continue")

        assertEquals("Continue", state.title)
        assertFalse(state.isLoading)
        assertTrue(state.isEnabled)
    }
}
