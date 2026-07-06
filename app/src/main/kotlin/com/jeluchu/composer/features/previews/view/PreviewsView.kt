package com.jeluchu.composer.features.previews.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.JeluchuTheme
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.ui.composables.dropdown.DropdownItem
import com.jeluchu.jchucomponents.ui.composables.dropdown.DropdownItemOption
import com.jeluchu.jchucomponents.ui.composables.pager.PageIndicator
import com.jeluchu.jchucomponents.ui.composables.preferences.PreferenceItem
import com.jeluchu.jchucomponents.ui.composables.preferences.PreferenceSingleChoiceItem
import com.jeluchu.jchucomponents.ui.composables.preferences.PreferenceSwitch
import com.jeluchu.jchucomponents.ui.composables.shimmer.MiniPostItemShimmer
import com.jeluchu.jchucomponents.ui.composables.shimmer.MovieItemShimmer
import com.jeluchu.jchucomponents.ui.composables.snackbar.SnackbarCustomized
import com.jeluchu.jchucomponents.ui.composables.tabs.ChipTab
import com.jeluchu.jchucomponents.ui.composables.tabs.ScrollableChipTabRow

@Composable
fun PreviewsView(onBack: () -> Unit) {
    PreviewsCatalog(onBack)
}

@Composable
private fun PreviewsCatalog(onBack: () -> Unit) {
    val shimmerColors =
        listOf(
            JchuCatalogTheme.colors.surface,
            JchuCatalogTheme.colors.background,
            JchuCatalogTheme.colors.surface
        )
    val snackHostState = remember { SnackbarHostState() }
    var selectedTab by remember { mutableIntStateOf(1) }
    var selectedOption by remember { mutableStateOf("Daily") }
    var checked by remember { mutableStateOf(true) }

    LaunchedEffect(snackHostState) {
        snackHostState.showSnackbar("Catalog snackbar")
    }

    ScaffoldStructure(
        title = Names.previews,
        onNavIconClick = onBack
    ) {
        CatalogSection("Dropdown") {
            DropdownItem(
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(JchuCatalogTheme.colors.surface),
                onClick = {}
            ) {
                DropdownItemOption(
                    title = "Share component",
                    dropdownItemText =
                        com.jeluchu.jchucomponents.ui.composables.dropdown.DropdownItemText(
                            color = JchuCatalogTheme.colors.content
                        )
                )
            }
        }

        CatalogSection("Preferences") {
            PreferenceItem(
                title = "Catalog updates",
                description = "Reusable preference row",
                icon = Icons.Outlined.Notifications,
                containerColor = JchuCatalogTheme.colors.surface,
                contentColor = JchuCatalogTheme.colors.content
            )
            PreferenceSwitch(
                title = "Enable previews",
                description = "Switch row state",
                isChecked = checked,
                containerColor = JchuCatalogTheme.colors.surface,
                contentColor = JchuCatalogTheme.colors.content,
                onClick = { checked = !checked }
            )
            listOf("Daily", "Weekly").forEach { option ->
                PreferenceSingleChoiceItem(
                    text = option,
                    selected = option == selectedOption,
                    colors =
                        com.jeluchu.jchucomponents.ui.composables.preferences.PreferenceChoiceColors(
                            selectedRadioColor = JchuCatalogTheme.colors.accent,
                            unselectedRadioColor = JchuCatalogTheme.colors.content,
                            containerColor = JchuCatalogTheme.colors.surface,
                            contentColor = JchuCatalogTheme.colors.content
                        ),
                    onClick = { selectedOption = option }
                )
            }
        }

        CatalogSection("Tabs") {
            ScrollableChipTabRow(
                selectedTabIndex = selectedTab,
                backgroundColor = JchuCatalogTheme.colors.surface,
                contentColor = JchuCatalogTheme.colors.content
            ) {
                listOf("Overview", "States", "A11y", "Motion").forEachIndexed { index, label ->
                    ChipTab(
                        selected = selectedTab == index,
                        selectedContentColor = JchuCatalogTheme.colors.accent,
                        unselectedContentColor = JchuCatalogTheme.colors.content.copy(alpha = .6f),
                        onClick = { selectedTab = index }
                    ) {
                        Text(
                            text = label,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        CatalogSection("Pager") {
            PageIndicator(
                pagesCount = 5,
                currentPageIndex = selectedTab.coerceAtMost(4),
                color = JchuCatalogTheme.colors.accent
            )
        }

        CatalogSection("Shimmer") {
            Row(horizontalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen08)) {
                MovieItemShimmer(
                    lists = shimmerColors,
                    floatAnim = 500f,
                    isVertical = false
                )
                Column(modifier = Modifier.weight(1f)) {
                    MiniPostItemShimmer(
                        lists = shimmerColors,
                        floatAnim = 500f,
                        isVertical = false
                    )
                }
            }
        }

        CatalogSection("Snackbar") {
            SnackbarCustomized(
                snackHost = snackHostState,
                bgColor = JchuCatalogTheme.colors.accent,
                contentColor = Color.White
            )
        }
    }
}

@Composable
private fun CatalogSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(JchuCatalogTheme.colors.background)
                .padding(JchuCatalogTheme.spacing.dimen12),
        verticalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen08)
    ) {
        androidx.compose.material3.Text(
            text = title,
            style = JchuCatalogTheme.typography.section,
            color = JchuCatalogTheme.colors.content
        )
        content()
    }
}

@Preview(name = "Previews - Light", showBackground = true)
@Composable
private fun PreviewsLightPreview() {
    JeluchuTheme {
        PreviewsCatalog(onBack = {})
    }
}

@Preview(
    name = "Previews - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewsDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            PreviewsCatalog(onBack = {})
        }
    }
}
