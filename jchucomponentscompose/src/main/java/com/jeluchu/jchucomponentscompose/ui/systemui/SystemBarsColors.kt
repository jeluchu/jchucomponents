/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.ui.systemui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 *
 * Author: @Jeluchu
 *
 * This component makes adjustments
 * to the system interface of the cell phone
 *
 * @param systemBarsColor change the color of the system navigation bar
 * @param statusBarColor change the color of the system notification bar
 *
 */

@Composable
fun SystemStatusBarColors(
    systemBarsColor: Color,
    statusBarColor: Color,
    useDarkIcons: Boolean = MaterialTheme.colors.isLight
) {

    val systemUiController = rememberSystemUiController()

    SideEffect {

        systemUiController.setSystemBarsColor(
            color = systemBarsColor,
            darkIcons = useDarkIcons
        )

        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = useDarkIcons
        )

    }

}