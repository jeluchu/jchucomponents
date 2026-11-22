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

/** Returns `0`. Mirrors [String.Companion.empty] for numbers. */
fun Int.Companion.empty(): Int = 0

/** Returns `0L`. */
fun Long.Companion.empty(): Long = 0L

/** Returns `0f`. */
fun Float.Companion.empty(): Float = 0f

/** Returns `0.0`. */
fun Double.Companion.empty(): Double = 0.0

/** Returns this value, or [defaultValue] when it is `null`. Same as [orZero] with the [String.orEmpty] naming. */
fun Int?.orEmpty(defaultValue: Int = Int.empty()): Int = this ?: defaultValue

/** Returns this value, or [defaultValue] when it is `null`. */
fun Long?.orEmpty(defaultValue: Long = Long.empty()): Long = this ?: defaultValue

/** Returns this value, or [defaultValue] when it is `null`. */
fun Float?.orEmpty(defaultValue: Float = Float.empty()): Float = this ?: defaultValue

/** Returns this value, or [defaultValue] when it is `null`. */
fun Double?.orEmpty(defaultValue: Double = Double.empty()): Double = this ?: defaultValue

/** `true` when this value is not `0`. */
fun Int.isNotEmpty(): Boolean = this != Int.empty()

/** `true` when this number is greater than zero. */
fun <T : Number> T.isPositive(): Boolean = toDouble() > 0

/** `true` when this number is greater than or equal to zero. */
fun <T : Number> T.isPositiveOrZero(): Boolean = toDouble() >= 0

/** `true` when this number is lower than zero. */
fun <T : Number> T.isNegative(): Boolean = toDouble() < 0

/** `true` when this number is lower than or equal to zero. */
fun <T : Number> T.isNegativeOrZero(): Boolean = toDouble() <= 0

