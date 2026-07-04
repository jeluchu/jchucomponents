package com.jeluchu.jchucomponents.foundation.time

import com.jeluchu.jchucomponents.foundation.extensions.time.nextMonth
import com.jeluchu.jchucomponents.foundation.extensions.time.previousMonth
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class YearMonthTest {
    @Test
    fun exposesMonthBoundaries() {
        val february = YearMonth(2024, 2)

        assertEquals(LocalDate(2024, 2, 1), february.atStartOfMonth())
        assertEquals(LocalDate(2024, 2, 29), february.atEndOfMonth())
        assertEquals(29, february.lengthOfMonth())
    }

    @Test
    fun crossesYearBoundaries() {
        assertEquals(YearMonth(2027, 1), YearMonth(2026, 12).nextMonth)
        assertEquals(YearMonth(2025, 12), YearMonth(2026, 1).previousMonth)
    }

    @Test
    fun handlesYearsBeforeZeroWhenSubtractingMonths() {
        assertEquals(YearMonth(-1, 12), YearMonth(0, 1).minusMonths(1))
    }

    @Test
    fun validatesMonth() {
        assertFailsWith<IllegalArgumentException> { YearMonth(2026, 0) }
        assertFailsWith<IllegalArgumentException> { YearMonth(2026, 13) }
    }
}
