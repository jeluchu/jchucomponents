package com.jeluchu.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.composables.accessibility.Tooltip
import com.jeluchu.jchucomponents.ui.composables.cards.JchuSettingInfoCard
import com.jeluchu.jchucomponents.ui.composables.colors.JchuColorOption
import com.jeluchu.jchucomponents.ui.composables.colors.JchuColorPicker
import com.jeluchu.jchucomponents.ui.composables.chips.YoutubeChip
import com.jeluchu.jchucomponents.ui.composables.divider.JchuIconDivider
import com.jeluchu.jchucomponents.ui.composables.divider.JchuTextDivider
import com.jeluchu.jchucomponents.ui.composables.pager.PageIndicator
import com.jeluchu.jchucomponents.ui.composables.preferences.JchuPreferenceChoice
import com.jeluchu.jchucomponents.ui.composables.premium.TimeCard
import com.jeluchu.jchucomponents.ui.composables.premium.TimeCardColors
import com.jeluchu.jchucomponents.ui.composables.progress.ReadingIndicator
import com.jeluchu.jchucomponents.ui.composables.text.JchuExpandableText
import com.jeluchu.jchucomponents.ui.composables.textfields.CountField
import com.jeluchu.jchucomponents.ui.composables.textfields.JchuCountedField
import com.jeluchu.jchucomponents.ui.composables.chips.Chip
import com.jeluchu.jchucomponents.ui.composables.chips.SelectableChip
import com.jeluchu.jchucomponents.ui.composables.chips.Tag
import com.jeluchu.jchucomponents.ui.composables.chips.TagDefaults
import com.jeluchu.jchucomponents.ui.composables.loaders.CircularLoading
import com.jeluchu.jchucomponents.ui.composables.loaders.DotsLoading
import com.jeluchu.jchucomponents.ui.composables.loaders.PulseLoading
import com.jeluchu.jchucomponents.ui.composables.preferences.JchuPreferenceItem
import com.jeluchu.jchucomponents.ui.composables.preferences.JchuPreferenceSwitch
import com.jeluchu.jchucomponents.ui.composables.textfields.JchuSearchField

class Demo(
    val family: String,
    val name: String,
    val tag: String,
    val description: String,
    val code: String,
    val preview: @Composable () -> Unit
)

