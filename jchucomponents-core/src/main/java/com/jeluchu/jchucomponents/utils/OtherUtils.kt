/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils

import java.util.*

fun getUUIDBits(): Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE

fun getRandomUUID(): String =
    UUID.randomUUID().toString().replace("-", "").uppercase(Locale.getDefault())

fun randInt(min: Int, max: Int): Int = Random().nextInt(max - min + 1) + min
fun randDouble(min: Double, max: Double): Double = min + (max - min) * Random().nextDouble()