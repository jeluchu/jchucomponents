package com.jeluchu.composer.features.progress.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.composer.core.commons.models.MenuOptions
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.composables.SimpleButton
import com.jeluchu.composer.core.ui.theme.JeluchuTheme
import com.jeluchu.composer.core.ui.theme.milky
import com.jeluchu.composer.core.ui.theme.secondary
import com.jeluchu.composer.core.utils.DestinationsIds
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.ui.accompanist.systemui.SystemStatusBarColors
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors

@Composable
fun ProgressView(onItemClick: (String) -> Unit) {
    SystemStatusBarColors(
        systemBarsColor = secondary,
        statusBarColor = secondary
    )

    Progress(onItemClick)
}

@Composable
private fun Progress(
    onItemClick: (String) -> Unit
) = ScaffoldStructure(
    title = Names.progress,
    colors = CenterToolbarColors(
        containerColor = secondary,
        contentColor = milky
    ),
    onNavIconClick = { onItemClick(DestinationsIds.back) }
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen12)
    ) {
        MenuOptions.progress.forEach { option ->
            SimpleButton(
                modifier = Modifier
                    .clip(JchuCatalogTheme.shapes.corner10)
                    .background(JchuCatalogTheme.colors.primary.copy(.7f)),
                label = option.name,
                color = Color.DarkGray
            ) { onItemClick(option.id) }
        }
    }
}

@Preview(name = "Progress - Light", showBackground = true)
@Composable
private fun ProgressLightPreview() {
    JeluchuTheme {
        Progress(onItemClick = {})
    }
}

@Preview(
    name = "Progress - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ProgressDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            Progress(onItemClick = {})
        }
    }
}

@Preview(
    name = "Progress - Accessibility",
    showBackground = true,
    fontScale = 1.5f
)
@Composable
private fun ProgressAccessibilityPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.highContrast()) {
            Progress(onItemClick = {})
        }
    }
}
