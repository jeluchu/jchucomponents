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

/**
 *
 * Constant to perform the time formatting as follows: "EEEE, MMMM d, yyyy - hh:mm:ss a"
 * For example: "21:02 hrs"
 *
 * E: Day of week
 * M: Month of year
 * d: Day of month
 * y: Year
 * H: Hour
 * m: Minutes
 * s: Seconds
 * a: AM / PM
 *
 */
const val DATE_FORMAT_WEEK_AND_MONTH_TIME = "EEEE, MMMM d, yyyy - hh:mm:ss a"

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

/**
 * Convert time in **seconds** to `hh:mm:ss` or `mm:ss`
 */
fun Int.toDurationText(): String = this.run {
    if (this > 3600) "%d:%02d:%02d".format(this / 3600, (this % 3600) / 60, this % 60)
    else "%02d:%02d".format(this / 60, this % 60)
}

fun Date.addYears(years: Int): Date = add(Calendar.YEAR, years)
fun Date.addMonths(months: Int): Date = add(Calendar.MONTH, months)
fun Date.addDays(days: Int): Date = add(Calendar.DAY_OF_MONTH, days)
fun Date.addHours(hours: Int): Date = add(Calendar.HOUR_OF_DAY, hours)
fun Date.addMinutes(minutes: Int): Date = add(Calendar.MINUTE, minutes)
fun Date.addSeconds(seconds: Int): Date = add(Calendar.SECOND, seconds)

/** ---- CALENDAR ------------------------------------------------------------------------------ **/

fun Date.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}

fun Date.backInYears(years: Int): Date =
    Calendar.getInstance().also { calendar ->
        calendar.time = this
        calendar.add(Calendar.YEAR, -years)
    }.time

fun Date.forwardInYears(years: Int): Date =
    Calendar.getInstance().also { calendar ->
        calendar.time = this
        calendar.add(Calendar.YEAR, years)
    }.time
