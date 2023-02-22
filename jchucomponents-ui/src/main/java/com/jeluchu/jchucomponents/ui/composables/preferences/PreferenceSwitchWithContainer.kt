package com.jeluchu.jchucomponents.ui.composables.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.runtime.remember.rememberMutableStateOf
import com.jeluchu.jchucomponents.ui.themes.artichoke
import com.jeluchu.jchucomponents.ui.themes.cosmicLatte

@Composable
fun PreferenceSwitchWithContainer(
    title: String,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    icon: ImageVector? = null,
    colors: SwitchWithContainerColors = SwitchWithContainerColors(),
    switchColors: SwitchColors = SwitchDefaults.colors(),
    isChecked: Boolean,
    onClick: () -> Unit,
) {
    val thumbContent: (@Composable () -> Unit)? = if (isChecked) { {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else null

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(if (isChecked) colors.backgroundEnabled else colors.backgroundDisabled)
            .toggleable(value = isChecked) { onClick() }
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.let {
            Icon(imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp)
                    .size(24.dp),
                tint = if (isChecked) colors.iconEnabled else colors.iconDisabled
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = if (icon == null) 12.dp else 0.dp, end = 12.dp)
        ) {
            Text(
                text = title,
                maxLines = 2,
                style = style,
                color = if (isChecked) colors.textEnabled else colors.textDisabled
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = null,
            modifier = Modifier.padding(start = 12.dp, end = 6.dp),
            colors = switchColors,
            thumbContent = thumbContent
        )
    }
}

@Immutable
class SwitchWithContainerColors constructor(
    val iconEnabled: Color = artichoke,
    val iconDisabled: Color = cosmicLatte,
    val textEnabled: Color = artichoke,
    val textDisabled: Color = cosmicLatte,
    val backgroundEnabled: Color = cosmicLatte,
    val backgroundDisabled: Color = artichoke,
)

@Composable
@Preview
private fun PreferenceSwitchWithContainerPreview() {
    var isChecked by rememberMutableStateOf(false)
    PreferenceSwitchWithContainer(
        icon = Icons.Filled.Close,
        title = "Title ".repeat(2),
        isChecked = isChecked,
        onClick = { isChecked = !isChecked }
    )
}