package com.jeluchu.jchucomponents.foundation.time.providers

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.number
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

actual object DateProvider {
    actual fun formatLocalDate(date: LocalDate, pattern: String): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(date.toDate())

    actual fun formatLocalDateTime(dateTime: LocalDateTime, pattern: String): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(dateTime.toDate())

    actual fun parseLocalDate(value: String, pattern: String): LocalDate {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault()).apply { isLenient = false }
        val calendar = Calendar.getInstance().apply {
            time = formatter.parse(value)
                ?: throw IllegalArgumentException("Cannot parse date: $value")
        }
        return LocalDate(
            year = calendar.get(Calendar.YEAR),
            month = (calendar.get(Calendar.MONTH) + 1).toMonth(),
            day = calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    actual fun parseLocalDateTime(value: String, pattern: String): LocalDateTime {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault()).apply { isLenient = false }
        val calendar = Calendar.getInstance().apply {
            time = formatter.parse(value)
                ?: throw IllegalArgumentException("Cannot parse date time: $value")
        }
        return LocalDateTime(
            year = calendar.get(Calendar.YEAR),
            month = (calendar.get(Calendar.MONTH) + 1).toMonth(),
            day = calendar.get(Calendar.DAY_OF_MONTH),
            hour = calendar.get(Calendar.HOUR_OF_DAY),
            minute = calendar.get(Calendar.MINUTE),
            second = calendar.get(Calendar.SECOND),
            nanosecond = calendar.get(Calendar.MILLISECOND) * 1_000_000
        )
    }

    actual fun firstDayOfWeekFromLocale(): DayOfWeek =
        Calendar.getInstance(Locale.getDefault()).firstDayOfWeek.toDayOfWeek()

    actual fun currentDayOfWeek(): DayOfWeek =
        Calendar.getInstance().get(Calendar.DAY_OF_WEEK).toDayOfWeek()

    private fun LocalDate.toDate(): Date = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month.number - 1)
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    private fun LocalDateTime.toDate(): Date = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month.number - 1)
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, second)
        set(Calendar.MILLISECOND, nanosecond / 1_000_000)
    }.time

    private fun Int.toDayOfWeek(): DayOfWeek =
        when (this) {
            Calendar.MONDAY -> DayOfWeek.MONDAY
            Calendar.TUESDAY -> DayOfWeek.TUESDAY
            Calendar.WEDNESDAY -> DayOfWeek.WEDNESDAY
            Calendar.THURSDAY -> DayOfWeek.THURSDAY
            Calendar.FRIDAY -> DayOfWeek.FRIDAY
            Calendar.SATURDAY -> DayOfWeek.SATURDAY
            Calendar.SUNDAY -> DayOfWeek.SUNDAY
            else -> DayOfWeek.MONDAY
        }

    private fun Int.toMonth(): Month = Month.entries[this - 1]
}
