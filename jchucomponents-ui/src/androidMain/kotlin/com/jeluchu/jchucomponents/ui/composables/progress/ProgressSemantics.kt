package com.jeluchu.jchucomponents.ui.composables.progress

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics

internal fun Modifier.progressSemantics(
    value: Float,
    maxValue: Float,
    enabled: Boolean
): Modifier {
    val semanticMax = maxValue.takeIf { it > 0f } ?: 1f
    val semanticValue = value.coerceIn(0f, semanticMax)

    return semantics(mergeDescendants = true) {
        progressBarRangeInfo =
            ProgressBarRangeInfo(
                current = semanticValue,
                range = 0f..semanticMax
            )
        if (!enabled) disabled()
    }
}
