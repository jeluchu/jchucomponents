package com.jeluchu.composer.features.surfaces.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.JeluchuTheme
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.ui.composables.sheets.JchuAnimatedBottomSheet

@Composable
fun SurfacesView(onBack: () -> Unit) {
    SurfacesCatalog(onBack)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SurfacesCatalog(onBack: () -> Unit) {
    var sheetValue by remember { mutableStateOf<String?>(null) }

    ScaffoldStructure(
        title = Names.surfaces,
        onNavIconClick = onBack
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen16)) {
            Text(
                text = "Animated bottom sheet",
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content
            )
            Button(onClick = { sheetValue = "Content remains available while closing" }) {
                Text("Show bottom sheet")
            }
        }
    }

    JchuAnimatedBottomSheet(
        value = sheetValue,
        onDismissRequest = { sheetValue = null }
    ) { message ->
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(JchuCatalogTheme.spacing.dimen24),
            verticalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen16)
        ) {
            Text(message)
            Button(onClick = { sheetValue = null }) {
                Text("Close")
            }
        }
    }
}

@Preview(name = "Surfaces - Light", showBackground = true)
@Composable
private fun SurfacesLightPreview() {
    JeluchuTheme {
        SurfacesCatalog(onBack = {})
    }
}

@Preview(
    name = "Surfaces - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SurfacesDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            SurfacesCatalog(onBack = {})
        }
    }
}

@Preview(
    name = "Surfaces - Accessibility",
    showBackground = true,
    fontScale = 1.5f
)
@Composable
private fun SurfacesAccessibilityPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.highContrast()) {
            SurfacesCatalog(onBack = {})
        }
    }
}
