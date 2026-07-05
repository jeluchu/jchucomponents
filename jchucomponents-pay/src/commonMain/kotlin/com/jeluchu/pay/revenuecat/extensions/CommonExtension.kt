package com.jeluchu.pay.revenuecat.extensions

import kotlin.math.pow
import kotlin.math.round

internal fun String.Companion.empty() = ""

internal fun Float.roundTo(n: Int): Float = toDouble().roundTo(n).toFloat()

internal fun Double.roundTo(n: Int): Double {
    val factor = 10.0.pow(n)
    return round(this * factor) / factor
}
