package com.jeluchu.jchucomponents.ui.extensions.lazylist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable

/** Adds a keyed lazy-list item using an enum entry as its stable identity. */
fun LazyListScope.element(
    identifier: Enum<*>,
    content: @Composable LazyItemScope.() -> Unit,
) {
    item(
        key = identifier,
        contentType = identifier::class,
        content = content,
    )
}

fun LazyListScope.elementIf(
    condition: Boolean,
    identifier: Enum<*>,
    ifContent: @Composable LazyItemScope.() -> Unit,
    elseContent: @Composable LazyItemScope.() -> Unit,
) {
    if (condition) element(identifier, ifContent) else element(identifier, elseContent)
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.header(
    identifier: Enum<*>,
    content: @Composable LazyItemScope.(Int) -> Unit,
) {
    stickyHeader(
        key = identifier,
        contentType = identifier::class,
        content = content,
    )
}
