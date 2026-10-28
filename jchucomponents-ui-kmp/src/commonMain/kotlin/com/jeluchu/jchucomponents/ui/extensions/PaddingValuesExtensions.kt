package com.jeluchu.jchucomponents.ui.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * Adds the same horizontal spacing to both logical sides while preserving the
 * existing insets and vertical padding.
 */
fun PaddingValues.withHorizontalPadding(
    horizontalPadding: Dp,
    layoutDirection: LayoutDirection
): PaddingValues =
    PaddingValues(
        start = calculateStartPadding(layoutDirection) + horizontalPadding,
        top = calculateTopPadding(),
        end = calculateEndPadding(layoutDirection) + horizontalPadding,
        bottom = calculateBottomPadding()
    )
