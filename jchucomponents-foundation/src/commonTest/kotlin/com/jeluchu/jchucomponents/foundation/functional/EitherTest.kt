package com.jeluchu.jchucomponents.foundation.functional

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EitherTest {
    @Test
    fun mapsRightValue() {
        val result = right<String, Int>(2).map { it * 3 }

        assertEquals(6, result.getOrElse(0))
        assertTrue(result.isRight)
    }

    @Test
    fun keepsLeftValue() {
        val result = left<String, Int>("error").map { it * 3 }

        assertEquals(0, result.getOrElse(0))
        assertEquals("error", result.fold(onLeft = { it }, onRight = { "ok" }))
    }
}
