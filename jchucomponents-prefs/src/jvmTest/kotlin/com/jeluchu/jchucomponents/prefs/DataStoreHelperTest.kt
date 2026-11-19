package com.jeluchu.jchucomponents.prefs

import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DataStoreHelperTest {
    @Test
    fun valuesAreReadImmediatelyAndPersistedInOrder() {
        val dir = Files.createTempDirectory("jchu-helper").toFile()
        val prefs = createJchuPreferences("h.preferences_pb", dir)
        val helper = createJchuPreferencesHelper(prefs)

        Thread.sleep(300)
        assertTrue(helper.getBoolean("first", true))
        helper.saveBoolean("first", false)
        helper.saveInt("count", 1)
        helper.saveInt("count", 2)
        helper.saveString("name", "Lucía")
        assertEquals(false, helper.getBoolean("first", true))
        assertEquals(2, helper.getInt("count"))

        kotlinx.coroutines.runBlocking { helper.flush() }
        val reopened = createJchuPreferencesHelper(prefs)
        Thread.sleep(300)
        assertEquals(2, reopened.getInt("count"))
        assertEquals("Lucía", reopened.getString("name"))
        assertFalse(reopened.getBoolean("first", true))

        helper.remove("name")
        assertNull(helper.getStringOrNull("name"))
        helper.clearAll()
        assertFalse(helper.contains("count"))
    }

    @Test
    fun changesTickAndNumericReadsAreTolerant() {
        val dir = Files.createTempDirectory("jchu-helper").toFile()
        val helper = createJchuPreferencesHelper(createJchuPreferences("c.preferences_pb", dir))
        Thread.sleep(300)

        val before = helper.changes.value
        helper.saveInt("n", 7)
        assertTrue(helper.changes.value > before)
        assertEquals(7L, helper.getLong("n", 0L))
        assertEquals(7, helper.getAny("n"))
    }
}
