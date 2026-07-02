package com.jeluchu.composer.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.jchucomponents.ui.theme.Shapes
import com.jeluchu.jchucomponents.ui.theme.Spacing

@Composable
fun JeluchuTheme(
    spacing: Spacing = Spacing(),
    shapes: Shapes = Shapes(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = VisbyTypography
    ) {
        ProvideJchuCatalogTheme(
            spacing = spacing,
            shapes = shapes,
            content = content
        )
    }
}
