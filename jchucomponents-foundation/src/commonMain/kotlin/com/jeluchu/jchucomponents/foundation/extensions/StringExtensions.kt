package com.jeluchu.jchucomponents.foundation.extensions

/** Returns an empty [String]. */
fun String.Companion.empty(): String = ""

/** Returns this value, or [defaultValue] when it is `null`. */
fun String?.orEmpty(defaultValue: String = String.empty()): String = this ?: defaultValue
