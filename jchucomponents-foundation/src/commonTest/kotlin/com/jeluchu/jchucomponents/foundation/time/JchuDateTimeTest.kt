package com.jeluchu.jchucomponents.foundation.time

import com.jeluchu.jchucomponents.foundation.extensions.time.endOfMonth
import com.jeluchu.jchucomponents.foundation.extensions.time.endOfYear
import com.jeluchu.jchucomponents.foundation.extensions.time.isToday
import com.jeluchu.jchucomponents.foundation.extensions.time.isTomorrow
import com.jeluchu.jchucomponents.foundation.extensions.time.isWeekend
import com.jeluchu.jchucomponents.foundation.extensions.time.isYesterday
import com.jeluchu.jchucomponents.foundation.extensions.time.startOfMonth
import com.jeluchu.jchucomponents.foundation.extensions.time.startOfWeek
import com.jeluchu.jchucomponents.foundation.extensions.time.startOfYear
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Instant

class JchuDateTimeTest {
    @Test
    fun calculatesCalendarBoundaries() {
        val date = LocalDate(2024, 2, 14)

        assertEquals(LocalDate(2024, 2, 1), date.startOfMonth())
        assertEquals(LocalDate(2024, 2, 29), date.endOfMonth())
        assertEquals(LocalDate(2024, 1, 1), date.startOfYear())
        assertEquals(LocalDate(2024, 12, 31), date.endOfYear())
    }

    @Test
    fun respectsConfiguredFirstDayOfWeek() {
        val sunday = LocalDate(2026, 7, 5)

        assertEquals(LocalDate(2026, 6, 29), sunday.startOfWeek(DayOfWeek.MONDAY))
        assertEquals(LocalDate(2026, 7, 5), sunday.startOfWeek(DayOfWeek.SUNDAY))
    }

    @Test
    fun evaluatesRelativeDatesUsingInjectedTimeAndZone() {
        val now = Instant.parse("2026-07-03T23:30:00Z")
        val madrid = TimeZone.of("Europe/Madrid")

        assertTrue(LocalDate(2026, 7, 4).isToday(now, madrid))
        assertTrue(LocalDate(2026, 7, 3).isYesterday(now, madrid))
        assertTrue(LocalDate(2026, 7, 5).isTomorrow(now, madrid))
    }

    @Test
    fun calculatesAgeAroundBirthday() {
        val birthDate = LocalDate(2000, 7, 4)

        assertEquals(25, JchuDateTime.calculateAge(birthDate, LocalDate(2026, 7, 3)))
        assertEquals(26, JchuDateTime.calculateAge(birthDate, LocalDate(2026, 7, 4)))
    }

    @Test
    fun identifiesLeapYearsAndWeekends() {
        assertTrue(JchuDateTime.isLeapYear(2024))
        assertFalse(JchuDateTime.isLeapYear(2100))
        assertTrue(JchuDateTime.isLeapYear(2000))
        assertTrue(LocalDate(2026, 7, 4).isWeekend())
        assertFalse(LocalDate(2026, 7, 3).isWeekend())
    }
}
