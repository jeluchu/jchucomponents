package com.jeluchu.jchucomponents.foundation.text

private fun normalizeNfd(value: String): String = js("value.normalize('NFD')")

private val combiningMarks = Regex("[\\u0300-\\u036f]")

actual fun String.removeDiacritics(): String = normalizeNfd(this).replace(combiningMarks, "")
