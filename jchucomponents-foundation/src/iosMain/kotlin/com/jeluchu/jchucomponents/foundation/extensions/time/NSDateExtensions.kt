package com.jeluchu.jchucomponents.foundation.extensions.time

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import platform.Foundation.NSDate
import kotlin.time.Instant
import kotlinx.datetime.toNSDate as kotlinxToNSDate

fun LocalDate.toNSDate(timeZone: TimeZone = TimeZone.UTC): NSDate = atStartOfDayIn(timeZone).kotlinxToNSDate()

fun LocalDateTime.toNSDate(timeZone: TimeZone = TimeZone.UTC): NSDate = toInstant(timeZone).kotlinxToNSDate()

fun Instant.toNSDate(): NSDate = kotlinxToNSDate()
