package com.jeluchu.jchucomponents.foundation.time

import com.jeluchu.jchucomponents.foundation.extensions.time.format
import com.jeluchu.jchucomponents.foundation.extensions.time.toLocalDate
import com.jeluchu.jchucomponents.foundation.extensions.time.toLocalDateTime
import com.jeluchu.jchucomponents.foundation.time.providers.DateProvider
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DateProviderTest {
    @Test
    fun formatsLocalDateAndDateTimeWithPattern() {
        assertEquals("03/07/2026", LocalDate(2026, 7, 3).format("dd/MM/yyyy"))
        assertEquals("03/07/2026 09:15:30", LocalDateTime(2026, 7, 3, 9, 15, 30).format("dd/MM/yyyy HH:mm:ss"))
    }

    @Test
    fun parsesLocalDateAndDateTimeWithPattern() {
        assertEquals(LocalDate(2026, 7, 3), "03/07/2026".toLocalDate("dd/MM/yyyy"))
        assertEquals(
            LocalDateTime(2026, 7, 3, 9, 15, 30),
            "03/07/2026 09:15:30".toLocalDateTime("dd/MM/yyyy HH:mm:ss"),
        )
    }

    @Test
    fun exposesPlatformWeekDays() {
        assertTrue(DateProvider.firstDayOfWeekFromLocale() in DayOfWeek.entries)
        assertTrue(DateProvider.currentDayOfWeek() in DayOfWeek.entries)
    }
}
