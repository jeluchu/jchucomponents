package com.jeluchu.composer.features.inputs.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.CatalogFixtures
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.JeluchuTheme
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.ui.composables.textfields.CountField
import com.jeluchu.jchucomponents.ui.composables.textfields.GrowingTextFieldDefaults
import com.jeluchu.jchucomponents.ui.composables.textfields.JchuCountedField
import com.jeluchu.jchucomponents.ui.composables.textfields.JchuExpandableSearch
import com.jeluchu.jchucomponents.ui.composables.textfields.JchuGrowingTextField
import com.jeluchu.jchucomponents.ui.composables.textfields.JchuSearchField
import com.jeluchu.jchucomponents.ui.composables.textfields.SearchField
import com.jeluchu.jchucomponents.ui.composables.textfields.SearchBarDefaults

@Composable
fun InputsView(onBack: () -> Unit) {
    InputsCatalog(onBack)
}

@Composable
private fun InputsCatalog(onBack: () -> Unit) {
    val fixtures = CatalogFixtures.inputFixtures
    val searchState = remember { mutableStateOf("") }
    val countedState = remember { mutableStateOf("") }
    val expandableSearchState = remember { mutableStateOf("") }
    val notesState = remember { mutableStateOf("") }

    ScaffoldStructure(
        title = Names.inputs,
        onNavIconClick = onBack
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                JchuCatalogTheme.spacing.dimen16
            )
        ) {
            Text(
                text = fixtures[0].name,
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content,
            )
            JchuSearchField(
                value = searchState.value,
                onValueChange = { searchState.value = it },
                labelText = fixtures[0].label,
                searchField = SearchField(
                    labelColor = JchuCatalogTheme.colors.content,
                    contentColor = JchuCatalogTheme.colors.content,
                    backgroundColor = JchuCatalogTheme.colors.surface,
                )
            )

            Text(
                text = fixtures[1].name,
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content,
            )
            JchuCountedField(
                title = fixtures[1].label,
                value = countedState.value,
                onValueChange = { countedState.value = it },
                maxLength = requireNotNull(fixtures[1].maxLength),
                countField = CountField(
                    cursorColor = JchuCatalogTheme.colors.content,
                    counterTextColor = JchuCatalogTheme.colors.content,
                    disabledLabelColor = JchuCatalogTheme.colors.content,
                    backgroundColor = JchuCatalogTheme.colors.surface,
                )
            )

            Text(
                text = fixtures[2].name,
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content,
            )
            JchuExpandableSearch(
                query = expandableSearchState.value,
                onQueryChange = { expandableSearchState.value = it },
                defaults = SearchBarDefaults(
                    label = fixtures[2].label,
                    containerColor = JchuCatalogTheme.colors.surface,
                    contentColor = JchuCatalogTheme.colors.content,
                )
            )

            Text(
                text = fixtures[3].name,
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content,
            )
            JchuGrowingTextField(
                value = notesState.value,
                onValueChange = { notesState.value = it },
                defaults = GrowingTextFieldDefaults(
                    label = fixtures[3].label,
                    placeholder = "Describe the component",
                    maxCharacters = fixtures[3].maxLength,
                )
            )

            Text(
                text = fixtures[4].label,
                style = JchuCatalogTheme.typography.body,
                color = JchuCatalogTheme.colors.content,
            )
        }
    }
}

@Preview(name = "Inputs - Light", showBackground = true)
@Composable
private fun InputsLightPreview() {
    JeluchuTheme {
        InputsCatalog(onBack = {})
    }
}

@Preview(
    name = "Inputs - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun InputsDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            InputsCatalog(onBack = {})
        }
    }
}

@Preview(
    name = "Inputs - Accessibility",
    showBackground = true,
    fontScale = 1.5f
)
@Composable
private fun InputsAccessibilityPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.highContrast()) {
            InputsCatalog(onBack = {})
        }
    }
}
