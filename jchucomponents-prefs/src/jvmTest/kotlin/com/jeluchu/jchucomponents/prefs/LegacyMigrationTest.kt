package com.jeluchu.jchucomponents.prefs

import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LegacyMigrationTest {
    private fun newPrefs() = createJchuPreferences("m-${System.nanoTime()}.preferences_pb", Files.createTempDirectory("jchu-mig").toFile())

    @Test
    fun copiesEverySupportedTypeOnce() =
        runBlocking {
            val prefs = newPrefs()
            val values = mapOf<String, Any>("b" to true, "i" to 3, "l" to 4L, "f" to 1.5f, "d" to 2.5, "s" to "hi")

            assertTrue(copyLegacyValues(prefs, "marker", values))

            assertEquals(true, prefs.get(JchuPreferenceKeys.boolean("b"), false))
            assertEquals(3, prefs.get(JchuPreferenceKeys.int("i"), 0))
            assertEquals(4L, prefs.get(JchuPreferenceKeys.long("l"), 0L))
            assertEquals(1.5f, prefs.get(JchuPreferenceKeys.float("f"), 0f))
            assertEquals(2.5, prefs.get(JchuPreferenceKeys.double("d"), 0.0))
            assertEquals("hi", prefs.get(JchuPreferenceKeys.string("s"), ""))
        }

    @Test
    fun theSecondRunIsSkippedByTheMarker() =
        runBlocking {
            val prefs = newPrefs()
            assertTrue(copyLegacyValues(prefs, "marker", mapOf("s" to "old")))
            prefs.set(JchuPreferenceKeys.string("s"), "changed by the user")

            assertFalse(copyLegacyValues(prefs, "marker", mapOf("s" to "old")))

            assertEquals("changed by the user", prefs.get(JchuPreferenceKeys.string("s"), ""))
        }

    @Test
    fun existingValuesAreNeverOverwritten() =
        runBlocking {
            val prefs = newPrefs()
            prefs.set(JchuPreferenceKeys.string("theme"), "dark")

            copyLegacyValues(prefs, "marker", mapOf("theme" to "light", "other" to "x"))

            assertEquals("dark", prefs.get(JchuPreferenceKeys.string("theme"), ""))
            assertEquals("x", prefs.get(JchuPreferenceKeys.string("other"), ""))
        }

    @Test
    fun anEmptyLegacyStoreStillSetsTheMarker() =
        runBlocking {
            val prefs = newPrefs()
            assertTrue(copyLegacyValues(prefs, "marker", emptyMap()))
            assertFalse(copyLegacyValues(prefs, "marker", mapOf("late" to "value")))
        }
}
