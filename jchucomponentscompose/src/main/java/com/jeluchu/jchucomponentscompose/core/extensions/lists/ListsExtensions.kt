package com.jeluchu.jchucomponentscompose.core.extensions.lists

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch

/** Await indefinitely, blocking scrolls **/
fun LazyListState.disableScrolling(scope: CoroutineScope) {
    scope.launch {
        scroll(scrollPriority = MutatePriority.PreventUserInput) {
            awaitCancellation()
        }
    }
}

/** Cancel the previous indefinite "scroll" blocking **/
fun LazyListState.reenableScrolling(scope: CoroutineScope) {
    scope.launch {
        scroll(scrollPriority = MutatePriority.PreventUserInput) {}
    }
}