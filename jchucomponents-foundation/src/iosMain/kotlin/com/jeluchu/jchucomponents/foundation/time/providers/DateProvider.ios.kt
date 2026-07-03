package com.jeluchu.jchucomponents.foundation.time.providers

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.number
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitSecond
import platform.Foundation.NSCalendarUnitWeekday
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual object DateProvider {
    actual fun formatLocalDate(date: LocalDate, pattern: String): String {
        val nsDate = date.toNSDate() ?: return date.toString()
        return formatter(pattern).stringFromDate(nsDate)
    }

    actual fun formatLocalDateTime(dateTime: LocalDateTime, pattern: String): String {
        val nsDate = dateTime.toNSDate() ?: return dateTime.toString()
        return formatter(pattern).stringFromDate(nsDate)
    }

    actual fun parseLocalDate(value: String, pattern: String): LocalDate {
        val nsDate = formatter(pattern).dateFromString(value)
            ?: throw IllegalArgumentException("Cannot parse date: $value")
        val components = NSCalendar.currentCalendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            nsDate
        )
        return LocalDate(
            year = components.year.toInt(),
            month = components.month.toInt().toMonth(),
            day = components.day.toInt()
        )
    }

    actual fun parseLocalDateTime(value: String, pattern: String): LocalDateTime {
        val nsDate = formatter(pattern).dateFromString(value)
            ?: throw IllegalArgumentException("Cannot parse date time: $value")
        val components = NSCalendar.currentCalendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay or
                NSCalendarUnitHour or NSCalendarUnitMinute or NSCalendarUnitSecond,
            nsDate
        )
        return LocalDateTime(
            year = components.year.toInt(),
            month = components.month.toInt().toMonth(),
            day = components.day.toInt(),
            hour = components.hour.toInt(),
            minute = components.minute.toInt(),
            second = components.second.toInt()
        )
    }

    actual fun firstDayOfWeekFromLocale(): DayOfWeek =
        NSCalendar.currentCalendar.firstWeekday.toInt().toDayOfWeek()

    actual fun currentDayOfWeek(): DayOfWeek {
        val components = NSCalendar.currentCalendar.components(NSCalendarUnitWeekday, NSDate())
        return components.weekday.toInt().toDayOfWeek()
    }

    private fun formatter(pattern: String): NSDateFormatter =
        NSDateFormatter().apply {
            dateFormat = pattern
            locale = NSLocale.currentLocale
        }

    private fun LocalDate.toNSDate(): NSDate? =
        NSCalendar.currentCalendar.dateFromComponents(
            NSDateComponents().apply {
                year = this@toNSDate.year.toLong()
                month = this@toNSDate.month.number.toLong()
                day = this@toNSDate.day.toLong()
                hour = 0
                minute = 0
                second = 0
            }
        )

    private fun LocalDateTime.toNSDate(): NSDate? =
        NSCalendar.currentCalendar.dateFromComponents(
            NSDateComponents().apply {
                year = this@toNSDate.year.toLong()
                month = this@toNSDate.month.number.toLong()
                day = this@toNSDate.day.toLong()
                hour = this@toNSDate.hour.toLong()
                minute = this@toNSDate.minute.toLong()
                second = this@toNSDate.second.toLong()
            }
        )

    private fun Int.toDayOfWeek(): DayOfWeek =
        when (this) {
            1 -> DayOfWeek.SUNDAY
            2 -> DayOfWeek.MONDAY
            3 -> DayOfWeek.TUESDAY
            4 -> DayOfWeek.WEDNESDAY
            5 -> DayOfWeek.THURSDAY
            6 -> DayOfWeek.FRIDAY
            7 -> DayOfWeek.SATURDAY
            else -> DayOfWeek.MONDAY
        }

    private fun Int.toMonth(): Month = Month.entries[this - 1]
}
