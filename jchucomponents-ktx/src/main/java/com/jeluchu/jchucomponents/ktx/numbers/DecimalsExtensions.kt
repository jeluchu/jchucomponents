/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.numbers

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun Float.roundTo(n: Int): Float = toBigDecimal().setScale(n, RoundingMode.UP).toFloat()

fun Double.roundTo(n: Int): Double = toBigDecimal().setScale(n, RoundingMode.UP).toDouble()

/**
 * Return date in specified format.
 * @param milliSeconds Date in milliseconds
 * @param dateFormat Date format
 * @return String representing date in specified format
 */
fun Long.getDate(dateFormat: String?): String? {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    val calendar: Calendar = Calendar.getInstance().apply { timeInMillis = this@getDate }
    return formatter.format(calendar.time)
}

fun Double.toPriceAmount(): String = DecimalFormat("###,###,###.00").format(this)