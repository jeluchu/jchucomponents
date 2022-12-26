/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.date

import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * Constant to perform the date formatting as follows: "dd/MM/yyyy"
 * For example: "04/07/2022"
 *
 * d: Day
 * M: Month
 * y: Year
 *
 */
const val DATE_FORMAT_VERBOSE = "dd/MM/yyyy"

/**
 *
 * Constant to perform the date formatting as follows: "dd/MM/yyyy HH:mm"
 * For example: "04/07/2022 21:02"
 *
 * d: Day
 * M: Month
 * y: Year
 * H: Hour
 * m: Minutes
 *
 */
const val DATE_FORMAT_TIMESTAMP = "dd/MM/yyyy HH:mm"

/**
 *
 * Constant to perform the time formatting as follows: "HH:mm 'H'"
 * For example: "21:02 hrs"
 *
 * H: Hour
 * m: Minutes
 * 'H': Custom String param
 *
 */
const val DATE_FORMAT_ONLY_TIME = "HH:mm 'H'"

/** ---- FORMATS ------------------------------------------------------------------------------- **/

fun Date.format(): String =
    SimpleDateFormat(
        DATE_FORMAT_VERBOSE,
        Locale.getDefault()
    ).format(this)

fun Date.formatWithTime(): String =
    SimpleDateFormat(
        DATE_FORMAT_TIMESTAMP,
        Locale.getDefault()
    ).format(this)

fun Date.formatOnlyTime(): String =
    SimpleDateFormat(
        DATE_FORMAT_ONLY_TIME,
        Locale.getDefault()
    ).format(this)

fun simpleFormat(pattern: String) = SimpleDateFormat(pattern, Locale.getDefault())

/**
 * Pattern: yyyy-MM-dd HH:mm:ss
 */
fun Date.formatToServerDateTimeDefaults(): String = simpleFormat("yyyy-MM-dd HH:mm:ss").format(this)

/**
 * Pattern: yyyy-MM-dd
 */
fun Date.formatToServerDateDefaults(): String = simpleFormat("yyyy-MM-dd").format(this)


fun Date.formatToTruncatedDateTime(): String = simpleFormat("yyyyMMddHHmmss").format(this)

/**
 * Pattern: HH:mm:ss
 */
fun Date.formatToServerTimeDefaults(): String = simpleFormat("HH:mm:ss").format(this)

/**
 * Pattern: dd/MM/yyyy HH:mm:ss
 */
fun Date.formatToViewDateTimeDefaults(): String = simpleFormat("dd/MM/yyyy HH:mm:ss").format(this)

/**
 * Pattern: dd/MM/yyyy
 */
fun Date.formatToViewDateDefaults(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Pattern: HH:mm:ss
 */
fun Date.formatToViewTimeDefaults(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Add field date to current date
 */
fun Date.add(field: Int, amount: Int): Date {
    Calendar.getInstance().apply {
        time = this@add
        add(field, amount)
        return time
    }
}

fun Date.addYears(years: Int): Date {
    return add(Calendar.YEAR, years)
}

fun Date.addMonths(months: Int): Date {
    return add(Calendar.MONTH, months)
}

fun Date.addDays(days: Int): Date {
    return add(Calendar.DAY_OF_MONTH, days)
}

fun Date.addHours(hours: Int): Date {
    return add(Calendar.HOUR_OF_DAY, hours)
}

fun Date.addMinutes(minutes: Int): Date {
    return add(Calendar.MINUTE, minutes)
}

fun Date.addSeconds(seconds: Int): Date {
    return add(Calendar.SECOND, seconds)
}

/** ---- CALENDAR ------------------------------------------------------------------------------ **/

fun Date.addYear(years: Int): Date =
    this.toCalendar()
        .run {
            add(Calendar.YEAR, years)
            this.time
        }

fun Date.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}
