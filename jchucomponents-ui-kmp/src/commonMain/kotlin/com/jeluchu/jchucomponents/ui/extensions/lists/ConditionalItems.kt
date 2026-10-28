package com.jeluchu.jchucomponents.ui.extensions.lists

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable

/**
 * Adds an item to a lazy list only when [condition] is true.
 */
fun LazyListScope.itemIf(
    condition: Boolean,
    key: Any? = null,
    contentType: Any? = null,
    content: @Composable LazyItemScope.() -> Unit
) {
    if (condition) {
        item(
            key = key,
            contentType = contentType,
            content = content
        )
    }
}

/**
 * Adds an item to a lazy grid only when [condition] is true.
 */
fun LazyGridScope.itemIf(
    condition: Boolean,
    key: Any? = null,
    contentType: Any? = null,
    span: (LazyGridItemSpanScope.() -> GridItemSpan)? = null,
    content: @Composable LazyGridItemScope.() -> Unit
) {
    if (condition) {
        item(
            key = key,
            span = span,
            contentType = contentType,
            content = content
        )
    }
}
