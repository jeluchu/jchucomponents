package com.jeluchu.composer.features.chips.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.jchucomponents.ui.composables.chips.Chip
import com.jeluchu.jchucomponents.ui.composables.chips.RemovableChip
import com.jeluchu.jchucomponents.ui.composables.chips.SelectableChip

@Composable
fun ChipsView(onBack: () -> Unit) {
    var selected by remember { mutableStateOf(false) }
    var removableVisible by remember { mutableStateOf(true) }

    ScaffoldStructure(
        title = "Chips",
        onNavIconClick = onBack
    ) {
        Text("Basic", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Chip(label = "Default")
            Chip(
                label = "Clickable",
                isClickable = true,
                onClick = { selected = !selected }
            )
        }

        Text("Selection", style = MaterialTheme.typography.titleMedium)
        SelectableChip(
            label = if (selected) "Selected" else "Not selected",
            selected = selected,
            onClick = { selected = it }
        )

        if (removableVisible) {
            Text("Removable", style = MaterialTheme.typography.titleMedium)
            RemovableChip(
                label = "Remove me",
                contentDescription = "Remove chip",
                onRemove = { removableVisible = false }
            )
        }
    }
}
