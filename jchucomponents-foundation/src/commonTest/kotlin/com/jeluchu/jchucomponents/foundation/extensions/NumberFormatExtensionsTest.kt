package com.jeluchu.jchucomponents.foundation.extensions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NumberFormatExtensionsTest {
    @Test
    fun thousandsFormatGroupsWithDots() {
        assertEquals("1.234.567", 1_234_567.thousandsFormat())
        assertEquals("999", 999.thousandsFormat())
        assertEquals("1.000", 1_000.thousandsFormat())
        assertEquals("0", 0.thousandsFormat())
        assertEquals("-1.000", (-1_000).thousandsFormat())
        assertEquals("-2.147.483.648", Int.MIN_VALUE.thousandsFormat())
    }

    @Test
    fun milliSecondsToTimerShowsHoursOnlyWhenNeeded() {
        assertEquals("0:00", 0.milliSecondsToTimer())
        assertEquals("1:05", 65_000.milliSecondsToTimer())
        assertEquals("1:01:05", 3_665_000.milliSecondsToTimer())
    }

    @Test
    fun roundUpToNearestTenTreatsNullAsZero() {
        assertEquals(20, 14.roundUpToNearestTen())
        assertEquals(10, (null as Int?).roundUpToNearestTen())
    }

    @Test
    fun bytesToMegDropsTheRemainder() {
        assertEquals("5", (5L * 1024 * 1024 + 1).bytesToMeg())
        assertEquals("0", 1_024L.bytesToMeg())
    }

    @Test
    fun orEmptyFallsBackToTheDefault() {
        assertEquals(0, (null as Int?).orEmpty())
        assertEquals(7L, (null as Long?).orEmpty(7L))
        assertEquals(3, 3.orEmpty())
        assertEquals(0.0, (null as Double?).orEmpty())
        assertEquals(0f, (null as Float?).orEmpty())
        assertTrue(5.isNotEmpty())
        assertFalse(0.isNotEmpty())
    }

    @Test
    fun signChecksCoverAllNumberTypes() {
        assertTrue(1.isPositive())
        assertFalse(0.isPositive())
        assertTrue(0.isPositiveOrZero())
        assertTrue((-1.5).isNegative())
        assertTrue(0L.isNegativeOrZero())
        assertFalse(2f.isNegative())
    }
}
