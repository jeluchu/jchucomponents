package com.jeluchu.composer.features.layouts.view

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.JeluchuTheme
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.ui.composables.column.JchuScrollableColumn

@Composable
fun LayoutsView(onBack: () -> Unit) {
    LayoutsCatalog(onBack)
}

@Composable
private fun LayoutsCatalog(onBack: () -> Unit) {
    ScaffoldStructure(
        title = Names.layouts,
        onNavIconClick = onBack
    ) {
        Text(
            text = "Scrollable column",
            style = JchuCatalogTheme.typography.section,
            color = JchuCatalogTheme.colors.content
        )
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(JchuCatalogTheme.spacing.dimen240)
                    .border(
                        width = JchuCatalogTheme.spacing.dimen01,
                        color = JchuCatalogTheme.colors.content
                    )
        ) {
            JchuScrollableColumn(
                modifier = Modifier.padding(JchuCatalogTheme.spacing.dimen16),
                verticalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen12)
            ) {
                repeat(20) { index ->
                    Text(
                        text = "Scrollable item ${index + 1}",
                        color = JchuCatalogTheme.colors.content
                    )
                }
            }
        }
    }
}

@Preview(name = "Layouts - Light", showBackground = true)
@Composable
private fun LayoutsLightPreview() {
    JeluchuTheme {
        LayoutsCatalog(onBack = {})
    }
}

@Preview(
    name = "Layouts - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun LayoutsDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            LayoutsCatalog(onBack = {})
        }
    }
}

@Preview(
    name = "Layouts - Accessibility",
    showBackground = true,
    fontScale = 1.5f
)
@Composable
private fun LayoutsAccessibilityPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.highContrast()) {
            LayoutsCatalog(onBack = {})
        }
    }
}
