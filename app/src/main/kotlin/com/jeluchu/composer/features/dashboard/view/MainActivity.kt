/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.composer.features.dashboard.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jeluchu.composer.core.ui.navigation.Navigation
import com.jeluchu.composer.features.navigationcodegen.di.animeModule
import org.koin.compose.KoinApplication
import com.jeluchu.composer.core.ui.theme.JeluchuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinApplication(
                application = {
                    modules(animeModule)
                }
            ) {
                JeluchuTheme {
                    Navigation()
                }
            }
        }
    }
}
