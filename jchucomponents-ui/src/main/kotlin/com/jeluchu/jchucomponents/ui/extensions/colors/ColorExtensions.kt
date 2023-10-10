package com.jeluchu.jchucomponents.ui.extensions.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import kotlin.random.Random

fun Color.Companion.random(): Color {
    val red = Random.nextInt(0, 256)
    val green = Random.nextInt(0, 256)
    val blue = Random.nextInt(0, 256)
    return Color(red, green, blue)
}

fun Color.isDark() = ColorUtils.calculateLuminance(toArgb()) < 0.5
