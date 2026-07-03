package com.jeluchu.composer.features.bottons.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.jeluchu.jchucomponents.ui.composables.button.JchuFloatingButton
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors

@Composable
fun FloatingButtonView(onItemClick: (String) -> Unit) {
    FloatingButtonCatalog(onItemClick)
}

@Composable
private fun FloatingButtonCatalog(
    onItemClick: (String) -> Unit
) = ScaffoldStructure(
    title = Names.floatingButtons,
    colors = CenterToolbarColors(
        containerColor = secondary,
        contentColor = milky
    ),
    onNavIconClick = { onItemClick(DestinationsIds.back) }
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen16)
    ) {
        Text(
            text = "Floating buttons",
            style = JchuCatalogTheme.typography.section,
            color = JchuCatalogTheme.colors.content,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = JchuCatalogTheme.spacing.dimen08),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            CatalogFixtures.floatingButtonFixtures.forEach { fixture ->
                JchuFloatingButton(
                    enabled = fixture.isEnabled,
                    size = fixture.size,
                    contentDescription = fixture.contentDescription,
                )
            }
        }
    }
}

@Preview(name = "Floating Buttons - Light", showBackground = true)
@Composable
private fun FloatingButtonsLightPreview() {
    JeluchuTheme {
        FloatingButtonCatalog(onItemClick = {})
    }
}

@Preview(
    name = "Floating Buttons - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun FloatingButtonsDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            FloatingButtonCatalog(onItemClick = {})
        }
    }
}

@Preview(
    name = "Floating Buttons - Accessibility",
    showBackground = true,
    fontScale = 1.5f
)
@Composable
private fun FloatingButtonsAccessibilityPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.highContrast()) {
            FloatingButtonCatalog(onItemClick = {})
        }
    }
}
