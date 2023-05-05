package com.jeluchu.jchucomponents.extensions.flow

import com.jeluchu.jchucomponents.core.exception.Failure
import com.jeluchu.jchucomponents.utils.network.models.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

fun <T> Flow<T>.flowCollector(scope: CoroutineScope, onExecute: (T) -> Unit) {
    scope.launch { runCatching { collect { onExecute(it) } } }
}

fun <T, S> Flow<Resource<Failure, T>>.flowResourceCollector(
    scope: CoroutineScope,
    initialValue: S,
    onLoading: () -> Unit,
    onSuccess: (T?) -> Unit,
    onFailure: (Failure?) -> Unit
) = this
    .onStart { onLoading() }
    .onEach {
        when (it) {
            is Resource.Success -> onSuccess(it.data)
            is Resource.Loading -> onLoading()
            is Resource.Error -> onFailure(it.error)
        }
    }
    .stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = initialValue
    )
    .launchIn(scope)