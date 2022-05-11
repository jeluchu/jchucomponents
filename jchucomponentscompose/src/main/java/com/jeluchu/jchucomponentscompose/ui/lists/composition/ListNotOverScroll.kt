package com.jeluchu.jchucomponentscompose.ui.lists.composition

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListNotOverScroll(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalOverScrollConfiguration provides null
    ) { content() }
}