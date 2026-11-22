package com.jeluchu.jchucomponents.ui.composables.structures

import androidx.compose.runtime.Composable

@Composable
fun <T> JchuRemoteScreenContent(
    data: T?,
    isLoading: Boolean,
    error: String?,
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable (T) -> Unit,
    onFailure: @Composable (String?) -> Unit
) {
    when {
        isLoading -> onLoading()
        data != null -> onSuccess(data)
        error != null -> onFailure(error)
        else -> onFailure(null)
    }
}

@Composable
fun JchuRemoteScreenContent(
    isLoading: Boolean,
    error: String?,
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable () -> Unit,
    onFailure: @Composable (String?) -> Unit
) {
    when {
        isLoading -> onLoading()
        error != null -> onFailure(error)
        else -> onSuccess()
    }
}