val demos: List<Demo> by lazy {
    listOf(
        Demo(
            "Chips", "SelectableChip", "Compose", "Chip that toggles its selected state and shows a check mark.",
            """var selected by remember { mutableStateOf(true) }

SelectableChip(
    label = "Action",
    selected = selected,
    onClick = { selected = it }
)"""
        ) {
            val p = LocalPalette.current
            var selected by remember { mutableStateOf(setOf("Action")) }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Action", "Drama", "Sci-Fi").forEach { genre ->
                    SelectableChip(
                        label = genre, selected = genre in selected, color = if (genre in selected) p.accent else p.panel,
                        textColor = if (genre in selected) p.onAccent else p.fg,
                        onClick = { on -> selected = if (on) selected + genre else selected - genre }
                    )
                }
            }
        },
        Demo(
            "Chips", "Chip", "Compose", "Base chip with optional start and end icons.",
            """Chip(
    label = "Settings",
    startIcon = { Icons.Default.Settings },
    isClickable = true,
    onClick = { }
)"""
        ) {
            val p = LocalPalette.current
            Chip(label = "Settings", startIcon = { Icons.Default.Settings }, startIconTint = p.onAccent, color = p.accent, textColor = p.onAccent, isClickable = true)
        },
        Demo(
            "Chips", "Tag", "Compose", "Compact title tag with custom colors.",
            """Tag(
    title = "Sci-Fi",
    colors = TagDefaults.tagColors(
        backgroundColor = accent,
        contentColor = onAccent
    )
)"""
        ) {
            val p = LocalPalette.current
            Tag(title = "Sci-Fi", colors = TagDefaults.tagColors(backgroundColor = p.accent, contentColor = p.onAccent))
        },
        Demo(
            "Loading", "DotsLoading", "Compose", "Animated dots loader filling its container.",
            """Box(Modifier.height(70.dp)) {
    DotsLoading(loaderColor = accent)
}"""
        ) {
            val p = LocalPalette.current
            Box(Modifier.height(70.dp).fillMaxWidth()) { DotsLoading(loaderColor = p.accent) }
        },
        Demo(
            "Loading", "PulseLoading", "Compose", "Pulsing circle that expands and fades.",
            """PulseLoading(
    maxPulseSize = 150f,
    pulseColor = mint.copy(alpha = 0.4f),
    centreColor = accent
)"""
        ) {
            val p = LocalPalette.current
            Box(Modifier.size(150.dp)) { PulseLoading(maxPulseSize = 150f, pulseColor = p.mint.copy(alpha = 0.4f), centreColor = p.accent) }
        },
        Demo(
            "Loading", "CircularLoading", "Compose", "Centered circular progress, shown while isShow is true.",
            """CircularLoading(
    isShow = state.isLoading,
    colorLoading = accent
)"""
        ) {
            val p = LocalPalette.current
            Box(Modifier.height(70.dp).fillMaxWidth()) { CircularLoading(isShow = true, colorLoading = p.accent) }
        },
        Demo(
            "Preferences", "JchuPreferenceSwitch", "Compose", "Settings row with title, description and a switch.",
            """var on by remember { mutableStateOf(true) }

JchuPreferenceSwitch(
    title = "Notifications",
    description = "Episode alerts",
    checked = on,
    onCheckedChange = { on = it }
)"""
        ) {
            val p = LocalPalette.current
            var on by remember { mutableStateOf(true) }
            JchuPreferenceSwitch(
                title = "Notifications", description = "Episode alerts", checked = on, onCheckedChange = { on = it },
                containerColor = p.panel, contentColor = p.fg, modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth()
            )
        },
        Demo(
            "Preferences", "JchuPreferenceItem", "Compose", "Plain tappable settings row with leading and trailing slots.",
            """JchuPreferenceItem(
    title = "Language",
    description = "English",
    onClick = { }
)"""
        ) {
            val p = LocalPalette.current
            var taps by remember { mutableStateOf(0) }
            JchuPreferenceItem(
                title = "Language", description = "English · tapped $taps times", onClick = { taps++ },
                containerColor = p.panel, contentColor = p.fg, modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth()
            )
        },
        Demo(
            "Inputs", "JchuSearchField", "Compose", "Single-line search field with clear action.",
            """var query by remember { mutableStateOf("") }

JchuSearchField(
    value = query,
    onValueChange = { query = it },
    labelText = "Search anime"
)"""
        ) {
            var query by remember { mutableStateOf("") }
            JchuSearchField(value = query, onValueChange = { query = it }, labelText = "Search anime", modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth())
        },
        Demo(
            "Cards", "JchuSettingInfoCard", "Compose", "Information card with a free Row content slot.",
            """JchuSettingInfoCard {
    Icon(Icons.Default.Check, contentDescription = null)
    Text("Requirements met")
}"""
        ) {
            JchuSettingInfoCard(modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth()) {
                Icon(Icons.Default.Check, contentDescription = null)
                Text("Requirements met")
            }
        }
        ,
        Demo(
            "Chips", "YoutubeChip", "Compose", "Filter chip styled after the YouTube category bar.",
            """YoutubeChip(
    selected = true,
    text = "Music"
)"""
        ) {
            var sel by remember { mutableStateOf(0) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("All", "Music", "Live").forEachIndexed { i, t ->
                    Box(Modifier.clickable { sel = i }) { YoutubeChip(selected = sel == i, text = t) }
                }
            }
        },
        Demo(
            "Progress", "ReadingIndicator", "Compose", "Progress bar for pages read out of the total.",
            """ReadingIndicator(
    lastPage = 4f,
    totalPage = 10f
)"""
        ) {
            // ReadingIndicator draws track and fill in DarkGray on dark themes, so show it on a light surface.
            MaterialTheme(colorScheme = LightSage.scheme) {
                Box(Modifier.widthIn(max = 280.dp).fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(LightSage.panel)) {
                    ReadingIndicator(lastPage = 4f, totalPage = 10f)
                }
            }
        },
        Demo(
            "Progress", "PageIndicator", "Compose", "Dots that show the current page of a pager.",
            """PageIndicator(
    pagesCount = 5,
    currentPageIndex = page,
    color = accent
)"""
        ) {
            val p = LocalPalette.current
            var page by remember { mutableStateOf(1) }
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PageIndicator(pagesCount = 5, currentPageIndex = page, color = p.accent)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Pill("Prev", false, { page = (page - 1).coerceAtLeast(0) })
                    Pill("Next", false, { page = (page + 1).coerceAtMost(4) })
                }
            }
        },
        Demo(
            "Preferences", "JchuPreferenceChoice", "Compose", "Radio-style row for single-choice settings.",
            """JchuPreferenceChoice(
    title = "Dark theme",
    selected = choice == 0,
    onClick = { choice = 0 }
)"""
        ) {
            val p = LocalPalette.current
            var choice by remember { mutableStateOf(0) }
            Column(Modifier.widthIn(max = 300.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Dark theme", "Light theme").forEachIndexed { i, t ->
                    JchuPreferenceChoice(title = t, selected = choice == i, onClick = { choice = i }, containerColor = p.panel, contentColor = p.fg)
                }
            }
        },
        Demo(
            "Inputs", "JchuCountedField", "Compose", "Text field with a character counter.",
            """var name by remember { mutableStateOf("") }

JchuCountedField(
    title = "Name",
    value = name,
    onValueChange = { name = it },
    maxLength = 30
)"""
        ) {
            val p = LocalPalette.current
            var name by remember { mutableStateOf("") }
            Box(Modifier.widthIn(max = 300.dp).fillMaxWidth()) {
                JchuCountedField(
                    title = "Name", value = name, onValueChange = { name = it }, maxLength = 30,
                    countField = CountField(cursorColor = p.accent, counterTextColor = p.onAccent, disabledLabelColor = p.accent, backgroundColor = p.soft)
                )
            }
        },
        Demo(
            "Inputs", "JchuColorPicker", "Compose", "Typed color swatches with a selected state.",
            """JchuColorPicker(
    options = listOf(
        JchuColorOption("sage", sage, "Sage"),
        JchuColorOption("mint", mint, "Mint")
    ),
    selectedValue = selected,
    onValueSelected = { selected = it }
)"""
        ) {
            var selected by remember { mutableStateOf("sage") }
            val options = remember {
                listOf(
                    JchuColorOption("sage", Color(0xFFB9C78F), "Sage"), JchuColorOption("mint", Color(0xFF8FC4A0), "Mint"),
                    JchuColorOption("moss", Color(0xFF3D4D42), "Moss"), JchuColorOption("cream", Color(0xFFF1EFE0), "Cream"),
                    JchuColorOption("slate", Color(0xFF1B2128), "Slate")
                )
            }
            JchuColorPicker(options = options, selectedValue = selected, onValueSelected = { selected = it })
        },
        Demo(
            "Cards", "TimeCard", "Compose", "Pricing-style card with title, amount and description.",
            """TimeCard(
    title = "Monthly",
    amount = "1.99",
    description = "per month"
)"""
        ) {
            val p = LocalPalette.current
            TimeCard(title = "Monthly", amount = "1.99", description = "per month", colors = TimeCardColors(containerColor = p.soft, contentPrimaryColor = p.accent, contentSecondaryColor = p.fg))
        },
        Demo(
            "Text", "JchuExpandableText", "Compose", "Long text collapsed to a few lines with a show more toggle.",
            """var expanded by remember { mutableStateOf(false) }

JchuExpandableText(
    text = synopsis,
    expanded = expanded,
    onExpandedChange = { expanded = it },
    collapsedMaxLines = 2
)"""
        ) {
            val p = LocalPalette.current
            var expanded by remember { mutableStateOf(false) }
            JchuExpandableText(
                text = "A quiet village hides a library where every book rewrites itself each night. When a young archivist notices the pattern, she must decide whether to keep the secret or share it with the world.",
                expanded = expanded, onExpandedChange = { expanded = it }, collapsedMaxLines = 2, color = p.fg,
                modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth()
            )
        },
        Demo(
            "Text", "Dividers", "Compose", "Dividers with a centered text or icon.",
            """JchuTextDivider(text = "OR")
JchuIconDivider(icon = Icons.Default.Star)"""
        ) {
            val p = LocalPalette.current
            Column(Modifier.widthIn(max = 300.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                JchuTextDivider(text = "OR", tint = p.fg)
                JchuIconDivider(icon = Icons.Default.Star, tint = p.accent)
            }
        },
        Demo(
            "Feedback", "Tooltip", "Compose", "Popup tooltip that closes itself after a timeout.",
            """val expanded = remember { mutableStateOf(false) }

Box {
    Button(onClick = { expanded.value = true }) { Text("Hint") }
    Tooltip(expanded = expanded, text = "Saved to library")
}"""
        ) {
            val p = LocalPalette.current
            val expanded = remember { mutableStateOf(false) }
            Box {
                Pill("Show tooltip", false, { expanded.value = true }, height = 44.dp)
                Tooltip(expanded = expanded, text = "Saved to library", backgroundColor = p.codeBg)
            }
        }
    )
}
