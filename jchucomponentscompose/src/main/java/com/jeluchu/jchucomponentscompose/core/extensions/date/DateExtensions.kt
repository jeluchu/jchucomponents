package com.jeluchu.jchucomponentscompose.core.extensions.date

import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMAT_VERBOSE = "dd/MM/yyyy"
const val DATE_FORMAT_TIMESTAMP = "dd/MM/yyyy HH:mm"
const val DATE_FORMAT_ONLY_TIME = "HH:mm 'H'"

/** ---- FORMATS ------------------------------------------------------------------------------- **/

fun Date.format(): String =
    SimpleDateFormat(
        com.jeluchu.jchucomponentscompose.core.extensions.date.DATE_FORMAT_VERBOSE,
        Locale.getDefault()
    ).format(this)

fun Date.formatWithTime(): String =
    SimpleDateFormat(
        com.jeluchu.jchucomponentscompose.core.extensions.date.DATE_FORMAT_TIMESTAMP,
        Locale.getDefault()
    ).format(this)

fun Date.formatOnlyTime(): String =
    SimpleDateFormat(
        com.jeluchu.jchucomponentscompose.core.extensions.date.DATE_FORMAT_ONLY_TIME,
        Locale.getDefault()
    ).format(this)

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
