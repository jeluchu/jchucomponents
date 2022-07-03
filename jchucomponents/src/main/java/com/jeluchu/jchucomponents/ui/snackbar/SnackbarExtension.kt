/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.snackbar

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
fun SnackbarController.showSnack(
    scaffoldState: ScaffoldState,
    message: String
) {
    getScope().launch {
        showSnackbar(
            scaffoldState = scaffoldState,
            message = message
        )
    }
}