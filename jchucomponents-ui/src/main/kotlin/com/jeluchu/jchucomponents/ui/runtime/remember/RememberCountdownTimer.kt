package com.jeluchu.jchucomponents.ui.runtime.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.withFrameMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun rememberCountdownTimerState(
    initialMillis: Long,
    step: Long = 1000
): MutableState<Long> {
    val timeLeft = rememberMutableStateOf(initialMillis)
    LaunchedEffect(initialMillis, step) {
        val startTime = withFrameMillis { it }
        while (isActive && timeLeft.value > 0) {
            val duration = withFrameMillis { time ->
                (time - startTime).coerceAtLeast(0)
            }
            timeLeft.value = (initialMillis - duration).coerceAtLeast(0)
            delay(step.coerceAtMost(timeLeft.value))
        }
    }
    return timeLeft
}
