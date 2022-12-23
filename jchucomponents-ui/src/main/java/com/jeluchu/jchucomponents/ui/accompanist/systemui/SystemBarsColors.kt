/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.accompanist.systemui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.SystemUiController
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
    statusBarColor: Color,
    systemBarsColor: Color,
    useDarkIcons: Boolean = MaterialTheme.colors.isLight,
    systemUiController: SystemUiController = rememberSystemUiController()
) = SideEffect {

    systemUiController.setSystemBarsColor(
        color = systemBarsColor,
        darkIcons = useDarkIcons
    )

    systemUiController.setStatusBarColor(
        color = statusBarColor,
        darkIcons = useDarkIcons
    )

}
