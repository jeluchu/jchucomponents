package com.jeluchu.jchucomponents.foundation.extensions.time

import com.jeluchu.jchucomponents.foundation.time.providers.DateProvider
import com.jeluchu.jchucomponents.foundation.time.providers.TimeProvider
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

fun currentTimeMillis(): Long = TimeProvider.currentTimeMillis()

fun LocalDate.format(pattern: String): String = DateProvider.formatLocalDate(date = this, pattern = pattern)

fun LocalDateTime.format(pattern: String): String = DateProvider.formatLocalDateTime(dateTime = this, pattern = pattern)

@Throws(IllegalArgumentException::class)
fun String.toLocalDate(pattern: String): LocalDate = DateProvider.parseLocalDate(value = this, pattern = pattern)

@Throws(IllegalArgumentException::class)
fun String.toLocalDateTime(pattern: String): LocalDateTime = DateProvider.parseLocalDateTime(value = this, pattern = pattern)

fun firstDayOfWeekFromLocale(): DayOfWeek = DateProvider.firstDayOfWeekFromLocale()

fun currentDayOfWeek(): DayOfWeek = DateProvider.currentDayOfWeek()
