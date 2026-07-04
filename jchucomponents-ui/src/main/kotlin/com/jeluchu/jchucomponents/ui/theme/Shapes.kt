package com.jeluchu.jchucomponents.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius

@Stable
class Shapes(
    val none: Shape = 0.cornerRadius(),
    val corner02: Shape = 2.cornerRadius(),
    val corner03: Shape = 3.cornerRadius(),
    val corner04: Shape = 4.cornerRadius(),
    val corner05: Shape = 5.cornerRadius(),
    val corner06: Shape = 6.cornerRadius(),
    val corner07: Shape = 7.cornerRadius(),
    val corner08: Shape = 8.cornerRadius(),
    val corner09: Shape = 9.cornerRadius(),
    val corner10: Shape = 10.cornerRadius(),
    val corner11: Shape = 11.cornerRadius(),
    val corner12: Shape = 12.cornerRadius(),
    val corner13: Shape = 13.cornerRadius(),
    val corner14: Shape = 14.cornerRadius(),
    val corner15: Shape = 15.cornerRadius(),
    val corner16: Shape = 16.cornerRadius(),
    val corner17: Shape = 17.cornerRadius(),
    val corner18: Shape = 18.cornerRadius(),
    val corner19: Shape = 19.cornerRadius(),
    val corner20: Shape = 20.cornerRadius(),
    val corner21: Shape = 21.cornerRadius(),
    val corner22: Shape = 22.cornerRadius(),
    val corner24: Shape = 24.cornerRadius(),
    val corner25: Shape = 25.cornerRadius(),
    val corner30: Shape = 30.cornerRadius(),
    val corner32: Shape = 32.cornerRadius(),
    val corner34: Shape = 34.cornerRadius(),
    val corner40: Shape = 40.cornerRadius(),
    val corner48: Shape = 48.cornerRadius(),
    val corner50: Shape = 50.cornerRadius(),
    val corner100: Shape = 100.cornerRadius(),
    val corner999: Shape = 999.cornerRadius(),
    val bottomSheetShape: Shape = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 20.dp,
        bottomEnd = 0.dp,
        bottomStart = 0.dp
    ),
    val topSheetShape: Shape = RoundedCornerShape(
        topStart = 0.dp,
        bottomStart = 20.dp,
        topEnd = 0.dp,
        bottomEnd = 20.dp
    ),
    val endSheetShape: Shape = RoundedCornerShape(
        topStart = 0.dp,
        bottomStart = 0.dp,
        topEnd = 10.dp,
        bottomEnd = 10.dp
    )
)

val LocalShapes = staticCompositionLocalOf { Shapes() }