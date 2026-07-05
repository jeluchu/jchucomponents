package com.jeluchu.composer.features.loaders.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.CatalogFixtures
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.JeluchuTheme
import com.jeluchu.jchucomponents.ui.composables.loaders.CircularLoading
import com.jeluchu.jchucomponents.ui.composables.loaders.DotsLoading
import com.jeluchu.jchucomponents.ui.composables.loaders.PulseLoading

@Composable
fun LoadersView(onBack: () -> Unit) {
    LoadersCatalog(onBack)
}

@Composable
private fun LoadersCatalog(onBack: () -> Unit) {
    val fixtures = CatalogFixtures.loaderFixtures

    ScaffoldStructure(
        title = "Loaders",
        onNavIconClick = onBack
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen16)
        ) {
            Text(
                text = fixtures[0].name,
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(96.dp)
            ) {
                CircularLoading(
                    isShow = true,
                    colorLoading = JchuCatalogTheme.colors.primary
                )
            }

            Text(
                text = fixtures[1].name,
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(96.dp),
                contentAlignment = Alignment.Center
            ) {
                DotsLoading(
                    modifier = Modifier.height(48.dp),
                    loaderColor = JchuCatalogTheme.colors.primary
                )
            }

            Text(
                text = fixtures[2].name,
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(160.dp)
            ) {
                PulseLoading(
                    maxPulseSize = 140f,
                    minPulseSize = 40f,
                    pulseColor = JchuCatalogTheme.colors.primary.copy(alpha = .18f),
                    centreColor = JchuCatalogTheme.colors.primary
                )
            }
        }
    }
}

@Preview(name = "Loaders - Light", showBackground = true)
@Composable
private fun LoadersLightPreview() {
    JeluchuTheme {
        LoadersCatalog(onBack = {})
    }
}

@Preview(
    name = "Loaders - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun LoadersDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            LoadersCatalog(onBack = {})
        }
    }
}

@Preview(
    name = "Loaders - Accessibility",
    showBackground = true,
    fontScale = 1.5f
)
@Composable
private fun LoadersAccessibilityPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.highContrast()) {
            LoadersCatalog(onBack = {})
        }
    }
}
