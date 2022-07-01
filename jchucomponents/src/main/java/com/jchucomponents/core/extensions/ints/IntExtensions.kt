/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.core.extensions.ints

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jchucomponents.core.extensions.strings.empty
import java.math.RoundingMode
import java.text.DecimalFormat

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
fun Long?.orEmpty(defaultValue: Long = Long.empty()): Long =  this ?: defaultValue

fun Int.fixedDecimalsTime(): String {
    val decimalFormat = DecimalFormat("00")
    decimalFormat.roundingMode = RoundingMode.DOWN
    return decimalFormat.format(this)
}

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