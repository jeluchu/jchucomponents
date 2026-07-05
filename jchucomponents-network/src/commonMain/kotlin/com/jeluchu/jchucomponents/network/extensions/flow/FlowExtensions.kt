package com.jeluchu.jchucomponents.network.extensions.flow

import com.jeluchu.jchucomponents.network.models.Failure
import com.jeluchu.jchucomponents.network.models.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

fun <T> Flow<T>.flowCollector(
    scope: CoroutineScope,
    onExecute: (T) -> Unit,
) {
    scope.launch { runCatching { collect { onExecute(it) } } }
}

fun <T, S> Flow<Resource<Failure, T>>.flowResourceCollector(
    initialValue: S,
    scope: CoroutineScope,
    onLoading: () -> Unit,
    onSuccess: (T?) -> Unit,
    onFailure: (Failure?) -> Unit,
) = this
    .onStart { onLoading() }
    .onEach {
        when (it) {
            is Resource.Success -> onSuccess(it.data)
            is Resource.Loading -> onLoading()
            is Resource.Error -> onFailure(it.error)
        }
    }.stateIn(
        scope = scope,
        initialValue = initialValue,
        started = SharingStarted.WhileSubscribed(),
    ).launchIn(scope)
