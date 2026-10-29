package com.jeluchu.jchucomponents.ui.composables.column

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jeluchu.jchucomponents.ui.foundation.lists.composition.ListNotOverScroll

/**
 * A vertically scrollable column that suppresses the legacy overscroll effect on platforms where
 * it looks outdated (Android below 12).
 *
 * The content keeps its [ColumnScope], so children can use column-specific modifiers such as
 * `weight` and `align`.
 *
 * @param modifier modifier applied before the component fills the available size and scrolls.
 * @param verticalArrangement vertical arrangement of the column's children.
 * @param horizontalAlignment horizontal alignment of the column's children.
 * @param state state used to control or observe the scroll position.
 * @param content content displayed inside the column.
 */
@Composable
fun JchuScrollableColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    state: ScrollState = rememberScrollState(),
    content: @Composable ColumnScope.() -> Unit
) {
    val column: @Composable () -> Unit = {
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .verticalScroll(state),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content
        )
    }

    if (shouldSuppressLegacyOverscroll()) {
        ListNotOverScroll(content = column)
    } else {
        column()
    }
}

/** Whether the platform still draws the legacy stretch-less overscroll glow. */
internal expect fun shouldSuppressLegacyOverscroll(): Boolean
