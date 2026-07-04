package com.jeluchu.jchucomponents.foundation.extensions

/** Returns the additive identity for [Int]. */
fun Int.Companion.zero(): Int = 0

/** Returns the additive identity for [Long]. */
fun Long.Companion.zero(): Long = 0L

/** Returns the additive identity for [Float]. */
fun Float.Companion.zero(): Float = 0f

/** Returns the additive identity for [Double]. */
fun Double.Companion.zero(): Double = 0.0

/** Returns this value, or [defaultValue] when it is `null`. */
fun Int?.orZero(defaultValue: Int = Int.zero()): Int = this ?: defaultValue

/** Returns this value, or [defaultValue] when it is `null`. */
fun Long?.orZero(defaultValue: Long = Long.zero()): Long = this ?: defaultValue

/** Returns this value, or [defaultValue] when it is `null`. */
fun Float?.orZero(defaultValue: Float = Float.zero()): Float = this ?: defaultValue

/** Returns this value, or [defaultValue] when it is `null`. */
fun Double?.orZero(defaultValue: Double = Double.zero()): Double = this ?: defaultValue
