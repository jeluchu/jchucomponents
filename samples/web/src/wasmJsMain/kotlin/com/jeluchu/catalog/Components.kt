package com.jeluchu.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.composables.cards.JchuSettingInfoCard
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
                containerColor = p.panel, contentColor = p.fg, modifier = Modifier.width(300.dp)
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
                containerColor = p.panel, contentColor = p.fg, modifier = Modifier.width(300.dp)
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
            JchuSearchField(value = query, onValueChange = { query = it }, labelText = "Search anime", modifier = Modifier.width(300.dp))
        },
        Demo(
            "Cards", "JchuSettingInfoCard", "Compose", "Information card with a free Row content slot.",
            """JchuSettingInfoCard {
    Icon(Icons.Default.Check, contentDescription = null)
    Text("Requirements met")
}"""
        ) {
            JchuSettingInfoCard(modifier = Modifier.width(300.dp)) {
                Icon(Icons.Default.Check, contentDescription = null)
                Text("Requirements met")
            }
        }
    )
}
