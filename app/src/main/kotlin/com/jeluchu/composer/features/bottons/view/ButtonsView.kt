package com.jeluchu.composer.features.bottons.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.CatalogFixtures
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
import com.jeluchu.jchucomponents.ui.composables.button.JchuProgressButton
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius

@Composable
fun ButtonsView(onItemClick: (String) -> Unit) {
    Buttons(onItemClick)
}

@Composable
private fun Buttons(onItemClick: (String) -> Unit) =
    ScaffoldStructure(
        title = Names.buttons,
        colors =
            CenterToolbarColors(
                containerColor = secondary,
                contentColor = milky
            ),
        onNavIconClick = { onItemClick(DestinationsIds.back) }
    ) {
        var interactiveLoading by remember { mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen16)
        ) {
            Text(
                text = "Progress buttons",
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content
            )

            val states =
                CatalogFixtures.progressButtonStateFixtures.map { it.state } +
                    CatalogFixtures.progressButtonStates.first().copy(
                        title = "Interactive",
                        isLoading = interactiveLoading
                    )

            states.forEach { state ->
                JchuProgressButton(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Check,
                    onClick = {
                        if (state.title == "Interactive") {
                            interactiveLoading = !interactiveLoading
                        }
                    }
                )
            }

            Text(
                text = "Other buttons",
                modifier = Modifier.padding(top = JchuCatalogTheme.spacing.dimen08),
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content
            )

            MenuOptions.buttons.forEach { option ->
                SimpleButton(
                    modifier =
                        Modifier
                            .clip(10.cornerRadius())
                            .background(JchuCatalogTheme.colors.primary.copy(.7f)),
                    label = option.name,
                    color = Color.DarkGray
                ) { onItemClick(option.id) }
            }
        }
    }

@Preview(name = "Buttons - Light", showBackground = true)
@Composable
private fun ButtonsLightPreview() {
    JeluchuTheme {
        Buttons(onItemClick = {})
    }
}

@Preview(
    name = "Buttons - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ButtonsDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            Buttons(onItemClick = {})
        }
    }
}

@Preview(
    name = "Buttons - Accessibility",
    showBackground = true,
    fontScale = 1.5f
)
@Composable
private fun ButtonsAccessibilityPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.highContrast()) {
            Buttons(onItemClick = {})
        }
    }
}
