/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.numbers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ktx.constants.FIRST_DAY_OF_MONTH
import com.jeluchu.jchucomponents.ktx.strings.empty
import java.util.*
import kotlin.math.roundToInt

@Composable
fun Int.height() = Spacer(modifier = Modifier.height(this.dp))

@Composable
fun Int.width() = Spacer(modifier = Modifier.width(this.dp))

fun Int.Companion.empty() = 0
fun Int?.orEmpty(defaultValue: Int = Int.empty()) = this ?: defaultValue
fun Int.isNotEmpty() = this != Int.empty()
fun Long.bytesToMeg(): String = (this / (1024L * 1024L)).toString()

fun Float.Companion.empty() = 0f
fun Float?.orEmpty(defaultValue: Float = Float.empty()): Float = this ?: defaultValue

fun Long.Companion.empty() = 0L
fun Long?.orEmpty(defaultValue: Long = Long.empty()): Long = this ?: defaultValue

fun Int.milliSecondsToTimer(): String {

    var finalTimerString = String.empty()
    val secondsString: String

    val hours = (this / (1000 * 60 * 60))
    val minutes = (this % (1000 * 60 * 60)) / (1000 * 60)
    val seconds = (this % (1000 * 60 * 60) % (1000 * 60) / 1000)
    if (hours > 0) finalTimerString = "$hours:"

    secondsString = if (seconds < 10) {
        "0$seconds"
    } else {
        "" + seconds
    }
    finalTimerString = "$finalTimerString$minutes:$secondsString"

    return finalTimerString
}

fun Int?.roundUpToNearestTen(): Int = ((((this ?: 0) + 5) / 10.0).roundToInt() * 10)

/**
 * Return a list of calendar values which contains the months of the year.
 * If the year is the current one, return only the months until the current one.
 */
fun Int.getMonths(): List<Calendar> =
    mutableListOf<Calendar>().also { months ->
        Calendar.getInstance().let { calendar ->
            val maxMonth =
                if (this == calendar.get(Calendar.YEAR)) calendar.get(Calendar.MONTH)
                else Calendar.DECEMBER

            calendar.set(Calendar.YEAR, this)

            for (i in Calendar.JANUARY..maxMonth)
                months.add(
                    Calendar.getInstance().apply {
                        set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                        set(Calendar.MONTH, i)
                        set(Calendar.DAY_OF_MONTH, FIRST_DAY_OF_MONTH)
                    }
                )
        }
    }