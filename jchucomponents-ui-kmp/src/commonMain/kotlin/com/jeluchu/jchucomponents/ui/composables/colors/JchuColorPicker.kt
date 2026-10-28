package com.jeluchu.jchucomponents.ui.composables.colors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** A typed color option displayed by [JchuColorPicker]. */
data class JchuColorOption<T>(
    val value: T,
    val color: Color,
    val contentDescription: String,
    val enabled: Boolean = true
)

/**
 * Displays a wrapping, single-choice color palette controlled by the caller.
 *
 * Values are intentionally independent from color strings, resources and application models.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> JchuColorPicker(
    options: List<JchuColorOption<T>>,
    selectedValue: T,
    onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemSize: Dp = 44.dp,
    horizontalSpacing: Dp = 8.dp,
    verticalSpacing: Dp = 8.dp,
    selectedBorderColor: Color = MaterialTheme.colorScheme.onSurface,
    unselectedBorderColor: Color = MaterialTheme.colorScheme.outline,
    selectionIndicatorColor: Color = MaterialTheme.colorScheme.surface
) {
    FlowRow(
        modifier = modifier.selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        options.forEach { option ->
            val selected = option.value == selectedValue
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = if (selected) selectionIndicatorColor else Color.Transparent,
                modifier =
                    Modifier
                        .size(itemSize)
                        .alpha(if (option.enabled) 1f else 0.38f)
                        .clip(CircleShape)
                        .background(option.color)
                        .border(
                            width = if (selected) 3.dp else 1.dp,
                            color = if (selected) selectedBorderColor else unselectedBorderColor,
                            shape = CircleShape
                        )
                        .semantics {
                            contentDescription = option.contentDescription
                        }
                        .selectable(
                            selected = selected,
                            enabled = option.enabled,
                            role = Role.RadioButton,
                            onClick = { onValueSelected(option.value) }
                        )
            )
        }
    }
}
