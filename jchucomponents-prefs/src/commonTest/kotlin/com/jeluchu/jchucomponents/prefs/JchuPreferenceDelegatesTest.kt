package com.jeluchu.jchucomponents.prefs

import kotlin.test.Test
import kotlin.test.assertEquals

class JchuPreferenceDelegatesTest {
    private enum class Sort { Name, Date }

    private class Settings(helper: JchuPreferencesHelper) {
        var first by booleanPreference("first", default = true, helper = { helper })
        var launches by intPreference("launches", helper = { helper })
        var last by longPreference("last", 5L, helper = { helper })
        var ratio by floatPreference("ratio", 1.5f, helper = { helper })
        var weight by doublePreference("weight", 2.5, helper = { helper })
        var theme by stringPreference("theme", "system", helper = { helper })
        var sort by enumPreference("sort", Sort.Name, helper = { helper })
        val readOnly by intPreference("launches", helper = { helper })
    }

    @Test
    fun propertiesReadDefaultsThenWhatWasWritten() {
        val helper = createInMemoryJchuPreferencesHelper()
        val settings = Settings(helper)

        assertEquals(true, settings.first)
        assertEquals(0, settings.launches)
        assertEquals(5L, settings.last)
        assertEquals(1.5f, settings.ratio)
        assertEquals(2.5, settings.weight)
        assertEquals("system", settings.theme)
        assertEquals(Sort.Name, settings.sort)

        settings.first = false
        settings.launches = 3
        settings.last = 9L
        settings.ratio = 0.5f
        settings.weight = 4.0
        settings.theme = "dark"
        settings.sort = Sort.Date

        assertEquals(false, settings.first)
        assertEquals(3, settings.launches)
        assertEquals(3, settings.readOnly)
        assertEquals(9L, settings.last)
        assertEquals(0.5f, settings.ratio)
        assertEquals(4.0, settings.weight)
        assertEquals("dark", settings.theme)
        assertEquals(Sort.Date, settings.sort)
        assertEquals("Date", helper.getStringOrNull("sort"))
    }

    @Test
    fun anUnknownStoredEnumNameFallsBackToTheDefault() {
        val helper = createInMemoryJchuPreferencesHelper()
        helper.saveString("sort", "Removed")
        assertEquals(Sort.Name, Settings(helper).sort)
    }

    @Test
    fun withoutAnExplicitHelperTheKoinOrMemoryHelperIsUsed() {
        var counter by intPreference("jchu.test.delegate.counter")
        counter += 2
        assertEquals(2, counter)
        jchuPreferencesHelper().remove("jchu.test.delegate.counter")
    }
}
