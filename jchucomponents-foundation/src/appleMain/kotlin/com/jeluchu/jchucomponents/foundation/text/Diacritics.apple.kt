package com.jeluchu.jchucomponents.foundation.text

import platform.Foundation.NSString
import platform.Foundation.decomposedStringWithCanonicalMapping

private val combiningMarks = Regex("[\\u0300-\\u036f]+")

@Suppress("CAST_NEVER_SUCCEEDS")
actual fun String.removeDiacritics(): String = (this as NSString).decomposedStringWithCanonicalMapping.replace(combiningMarks, "")
