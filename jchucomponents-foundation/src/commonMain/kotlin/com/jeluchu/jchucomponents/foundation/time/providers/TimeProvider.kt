package com.jeluchu.jchucomponents.foundation.time.providers

expect object TimeProvider {
    fun currentTimeMillis(): Long
}
