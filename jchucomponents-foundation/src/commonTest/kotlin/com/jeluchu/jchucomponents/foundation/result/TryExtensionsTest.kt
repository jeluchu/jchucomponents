package com.jeluchu.jchucomponents.foundation.result

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TryExtensionsTest {
    @Test
    fun catchesFailures() {
        assertNull(tryOrNull { error("boom") })
        assertEquals("fallback", tryOrDefault("fallback") { error("boom") })
        assertEquals("value", tryOrDefaultNotNull("fallback") { "value" })
    }

    @Test
    fun safeLetRunsOnlyWhenAllValuesExist() {
        assertEquals(6, safeLet(1, 2, 3) { first, second, third -> first + second + third })
        assertNull(safeLet(1, null as Int?, 3) { first, second, third -> first + second + third })
    }
}
