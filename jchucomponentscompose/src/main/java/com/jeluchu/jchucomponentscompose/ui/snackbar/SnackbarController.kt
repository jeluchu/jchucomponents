/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.ui.snackbar

import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
class SnackbarController
constructor(
    private val scope: CoroutineScope
) {

    private var snackbarJob: Job? = null

    init {
        cancelActiveJob()
    }

    fun getScope() = scope

    fun showSnackbar(
        scaffoldState: ScaffoldState,
        message: String,
        actionLabel: String = String.empty()
    ) {
        if (snackbarJob == null) {
            snackbarJob = scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel
                )
                cancelActiveJob()
            }
        } else {
            cancelActiveJob()
            snackbarJob = scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel
                )
                cancelActiveJob()
            }
        }
    }

    fun showSnackbar(
        scaffoldState: BottomSheetScaffoldState,
        message: String,
        actionLabel: String = String.empty()
    ) {
        if (snackbarJob == null) {
            snackbarJob = scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel
                )
                cancelActiveJob()
            }
        } else {
            cancelActiveJob()
            snackbarJob = scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel
                )
                cancelActiveJob()
            }
        }
    }

    private fun cancelActiveJob() {
        snackbarJob?.let { job ->
            job.cancel()
            snackbarJob = Job()
        }
    }
}