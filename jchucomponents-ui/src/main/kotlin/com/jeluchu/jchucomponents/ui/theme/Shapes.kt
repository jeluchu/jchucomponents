package com.jeluchu.jchucomponents.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius

@Stable
class Shapes(
    val corner02: Shape = 2.cornerRadius(),
    val corner03: Shape = 3.cornerRadius(),
    val corner04: Shape = 4.cornerRadius(),
    val corner05: Shape = 5.cornerRadius(),
    val corner06: Shape = 6.cornerRadius(),
    val corner07: Shape = 7.cornerRadius(),
    val corner08: Shape = 8.cornerRadius(),
    val corner09: Shape = 9.cornerRadius(),
    val corner10: Shape = 10.cornerRadius(),
    val corner12: Shape = 12.cornerRadius(),
    val corner13: Shape = 13.cornerRadius(),
    val corner14: Shape = 14.cornerRadius(),
    val corner15: Shape = 15.cornerRadius(),
    val corner16: Shape = 16.cornerRadius(),
    val corner17: Shape = 17.cornerRadius(),
    val corner18: Shape = 18.cornerRadius(),
    val corner19: Shape = 19.cornerRadius(),
    val corner20: Shape = 20.cornerRadius(),
    val corner24: Shape = 24.cornerRadius(),
    val corner32: Shape = 32.cornerRadius(),
    val corner40: Shape = 40.cornerRadius(),
    val corner48: Shape = 48.cornerRadius()
)

val LocalShapes = staticCompositionLocalOf { Shapes() }