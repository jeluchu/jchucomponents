package com.jeluchu.composer.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun JeluchuTheme(
    content: @Composable () -> Unit
) = MaterialTheme(
    typography = VisbyTypography,
    content = content
)