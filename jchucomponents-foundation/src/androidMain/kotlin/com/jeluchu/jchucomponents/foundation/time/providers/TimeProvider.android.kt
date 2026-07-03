package com.jeluchu.jchucomponents.foundation.time.providers

actual object TimeProvider {
    actual fun currentTimeMillis(): Long = System.currentTimeMillis()
}
