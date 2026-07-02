package com.jeluchu.composer.features.chips.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.CatalogFixtures
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.JeluchuTheme
import com.jeluchu.jchucomponents.ui.composables.chips.ChipTagView
import com.jeluchu.jchucomponents.ui.composables.chips.Chip
import com.jeluchu.jchucomponents.ui.composables.chips.RemovableChip
import com.jeluchu.jchucomponents.ui.composables.chips.SelectableChip
import com.jeluchu.jchucomponents.ui.composables.chips.Tag
import com.jeluchu.jchucomponents.ui.composables.chips.YoutubeChip

@Composable
fun ChipsView(onBack: () -> Unit) {
    ChipsCatalog(onBack)
}

@Composable
private fun ChipsCatalog(onBack: () -> Unit) {
    var selected by remember { mutableStateOf(false) }
    var removableVisible by remember { mutableStateOf(true) }
    val fixtures = CatalogFixtures.chipFixtures
    val tagFixtures = CatalogFixtures.tagChipFixtures
    val youtubeFixtures = CatalogFixtures.youtubeChipFixtures

    ScaffoldStructure(
        title = "Chips",
        onNavIconClick = onBack
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen16)
        ) {
            Text(
                text = "Basic",
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen08)) {
                Chip(label = fixtures[0].label)
                Chip(
                    label = fixtures[1].label,
                    contentDescription = fixtures[1].contentDescription,
                    isClickable = true,
                    onClick = { selected = !selected }
                )
            }

            Text(
                text = "Selection",
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content,
            )
            SelectableChip(
                label = if (selected) fixtures[2].label else "Not selected",
                contentDescription = fixtures[2].contentDescription,
                selected = selected,
                onClick = { selected = it }
            )

            if (removableVisible) {
                Text(
                    text = "Removable",
                    style = JchuCatalogTheme.typography.section,
                    color = JchuCatalogTheme.colors.content,
                )
                RemovableChip(
                    label = fixtures[3].label,
                    contentDescription = fixtures[3].contentDescription,
                    onRemove = { removableVisible = false }
                )
            }

            Text(
                text = "Long content",
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content,
            )
            Chip(
                modifier = Modifier.fillMaxWidth(),
                label = fixtures[4].label,
                contentDescription = fixtures[4].contentDescription,
            )

            Text(
                text = "Tag variants",
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen08)) {
                Tag(title = tagFixtures[0].label)
                ChipTagView(
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(JchuCatalogTheme.spacing.dimen10))
                        .background(JchuCatalogTheme.colors.primary.copy(alpha = .2f)),
                    title = tagFixtures[1].label,
                    textColor = JchuCatalogTheme.colors.content,
                )
            }

            Text(
                text = "YouTube variants",
                style = JchuCatalogTheme.typography.section,
                color = JchuCatalogTheme.colors.content,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen08)) {
                YoutubeChip(
                    modifier = Modifier.padding(horizontal = JchuCatalogTheme.spacing.dimen04),
                    selected = true,
                    text = youtubeFixtures[0].label,
                )
                YoutubeChip(
                    modifier = Modifier.padding(horizontal = JchuCatalogTheme.spacing.dimen04),
                    selected = false,
                    text = youtubeFixtures[1].label,
                )
            }
        }
    }
}

@Preview(name = "Chips - Light", showBackground = true)
@Composable
private fun ChipsLightPreview() {
    JeluchuTheme {
        ChipsCatalog(onBack = {})
    }
}

@Preview(
    name = "Chips - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ChipsDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            ChipsCatalog(onBack = {})
        }
    }
}

@Preview(
    name = "Chips - Accessibility",
    showBackground = true,
    fontScale = 1.5f
)
@Composable
private fun ChipsAccessibilityPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.highContrast()) {
            ChipsCatalog(onBack = {})
        }
    }
}
