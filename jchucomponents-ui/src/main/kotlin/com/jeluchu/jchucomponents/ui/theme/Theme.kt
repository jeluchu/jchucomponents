@file:Suppress("ktlint:standard:filename")

package com.jeluchu.jchucomponents.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object JchuTheme {
    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current

    val spacing: Spacing
        @Composable
        get() = LocalSpacing.current
}
