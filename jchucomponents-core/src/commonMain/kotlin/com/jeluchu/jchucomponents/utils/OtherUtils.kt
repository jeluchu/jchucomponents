/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils

import com.jeluchu.jchucomponents.foundation.uuid.generateUUID
import kotlin.random.Random

fun getUUIDBits(): Long =
    generateUUID()
        .replace("-", "")
        .take(16)
        .toULong(16)
        .toLong() and Long.MAX_VALUE

fun getRandomUUID(): String =
    generateUUID()
        .replace("-", "")
        .uppercase()

fun randInt(
    min: Int,
    max: Int
): Int = Random.Default.nextInt(max - min + 1) + min

fun randDouble(
    min: Double,
    max: Double
): Double = min + (max - min) * Random.Default.nextDouble()
