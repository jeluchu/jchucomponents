package com.jeluchu.jchucomponents.prefs

import com.jeluchu.jchucomponents.config.JchuConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JchuPreferencesConfigTest {
    @Test
    fun preferencesBlockContributesOneKoinModule() {
        val result = JchuConfig { preferences { fileName = "app.preferences_pb" } }

        assertEquals(1, result.modules.size)
    }

    @Test
    fun blankFileNameIsRejected() {
        assertFailsWith<IllegalArgumentException> { JchuConfig { preferences { fileName = " " } } }
    }
}
