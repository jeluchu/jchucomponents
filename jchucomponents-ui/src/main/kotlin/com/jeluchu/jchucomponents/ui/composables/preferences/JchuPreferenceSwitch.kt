package com.jeluchu.jchucomponents.ui.composables.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/** A preference row whose checked state is owned by the caller. */
@Composable
fun JchuPreferenceSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    switchColors: SwitchColors = SwitchDefaults.colors(),
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    descriptionStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    leadingContent: (@Composable () -> Unit)? = null,
    checkedIcon: ImageVector? = null
) = Surface(
    modifier =
        modifier.toggleable(
            value = checked,
            enabled = enabled,
            role = Role.Switch,
            onValueChange = onCheckedChange
        ),
    color = containerColor,
    contentColor = contentColor
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingContent != null) leadingContent()
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = if (leadingContent == null) 0.dp else 16.dp, end = 16.dp)
        ) {
            PreferenceItemTitle(
                text = title,
                enabled = enabled,
                style = titleStyle,
                color = contentColor
            )
            if (!description.isNullOrEmpty()) {
                PreferenceItemDescription(
                    text = description,
                    enabled = enabled,
                    style = descriptionStyle,
                    color = contentColor
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            colors = switchColors,
            thumbContent = if (checked && checkedIcon != null) {
                {
                    Icon(
                        imageVector = checkedIcon,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize)
                    )
                }
            } else {
                null
            }
        )
    }
}
