package com.jeluchu.jchucomponents.ui.composables.preferences

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ktx.colors.opacity

/** Compatibility wrapper for [JchuPreferenceSwitch]. */
@Deprecated(
    message = "Use JchuPreferenceSwitch",
    replaceWith = ReplaceWith(
        "JchuPreferenceSwitch(title = title, checked = isChecked, onCheckedChange = { onClick() })"
    )
)
@Composable
fun PreferenceSwitch(
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    isChecked: Boolean = true,
    containerColor: Color = Color.White,
    contentColor: Color = Color.DarkGray,
    colors: SwitchColors = SwitchDefaults.colors(),
    checkedIcon: ImageVector = Icons.Outlined.Check,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    descriptionStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onClick: () -> Unit = {}
) = JchuPreferenceSwitch(
    title = title,
    checked = isChecked,
    onCheckedChange = { onClick() },
    description = description,
    enabled = enabled,
    containerColor = containerColor,
    contentColor = contentColor,
    switchColors = colors,
    checkedIcon = checkedIcon,
    titleStyle = titleStyle,
    descriptionStyle = descriptionStyle,
    leadingContent = icon?.let {
        {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = contentColor.opacity(enabled),
                modifier = Modifier.padding(start = 8.dp).size(30.dp)
            )
        }
    }
)
