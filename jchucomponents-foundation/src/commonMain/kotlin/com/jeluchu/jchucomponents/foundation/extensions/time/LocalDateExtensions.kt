package com.jeluchu.jchucomponents.foundation.extensions.time

import com.jeluchu.jchucomponents.foundation.time.JchuDateTime
import com.jeluchu.jchucomponents.foundation.time.YearMonth
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlin.time.Instant

fun LocalDate.plusDays(days: Int): LocalDate = plus(days, DateTimeUnit.DAY)

fun LocalDate.minusDays(days: Int): LocalDate = minus(days, DateTimeUnit.DAY)

fun LocalDate.plusMonths(months: Int): LocalDate = plus(months, DateTimeUnit.MONTH)

fun LocalDate.minusMonths(months: Int): LocalDate = minus(months, DateTimeUnit.MONTH)

fun LocalDate.plusYears(years: Int): LocalDate = plus(years, DateTimeUnit.YEAR)

fun LocalDate.minusYears(years: Int): LocalDate = minus(years, DateTimeUnit.YEAR)

fun LocalDate.startOfMonth(): LocalDate = LocalDate(year, month, 1)

fun LocalDate.endOfMonth(): LocalDate = startOfMonth().plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)

fun LocalDate.startOfYear(): LocalDate = LocalDate(year, 1, 1)

fun LocalDate.endOfYear(): LocalDate = LocalDate(year, 12, 31)

fun LocalDate.startOfWeek(firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY): LocalDate {
    val daysFromStart = (dayOfWeek.ordinal - firstDayOfWeek.ordinal + 7) % 7
    return minus(daysFromStart, DateTimeUnit.DAY)
}

fun LocalDate.endOfWeek(firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY): LocalDate = startOfWeek(firstDayOfWeek).plus(6, DateTimeUnit.DAY)

fun LocalDate.isBetween(
    start: LocalDate,
    endInclusive: LocalDate,
): Boolean {
    require(start <= endInclusive) { "Start date cannot be after end date" }
    return this in start..endInclusive
}

fun LocalDate.isToday(
    instant: Instant = JchuDateTime.now(),
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): Boolean = this == JchuDateTime.today(instant, timeZone)

fun LocalDate.isYesterday(
    instant: Instant = JchuDateTime.now(),
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): Boolean = this == JchuDateTime.today(instant, timeZone).minusDays(1)

fun LocalDate.isTomorrow(
    instant: Instant = JchuDateTime.now(),
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): Boolean = this == JchuDateTime.today(instant, timeZone).plusDays(1)

fun LocalDate.isWeekend(): Boolean = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY

val LocalDate.yearMonth: YearMonth
    get() = YearMonth.from(this)
