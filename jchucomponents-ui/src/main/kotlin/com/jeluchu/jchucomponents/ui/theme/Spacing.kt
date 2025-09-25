package com.jeluchu.jchucomponents.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
class Spacing(
    val dimen02: Dp = 2.dp,
    val dimen03: Dp = 3.dp,
    val dimen04: Dp = 4.dp,
    val dimen05: Dp = 5.dp,
    val dimen06: Dp = 6.dp,
    val dimen07: Dp = 7.dp,
    val dimen08: Dp = 8.dp,
    val dimen09: Dp = 9.dp,
    val dimen10: Dp = 10.dp,
    val dimen11: Dp = 11.dp,
    val dimen12: Dp = 12.dp,
    val dimen13: Dp = 13.dp,
    val dimen14: Dp = 14.dp,
    val dimen15: Dp = 15.dp,
    val dimen16: Dp = 16.dp,
    val dimen17: Dp = 17.dp,
    val dimen18: Dp = 18.dp,
    val dimen19: Dp = 19.dp,
    val dimen20: Dp = 20.dp,
    val dimen24: Dp = 24.dp,
    val dimen32: Dp = 32.dp,
    val dimen40: Dp = 40.dp
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }
