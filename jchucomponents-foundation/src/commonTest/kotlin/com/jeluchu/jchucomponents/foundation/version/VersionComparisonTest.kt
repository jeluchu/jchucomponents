package com.jeluchu.jchucomponents.foundation.version

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class VersionComparisonTest {
    @Test
    fun comparesNumericComponents() {
        assertTrue(compareVersions("1.10.0", "1.9.9") > 0)
        assertTrue("2.0".compareVersionTo("10.0") < 0)
        assertEquals(0, compareVersions("1.0", "1.0.0"))
    }

    @Test
    fun acceptsVersionPrefixAndIgnoresBuildMetadata() {
        assertEquals(0, compareVersions("v1.2.3+android", "1.2.3+ios"))
        assertTrue(compareVersions("V2.0.0", "1.9.9") > 0)
    }

    @Test
    fun comparesPrereleaseVersions() {
        assertTrue(compareVersions("3.0.0-alpha05", "3.0.0-alpha04") > 0)
        assertTrue(compareVersions("1.0.0-alpha.2", "1.0.0-alpha.10") < 0)
        assertTrue(compareVersions("1.0.0", "1.0.0-rc.1") > 0)
    }

    @Test
    fun rejectsInvalidVersions() {
        assertFailsWith<IllegalArgumentException> { compareVersions("", "1.0.0") }
        assertFailsWith<IllegalArgumentException> { compareVersions("1.x", "1.0") }
    }
}
