package com.jeluchu.jchucomponents.ui.composables.column

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/** Compatibility wrapper for the pre-3.0 prefixed API. */
@Deprecated(
    message = "Use JchuScrollableColumn",
    replaceWith = ReplaceWith(
        "JchuScrollableColumn(modifier, verticalArrangement, horizontalAlignment, state, content)"
    )
)
@Composable
fun ScrollableColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    state: ScrollState = rememberScrollState(),
    content: @Composable () -> Unit
) = JchuScrollableColumn(
    modifier = modifier,
    verticalArrangement = verticalArrangement,
    horizontalAlignment = horizontalAlignment,
    state = state
) {
    content()
}
