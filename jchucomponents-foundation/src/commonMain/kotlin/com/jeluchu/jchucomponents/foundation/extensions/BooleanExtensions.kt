package com.jeluchu.jchucomponents.foundation.extensions

/**
 * Returns this value, or [defaultValue] when it is `null`.
 */
fun Boolean?.orFalse(defaultValue: Boolean = false): Boolean = this ?: defaultValue

/**
 * Returns `true` only when this nullable value is explicitly `true`.
 */
fun Boolean?.isTrue(): Boolean = this == true
