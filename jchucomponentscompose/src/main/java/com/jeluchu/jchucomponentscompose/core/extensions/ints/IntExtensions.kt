package com.jeluchu.jchucomponentscompose.core.extensions.ints

import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty
import java.math.RoundingMode
import java.text.DecimalFormat

fun Int.Companion.empty() = 0
fun Int?.orEmpty() = this ?: Int.empty()


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