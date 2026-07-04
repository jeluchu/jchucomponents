package com.jeluchu.jchucomponents.foundation.time

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.number

/**
 * A year and month without a day or time zone.
 */
data class YearMonth(
    val year: Int,
    val month: Int,
) {
    init {
        require(value = month in 1..12) { "Month must be between 1 and 12" }
    }

    fun atStartOfMonth(): LocalDate = LocalDate(year, month, 1)

    fun atDay(day: Int): LocalDate = LocalDate(year, month, day)

    fun atEndOfMonth(): LocalDate = plusMonths(1).atStartOfMonth().minus(1, DateTimeUnit.DAY)

    fun lengthOfMonth(): Int = atEndOfMonth().day

    fun plusMonths(months: Int): YearMonth {
        val totalMonths = year.toLong() * 12L + month - 1L + months
        val targetYear = floorDiv(value = totalMonths, divisor = 12L)
        val targetMonth = totalMonths - targetYear * 12L + 1L
        require(value = targetYear in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong()) {
            "Resulting year is outside the supported range"
        }
        return YearMonth(targetYear.toInt(), month = targetMonth.toInt())
    }

    fun minusMonths(months: Int): YearMonth {
        require(months != Int.MIN_VALUE) { "Months value is too small" }
        return plusMonths(-months)
    }

    override fun toString(): String = "$year-${month.toString().padStart(length = 2, padChar = '0')}"

    companion object {
        fun now(): YearMonth = from(date = JchuDateTime.today())

        fun of(
            year: Int,
            month: Int,
        ): YearMonth = YearMonth(year, month)

        fun from(date: LocalDate): YearMonth = YearMonth(year = date.year, month = date.month.number)
    }
}

private fun floorDiv(
    value: Long,
    divisor: Long,
): Long {
    val quotient = value / divisor
    val remainder = value % divisor
    return if (remainder != 0L && value < 0L) quotient - 1L else quotient
}
