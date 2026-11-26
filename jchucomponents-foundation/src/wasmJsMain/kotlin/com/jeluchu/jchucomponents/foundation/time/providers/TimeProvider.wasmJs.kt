package com.jeluchu.jchucomponents.foundation.time.providers

import kotlin.time.Clock

actual object TimeProvider {
    actual fun currentTimeMillis(): Long = Clock.System.now().toEpochMilliseconds()
}
