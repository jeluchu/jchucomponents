package com.jeluchu.jchucomponents.foundation.text

import java.text.Normalizer

private val combiningMarks = Regex("\\p{InCombiningDiacriticalMarks}+")

actual fun String.removeDiacritics(): String = Normalizer.normalize(this, Normalizer.Form.NFD).replace(combiningMarks, "")
