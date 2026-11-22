/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.snackbar

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.launch

fun SnackbarController.showSnack(
    snackbarHostState: SnackbarHostState,
    message: String
) {
    getScope().launch {
        showSnackbar(
            snackbarHostState = snackbarHostState,
            message = message
        )
    }
}
