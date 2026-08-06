package com.jeluchu.composer.features.inputs.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.CatalogFixtures
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.JeluchuTheme
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.ui.composables.textfields.CountField
import com.jeluchu.jchucomponents.ui.composables.colors.JchuColorOption
import com.jeluchu.jchucomponents.ui.composables.colors.JchuColorPicker
import com.jeluchu.jchucomponents.ui.composables.colors.JchuINookColorPicker
import com.jeluchu.jchucomponents.ui.composables.textfields.GrowingTextFieldDefaults
import com.jeluchu.jchucomponents.ui.composables.textfields.JchuCountedField
import com.jeluchu.jchucomponents.ui.composables.textfields.JchuExpandableSearch
import com.jeluchu.jchucomponents.ui.composables.textfields.JchuGroupedVisualTransformation
import com.jeluchu.jchucomponents.ui.composables.textfields.JchuGrowingTextField
import com.jeluchu.jchucomponents.ui.composables.textfields.JchuSearchField
import com.jeluchu.jchucomponents.ui.composables.textfields.SearchBarDefaults
import com.jeluchu.jchucomponents.ui.composables.textfields.SearchField

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
    val groupedCodeState = remember { mutableStateOf("123456789012") }
    val selectedColorState = remember { mutableStateOf("green") }

    ScaffoldStructure(
        title = Names.inputs,
        onNavIconClick = onBack
    ) {
        Column(
            verticalArrangement =
                Arrangement.spacedBy(
                    JchuCatalogTheme.spacing.dimen16
                )
        ) {
            Text(
                text = fixtures[0].name,
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content
            )
            JchuSearchField(
                value = searchState.value,
                onValueChange = { searchState.value = it },
                labelText = fixtures[0].label,
                searchField =
                    SearchField(
                        labelColor = JchuCatalogTheme.colors.content,
                        contentColor = JchuCatalogTheme.colors.content,
                        backgroundColor = JchuCatalogTheme.colors.surface
                    )
            )

            Text(
                text = fixtures[1].name,
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content
            )
            JchuCountedField(
                title = fixtures[1].label,
                value = countedState.value,
                onValueChange = { countedState.value = it },
                maxLength = requireNotNull(fixtures[1].maxLength),
                countField =
                    CountField(
                        cursorColor = JchuCatalogTheme.colors.content,
                        counterTextColor = JchuCatalogTheme.colors.content,
                        disabledLabelColor = JchuCatalogTheme.colors.content,
                        backgroundColor = JchuCatalogTheme.colors.surface
                    )
            )

            Text(
                text = fixtures[2].name,
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content
            )
            JchuExpandableSearch(
                query = expandableSearchState.value,
                onQueryChange = { expandableSearchState.value = it },
                defaults =
                    SearchBarDefaults(
                        label = fixtures[2].label,
                        containerColor = JchuCatalogTheme.colors.surface,
                        contentColor = JchuCatalogTheme.colors.content
                    )
            )

            Text(
                text = fixtures[3].name,
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content
            )
            JchuGrowingTextField(
                value = notesState.value,
                onValueChange = { notesState.value = it },
                defaults =
                    GrowingTextFieldDefaults(
                        label = fixtures[3].label,
                        placeholder = "Describe the component",
                        maxCharacters = fixtures[3].maxLength
                    )
            )

            Text(
                text = "Grouped visual transformation",
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content
            )
            OutlinedTextField(
                value = groupedCodeState.value,
                onValueChange = { value ->
                    groupedCodeState.value = value.filter(Char::isLetterOrDigit)
                },
                label = { Text("Presentation-only separators") },
                visualTransformation = JchuGroupedVisualTransformation(),
                singleLine = true
            )

            Text(
                text = "Color picker",
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content
            )
            JchuColorPicker(
                options =
                    listOf(
                        JchuColorOption("green", Color(0xFF3D8B5A), "Green"),
                        JchuColorOption("blue", Color(0xFF3976C5), "Blue"),
                        JchuColorOption("orange", Color(0xFFD97832), "Orange"),
                        JchuColorOption("disabled", Color.Gray, "Unavailable", enabled = false)
                    ),
                selectedValue = selectedColorState.value,
                onValueSelected = { selectedColorState.value = it }
            )

            Text(
                text = "iNook color settings fidelity",
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content
            )
            JchuINookColorPicker(
                title = "Phone color",
                description = "Choose the background color used by the phone.",
                colors = listOf("C6E5B1", "E0BBE4", "F8C8DC", "E0BBE4"),
                selectedColor = "C6E5B1",
                darkTheme = false,
                colorResolver = { value ->
                    value?.toLongOrNull(16)?.let { Color(0xFF000000 or it) } ?: Color.Transparent
                },
                onColorSelected = {}
            )

            Text(
                text = fixtures[4].label,
                style = JchuCatalogTheme.typography.body,
                color = JchuCatalogTheme.colors.content
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
