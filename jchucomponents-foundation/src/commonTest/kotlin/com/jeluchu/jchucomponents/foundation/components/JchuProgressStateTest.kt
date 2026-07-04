package com.jeluchu.jchucomponents.foundation.components

import kotlin.test.Test
import kotlin.test.assertEquals

class JchuProgressStateTest {
    @Test
    fun normalizesProgressToUnitRange() {
        assertEquals(0.4, state(value = 40.0, maxValue = 100.0).fraction)
        assertEquals(1.0, state(value = 120.0, maxValue = 100.0).fraction)
        assertEquals(0.0, state(value = -20.0, maxValue = 100.0).fraction)
        assertEquals(0.0, state(value = 10.0, maxValue = 0.0).fraction)
    }

    private fun state(
        value: Double,
        maxValue: Double,
    ) = JchuProgressState(
        title = "Progress",
        value = value,
        maxValue = maxValue,
        isEnabled = true,
        isIndeterminate = false,
    )
}
