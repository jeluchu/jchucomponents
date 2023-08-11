package com.jeluchu.composer

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.jeluchu.jchucomponents.ui.accompanist.systemui.SystemStatusBarColors
import com.jeluchu.jchucomponents.ui.themes.darkPastelBlue

@Composable
fun MainView() {
    SystemStatusBarColors(
        systemBarsColor = darkPastelBlue,
        statusBarColor = darkPastelBlue
    )

    Main()
}

@Composable
fun Main() {
    LazyColumn {

    }
}