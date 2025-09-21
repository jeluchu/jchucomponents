package com.jeluchu.jchucomponents.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Dimen {
    val sizeSpacing02: Dp = 2.00.dp
    val sizeSpacing04: Dp = 4.00.dp
    val sizeSpacing06: Dp = 6.00.dp
    val sizeSpacing07: Dp = 7.00.dp
    val sizeSpacing08: Dp = 8.00.dp
    val sizeSpacing10: Dp = 8.00.dp
    val sizeSpacing12: Dp = 12.00.dp
    val sizeSpacing16: Dp = 16.00.dp
    val sizeSpacing20: Dp = 20.00.dp
    val sizeSpacing24: Dp = 24.00.dp
    val sizeSpacing32: Dp = 32.00.dp
    val sizeSpacing40: Dp = 40.00.dp
    val sizeSpacing48: Dp = 48.00.dp
    val sizeSpacing56: Dp = 56.00.dp
    val sizeSpacing64: Dp = 64.00.dp
    val sizeSpacing72: Dp = 72.00.dp
    val sizeSpacing80: Dp = 80.00.dp
    val sizeSpacing88: Dp = 88.00.dp
    val sizeSpacing96: Dp = 96.00.dp
    val sizeSpacing120: Dp = 128.00.dp
}

// BETA
@Stable
class Spacing(
    val dimen02: Dp = 2.dp,
    val dimen04: Dp = 4.dp,
    val dimen06: Dp = 6.dp,
    val dimen08: Dp = 8.dp,
    val dimen10: Dp = 10.dp,
    val dimen12: Dp = 12.dp,
    val dimen16: Dp = 16.dp,
    val dimen20: Dp = 20.dp,
    val dimen24: Dp = 24.dp,
    val dimen32: Dp = 32.dp,
    val dimen40: Dp = 40.dp
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }
