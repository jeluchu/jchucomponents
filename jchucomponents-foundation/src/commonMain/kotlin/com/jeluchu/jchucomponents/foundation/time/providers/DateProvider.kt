package com.jeluchu.jchucomponents.foundation.time.providers

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

expect object DateProvider {
    fun formatLocalDate(date: LocalDate, pattern: String): String
    fun formatLocalDateTime(dateTime: LocalDateTime, pattern: String): String
    fun parseLocalDate(value: String, pattern: String): LocalDate
    fun parseLocalDateTime(value: String, pattern: String): LocalDateTime
    fun firstDayOfWeekFromLocale(): DayOfWeek
    fun currentDayOfWeek(): DayOfWeek
}
