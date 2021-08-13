package com.jeluchu.jchucomponentscompose.ui.systemui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SystemStatusBarColors(
    systemBarsColor: Color,
    statusBarColor: Color
) {

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight

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