package com.jeluchu.jchucomponents.foundation.extensions.time

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import kotlin.time.Instant
import kotlinx.datetime.toKotlinInstant as kotlinxToKotlinInstant

/** Converts this [NSDate] to a Kotlin [Instant]. */
fun NSDate.toKotlinInstant(): Instant = kotlinxToKotlinInstant()

/** Converts this [NSDate] to a [LocalDateTime] in [timeZone]. */
fun NSDate.toKotlinLocalDateTime(timeZone: TimeZone = TimeZone.UTC): LocalDateTime =
    toKotlinInstant().toLocalDateTime(timeZone)

/** Converts this [NSDate] to a [LocalDate] in [timeZone]. */
fun NSDate.toKotlinLocalDate(timeZone: TimeZone = TimeZone.UTC): LocalDate =
    toKotlinLocalDateTime(timeZone).date

/** Formats this date with an `NSDateFormatter` [format] pattern and [locale], for use from Swift. */
fun LocalDate.formatForSwift(
    format: String,
    locale: NSLocale = NSLocale.currentLocale
): String = NSDateFormatter().apply {
    dateFormat = format
    this.locale = locale
}.stringFromDate(toNSDate())
