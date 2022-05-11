/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.ui.override

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner

/**
 *
 * Author: @Jeluchu
 *
 * This is a compostable that replaces the former OnBackPressed
 *
 * @param enabled To activate this event (e.g. when pressing twice) it
 * is always activated by default [Boolean]
 *
 * @param onBack to include the action you want to be performed after
 * clicking backwards using navigation bar [Unit]
 *
 */

@Composable
fun OnBackPressed(
    enabled: Boolean = true,
    onBack: () -> Unit
) {

    val currentOnBack by rememberUpdatedState(onBack)
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }

    SideEffect { backCallback.isEnabled = enabled }

    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose { backCallback.remove() }
    }

}