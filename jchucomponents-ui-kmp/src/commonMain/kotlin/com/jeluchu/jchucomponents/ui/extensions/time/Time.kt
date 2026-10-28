/*
*
*  Copyright 2022 Jeluchu
*
*/

package com.jeluchu.jchucomponents.ui.extensions.time

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.jeluchu.jchucomponents.ui.runtime.remember.rememberMutableStateOf
import kotlinx.coroutines.delay
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

/**
 *
 * Author: @Jeluchu
 *
 * This component only returns the current time (it is a kind of clock)
 * which is updated every minute.
 *
 */

@Composable
fun clock(): String {
    var time by rememberMutableStateOf(currentTime())

    LaunchedEffect(0) {
        while (true) {
            time = currentTime()
            delay(1000)
        }
    }

    return "${time.hours}:${time.minutes}"
}

@Composable
fun clockTime(): Time {
    var time by rememberMutableStateOf(currentTime())

    LaunchedEffect(0) {
        while (true) {
            time = currentTime()
            delay(1000)
        }
    }

    return time
}

@Composable
fun clockWithZoneDay(): String {
    var time by rememberMutableStateOf(currentTime())

    LaunchedEffect(0) {
        while (true) {
            time = currentTime()
            delay(1000)
        }
    }

    return "${time.hours}:${time.minutes} ${time.zoneDay}"
}

fun currentTime(): Time {
    val localTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return Time(
        hours = localTime.hour.fixedDecimalsTime(),
        minutes = localTime.minute.fixedDecimalsTime(),
        zoneDay = if (localTime.hour < 12) "AM" else "PM"
    )
}

fun Int.fixedDecimalsTime(): String {
    val magnitude = toLong().let { if (it < 0) -it else it }.toString().padStart(2, '0')
    return if (this < 0) "-$magnitude" else magnitude
}

data class Time(
    var hours: String,
    var minutes: String,
    var zoneDay: String
)
