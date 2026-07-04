package com.jeluchu.jchucomponents.foundation.collections

import com.jeluchu.jchucomponents.foundation.extensions.lists.addAllIfNotExist
import com.jeluchu.jchucomponents.foundation.extensions.lists.concatenateLowercase
import kotlin.test.Test
import kotlin.test.assertEquals

class MutableListExtensionsTest {
    @Test
    fun addsOnlyMissingElements() {
        val values = mutableListOf(1, 2)

        values.addAllIfNotExist(listOf(2, 3, 4))

        assertEquals(listOf(1, 2, 3, 4), values)
    }

    @Test
    fun concatenatesLowercaseStrings() {
        assertEquals("helloworld", mutableListOf("Hello", "WORLD").concatenateLowercase())
    }
}
