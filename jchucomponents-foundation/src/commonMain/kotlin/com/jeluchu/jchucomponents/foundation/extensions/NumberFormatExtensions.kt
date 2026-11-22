package com.jeluchu.jchucomponents.foundation.extensions

import kotlin.math.abs
import kotlin.math.roundToInt

/** Whole mebibytes in this number of bytes, as text. */
fun Long.bytesToMeg(): String = (this / (1024L * 1024L)).toString()

/** Formats a duration in milliseconds as `h:mm:ss` (hours only when there are some) or `m:ss`. */
fun Int.milliSecondsToTimer(): String {
    val hours = this / (1000 * 60 * 60)
    val minutes = this % (1000 * 60 * 60) / (1000 * 60)
    val seconds = this % (1000 * 60 * 60) % (1000 * 60) / 1000
    val secondsText = if (seconds < 10) "0$seconds" else "$seconds"
    val hoursText = if (hours > 0) "$hours:" else ""
    val minutesText = if (hours > 0 && minutes < 10) "0$minutes" else "$minutes"
    return "$hoursText$minutesText:$secondsText"
}

/** Rounds this value to the nearest multiple of ten; `null` counts as `0`. */
fun Int?.roundUpToNearestTen(): Int = ((((this ?: 0) + 5) / 10.0).roundToInt() * 10)

/** Groups the digits by thousands with `.`: `1234567` becomes `1.234.567`. */
fun Int.thousandsFormat(): String {
    val grouped = abs(toLong()).toString().reversed().chunked(3).joinToString(".").reversed()
    return if (this < 0) "-$grouped" else grouped
}
