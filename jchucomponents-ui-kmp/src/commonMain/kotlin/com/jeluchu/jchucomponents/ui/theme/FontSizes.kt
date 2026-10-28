package com.jeluchu.jchucomponents.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Explicit font-size tokens used for decorative and custom text outside semantic typography styles.
 *
 * Names encode their default size in sp, for example [size16] defaults to 16.sp.
 */
@Stable
class FontSizes(
    val size08: TextUnit = 8.sp,
    val size09: TextUnit = 9.sp,
    val size10: TextUnit = 10.sp,
    val size11: TextUnit = 11.sp,
    val size12: TextUnit = 12.sp,
    val size13: TextUnit = 13.sp,
    val size14: TextUnit = 14.sp,
    val size15: TextUnit = 15.sp,
    val size16: TextUnit = 16.sp,
    val size17: TextUnit = 17.sp,
    val size18: TextUnit = 18.sp,
    val size19: TextUnit = 19.sp,
    val size20: TextUnit = 20.sp,
    val size21: TextUnit = 21.sp,
    val size22: TextUnit = 22.sp,
    val size23: TextUnit = 23.sp,
    val size24: TextUnit = 24.sp,
    val size25: TextUnit = 25.sp,
    val size26: TextUnit = 26.sp,
    val size27: TextUnit = 27.sp,
    val size28: TextUnit = 28.sp,
    val size29: TextUnit = 29.sp,
    val size30: TextUnit = 30.sp,
    val size31: TextUnit = 31.sp,
    val size32: TextUnit = 32.sp,
    val size34: TextUnit = 34.sp,
    val size35: TextUnit = 35.sp,
    val size36: TextUnit = 36.sp,
    val size38: TextUnit = 38.sp,
    val size40: TextUnit = 40.sp,
    val size42: TextUnit = 42.sp,
    val size44: TextUnit = 44.sp,
    val size45: TextUnit = 45.sp,
    val size46: TextUnit = 46.sp,
    val size48: TextUnit = 48.sp,
    val size50: TextUnit = 50.sp,
    val size54: TextUnit = 54.sp,
    val size56: TextUnit = 56.sp,
    val size57: TextUnit = 57.sp,
    val size60: TextUnit = 60.sp,
    val size62: TextUnit = 62.sp,
    val size64: TextUnit = 64.sp,
    val size70: TextUnit = 70.sp,
    val size72: TextUnit = 72.sp,
    val size76: TextUnit = 76.sp,
    val size80: TextUnit = 80.sp,
    val size82: TextUnit = 82.sp,
    val size88: TextUnit = 88.sp,
    val size92: TextUnit = 92.sp,
    val size118: TextUnit = 118.sp,
    val size132: TextUnit = 132.sp
)

val LocalFontSizes = staticCompositionLocalOf { FontSizes() }
