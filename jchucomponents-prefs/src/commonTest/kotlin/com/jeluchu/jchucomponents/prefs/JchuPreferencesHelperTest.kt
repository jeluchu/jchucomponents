package com.jeluchu.jchucomponents.prefs

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class JchuPreferencesHelperTest {
    @Test
    fun getPreferenceDispatchesOnTheDefaultType() {
        val helper = fakeHelper()
        helper.savePreference("n", 4)
        helper.savePreference("flag", true)
        assertEquals(4, helper.getPreference("n", 0))
        assertEquals(true, helper.getPreference("flag", false))
        assertEquals("x", helper.getPreference("s", "x"))
    }

    @Test
    fun withoutKoinTheHelperFallsBackToMemory() {
        val helper = jchuPreferencesHelper()
        helper.saveBoolean("preview", true)
        assertTrue(helper.getBoolean("preview", false))
        helper.remove("preview")
        assertNull(helper.getStringOrNull("preview"))
    }

    private fun fakeHelper(): JchuPreferencesHelper = object : JchuPreferencesHelper {
        val map = mutableMapOf<String, Any>()
        override val changes = kotlinx.coroutines.flow.MutableStateFlow(0)
        override fun getAny(key: String): Any? = map[key]
        override fun saveInt(key: String, value: Int) { map[key] = value }
        override fun saveLong(key: String, value: Long) { map[key] = value }
        override fun saveFloat(key: String, value: Float) { map[key] = value }
        override fun saveDouble(key: String, value: Double) { map[key] = value }
        override fun saveString(key: String, value: String) { map[key] = value }
        override fun saveBoolean(key: String, value: Boolean) { map[key] = value }
        override fun getInt(key: String, default: Int) = map[key] as Int? ?: default
        override fun getLong(key: String, default: Long) = map[key] as Long? ?: default
        override fun getFloat(key: String, default: Float) = map[key] as Float? ?: default
        override fun getDouble(key: String, default: Double) = map[key] as Double? ?: default
        override fun getString(key: String, default: String) = map[key] as String? ?: default
        override fun getBoolean(key: String, default: Boolean) = map[key] as Boolean? ?: default
        override fun getStringOrNull(key: String) = map[key] as String?
        override fun contains(key: String) = key in map
        override fun remove(key: String) { map.remove(key) }
        override fun clearAll() = map.clear()
        override suspend fun flush() = Unit
    }
}
