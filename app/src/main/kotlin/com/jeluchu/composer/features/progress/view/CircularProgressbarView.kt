package com.jeluchu.composer.features.progress.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.CatalogFixtures
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.JeluchuTheme
import com.jeluchu.composer.core.ui.theme.milky
import com.jeluchu.composer.core.ui.theme.secondary
import com.jeluchu.composer.core.utils.DestinationsIds
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.ui.composables.progress.JchuCircularProgress
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors

@Composable
fun CircularProgressbarView(onItemClick: (String) -> Unit) {
    CircularProgressbar(onItemClick)
}

@Composable
private fun CircularProgressbar(onItemClick: (String) -> Unit) {
    val fixtures = CatalogFixtures.progressStateFixtures

    ScaffoldStructure(
        title = Names.circularProgress,
        colors = CenterToolbarColors(
            containerColor = secondary,
            contentColor = milky
        ),
        onNavIconClick = { onItemClick(DestinationsIds.back) }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen16)
        ) {
            fixtures.forEach { fixture ->
                Text(
                    text = fixture.name,
                    style = JchuCatalogTheme.typography.section,
                    color = JchuCatalogTheme.colors.content,
                )
                JchuCircularProgress(
                    state = fixture.state,
                    icon = Icons.Default.Check,
                )
            }
        }
    }
}

@Preview(name = "Circular progress - Light", showBackground = true)
@Composable
private fun CircularProgressLightPreview() {
    JeluchuTheme {
        CircularProgressbar(onItemClick = {})
    }
}

@Preview(
    name = "Circular progress - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CircularProgressDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            CircularProgressbar(onItemClick = {})
        }
    }
}

@Preview(
    name = "Circular progress - Accessibility",
    showBackground = true,
    fontScale = 1.5f
)
@Composable
private fun CircularProgressAccessibilityPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.highContrast()) {
            CircularProgressbar(onItemClick = {})
        }
    }
}
