package com.jeluchu.composer.features.text.view

import android.content.res.Configuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.jeluchu.jchucomponents.navigation3.BackStackBack
import com.jeluchu.jchucomponents.navigation3.Screen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.JeluchuTheme
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.ui.composables.text.JchuExpandableText
import com.jeluchu.jchucomponents.ui.composables.text.JchuExpandableDescriptionConfig
import com.jeluchu.jchucomponents.ui.composables.text.JchuSimpleExpandableText

@Composable
@Screen(graph = "Text")
fun TextView(@BackStackBack onBack: () -> Unit) {
    TextCatalog(onBack)
}

@Composable
private fun TextCatalog(onBack: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ScaffoldStructure(
        title = Names.text,
        onNavIconClick = onBack
    ) {
        Text(
            text = "Expandable text",
            style = JchuCatalogTheme.typography.section,
            color = JchuCatalogTheme.colors.content
        )
        JchuExpandableText(
            text =
                "JchuExpandableText keeps its state in the caller. ".repeat(8) +
                    "It contains no networking, images or application-specific formatting.",
            expanded = expanded,
            onExpandedChange = { expanded = it },
            color = JchuCatalogTheme.colors.content
        )
        Text(
            text = "iNook expandable-description fidelity",
            style = JchuCatalogTheme.typography.section,
            color = JchuCatalogTheme.colors.content
        )
        JchuSimpleExpandableText(
            description =
                "This variant keeps the original internal state, fade, gradient, text " +
                    "selection and disclosure-arrow behavior. ".repeat(4),
            config = JchuExpandableDescriptionConfig(enableHapticFeedback = false)
        )
    }
}

@Preview(name = "Text - Light", showBackground = true)
@Composable
private fun TextLightPreview() {
    JeluchuTheme {
        TextCatalog(onBack = {})
    }
}

@Preview(
    name = "Text - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TextDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            TextCatalog(onBack = {})
        }
    }
}

@Preview(
    name = "Text - Accessibility",
    showBackground = true,
    fontScale = 1.5f
)
@Composable
private fun TextAccessibilityPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.highContrast()) {
            TextCatalog(onBack = {})
        }
    }
}
