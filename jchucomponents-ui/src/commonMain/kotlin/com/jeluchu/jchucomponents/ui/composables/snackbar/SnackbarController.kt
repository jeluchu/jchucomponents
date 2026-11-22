/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.snackbar

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Shows snackbars one at a time: a new one replaces the one currently on screen.
 *
 * Pass the [SnackbarHostState] of the scaffold that hosts the snackbars, for example the one given to
 * `Scaffold(snackbarHost = ...)` or `BottomSheetScaffoldState.snackbarHostState`.
 */
class SnackbarController(
    private val scope: CoroutineScope
) {
    private var snackbarJob: Job? = null

    init {
        cancelActiveJob()
    }

    fun getScope() = scope

    fun showSnackbar(
        snackbarHostState: SnackbarHostState,
        message: String,
        actionLabel: String = ""
    ) {
        cancelActiveJob()
        snackbarJob =
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel.takeIf { it.isNotEmpty() }
                )
                cancelActiveJob()
            }
    }

    private fun cancelActiveJob() {
        snackbarJob?.let { job ->
            job.cancel()
            snackbarJob = Job()
        }
    }
}
