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

/** Adds a lazy-list item with an explicit stable key and content type. */
fun LazyListScope.element(
    key: Any,
    contentType: Any?,
    content: @Composable LazyItemScope.() -> Unit,
) {
    item(
        key = key,
        contentType = contentType,
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

fun LazyListScope.elementIf(
    condition: Boolean,
    key: Any,
    contentType: Any?,
    ifContent: @Composable LazyItemScope.() -> Unit,
    elseContent: @Composable LazyItemScope.() -> Unit,
) {
    if (condition) element(key, contentType, ifContent) else element(key, contentType, elseContent)
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

/** Adds a sticky header with an explicit stable key and content type. */
@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.header(
    key: Any,
    contentType: Any?,
    content: @Composable LazyItemScope.(Int) -> Unit,
) {
    stickyHeader(
        key = key,
        contentType = contentType,
        content = content,
    )
}
