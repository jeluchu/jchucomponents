package com.jeluchu.jchucomponents.ui.extensions.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.luminance
import kotlin.random.Random

/** Returns this color, or [defaultValue] when it is `null`. */
fun Color?.orTransparent(defaultValue: Color = Color.Transparent): Color = this ?: defaultValue

/** Returns this color when [enabled], or the same color at [opacity] when it is not. */
fun Color.opacity(
    enabled: Boolean,
    opacity: Float = .62f
): Color = copy(alpha = if (enabled) 1f else opacity)

/** `true` when the relative luminance of this color is below `0.5`. */
fun Color.isDark(): Boolean = luminance() < 0.5f

/** A [ColorFilter] that tints with this color. */
fun Color.toColorFilter(): ColorFilter = ColorFilter.tint(this)

/** An opaque color with random red, green and blue channels. */
fun Color.Companion.random(): Color = Color(Random.nextInt(0, 256), Random.nextInt(0, 256), Random.nextInt(0, 256))
