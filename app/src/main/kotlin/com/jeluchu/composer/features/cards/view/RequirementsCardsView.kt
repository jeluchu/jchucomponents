package com.jeluchu.composer.features.cards.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.JeluchuTheme
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.ui.composables.cards.JchuINookAmountInfoColors
import com.jeluchu.jchucomponents.ui.composables.cards.JchuINookRequirementsCard
import com.jeluchu.jchucomponents.ui.composables.cards.JchuINookRequirementsCardColors
import com.jeluchu.jchucomponents.ui.composables.chips.JchuAmountCounterColors

@Composable
fun RequirementsCardsView(onBack: () -> Unit) {
    RequirementsCardsCatalog(onBack)
}

@Composable
private fun RequirementsCardsCatalog(onBack: () -> Unit) {
    val colors = JchuINookRequirementsCardColors(
        strokeColor = JchuCatalogTheme.colors.content.copy(alpha = .4f),
        contentColor = JchuCatalogTheme.colors.content,
        containerColor = JchuCatalogTheme.colors.content.copy(alpha = .05f),
        amountColors = JchuAmountCounterColors(
            contentColor = JchuCatalogTheme.colors.content,
            containerColor = JchuCatalogTheme.colors.content.copy(alpha = .1f)
        ),
        amountInfoColors = JchuINookAmountInfoColors(
            contentColor = JchuCatalogTheme.colors.content,
            containerColor = JchuCatalogTheme.colors.content.copy(alpha = .1f)
        )
    )

    ScaffoldStructure(title = Names.requirementsCards, onNavIconClick = onBack) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            JchuINookRequirementsCard(
                modifier = Modifier.weight(1f),
                title = "Customization kit",
                amount = "x2",
                imageVector = Icons.Default.Build,
                colors = colors
            )
            JchuINookRequirementsCard(
                modifier = Modifier.weight(1f),
                title = "Lighting type",
                requirement = "Fluorescent",
                colors = colors
            )
        }
        JchuINookRequirementsCard(
            title = "Appearance",
            amount = "10%",
            colors = colors
        )
    }
}

@Preview(name = "Requirements cards - Light", showBackground = true)
@Composable
private fun RequirementsCardsLightPreview() {
    JeluchuTheme { RequirementsCardsCatalog(onBack = {}) }
}

@Preview(
    name = "Requirements cards - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RequirementsCardsDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            RequirementsCardsCatalog(onBack = {})
        }
    }
}

@Preview(name = "Requirements cards - Accessibility", showBackground = true, fontScale = 1.5f)
@Composable
private fun RequirementsCardsAccessibilityPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.highContrast()) {
            RequirementsCardsCatalog(onBack = {})
        }
    }
}
