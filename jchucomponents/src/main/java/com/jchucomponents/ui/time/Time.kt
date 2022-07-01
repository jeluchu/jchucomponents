/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.ui.time

import androidx.compose.runtime.*
import com.jchucomponents.core.extensions.ints.fixedDecimalsTime
import kotlinx.coroutines.delay
import java.util.*

/**
 *
 * Author: @Jeluchu
 *
 * This component only returns the current time (it is a kind of clock)
 * which is updated every minute.
 *
 */

@Composable
fun minutesLeft(): String {

    var time by remember { mutableStateOf(currentTime()) }

    LaunchedEffect(0) {
        while (true) {
            time = currentTime()
            delay(1000)
        }
    }

    return "${time.hours}:${time.minutes} ${time.zoneDay}"
}

fun currentTime(): Time {
    val cal = Calendar.getInstance()
    Calendar.AM_PM
    return Time(
        hours = cal.get(Calendar.HOUR_OF_DAY).fixedDecimalsTime(),
        minutes = cal.get(Calendar.MINUTE).fixedDecimalsTime(),
        zoneDay = if (cal.get(Calendar.AM_PM) == 0) "AM" else "PM"
    )
}

data class Time(
    var hours: String,
    var minutes: String,
    var zoneDay: String
)
