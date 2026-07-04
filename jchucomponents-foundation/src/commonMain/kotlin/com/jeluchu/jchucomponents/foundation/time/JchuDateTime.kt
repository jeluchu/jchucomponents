package com.jeluchu.jchucomponents.foundation.time

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Multiplatform date and time operations shared by Android and iOS.
 *
 * Functions that depend on the current time accept explicit [Instant] and [TimeZone] values,
 * making them deterministic in tests while retaining useful system defaults.
 */
object JchuDateTime {
    fun now(): Instant = Clock.System.now()

    fun today(
        instant: Instant = now(),
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): LocalDate = instant.toLocalDateTime(timeZone).date

    fun nowLocal(
        instant: Instant = now(),
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): LocalDateTime = instant.toLocalDateTime(timeZone)

    @Throws(IllegalArgumentException::class)
    fun parseDate(value: String): LocalDate = LocalDate.parse(value)

    @Throws(IllegalArgumentException::class)
    fun parseDateTime(value: String): LocalDateTime = LocalDateTime.parse(value)

    @Throws(IllegalArgumentException::class)
    fun parseInstant(value: String): Instant = Instant.parse(value)

    fun fromEpochMilliseconds(value: Long): Instant = Instant.fromEpochMilliseconds(value)

    fun fromEpochSeconds(value: Long): Instant = Instant.fromEpochSeconds(value)

    fun daysBetween(
        start: LocalDate,
        end: LocalDate,
    ): Long = start.until(end, DateTimeUnit.DAY)

    fun monthsBetween(
        start: LocalDate,
        end: LocalDate,
    ): Long = start.until(end, DateTimeUnit.MONTH)

    fun yearsBetween(
        start: LocalDate,
        end: LocalDate,
    ): Long = start.until(end, DateTimeUnit.YEAR)

    fun isLeapYear(year: Int): Boolean = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)

    fun daysInMonth(
        year: Int,
        month: Int,
    ): Int = YearMonth(year, month).lengthOfMonth()

    fun daysInYear(year: Int): Int = if (isLeapYear(year)) 366 else 365

    @Throws(IllegalArgumentException::class)
    fun calculateAge(
        birthDate: LocalDate,
        referenceDate: LocalDate,
    ): Int {
        require(referenceDate >= birthDate) { "Reference date cannot be before birth date" }

        val birthdayHasPassed =
            referenceDate.month > birthDate.month ||
                referenceDate.month == birthDate.month &&
                referenceDate.day >= birthDate.day

        return referenceDate.year - birthDate.year - if (birthdayHasPassed) 0 else 1
    }

    fun daysOfWeek(firstDayOfWeek: DayOfWeek): List<DayOfWeek> {
        val days = DayOfWeek.entries
        val firstIndex = days.indexOf(firstDayOfWeek)
        return days.drop(firstIndex) + days.take(firstIndex)
    }
}
