/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.chips

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    startIcon: () -> ImageVector? = { null },
    isStartIconEnabled: Boolean = false,
    startIconTint: Color = Color.Unspecified,
    onStartIconClicked: () -> Unit = { },
    endIcon: () -> ImageVector? = { null },
    isEndIconEnabled: Boolean = false,
    endIconTint: Color = Color.Unspecified,
    onEndIconClicked: () -> Unit = { },
    textColor: Color = Color.DarkGray,
    color: Color = MaterialTheme.colorScheme.surface,
    contentDescription: String? = null,
    label: String,
    elevation: Dp = 0.dp,
    shape: Shape = RoundedCornerShape(10.dp),
    isClickable: Boolean = false,
    onClick: () -> Unit = { }
) {
    Surface(
        modifier = modifier
            .clip(shape)
            .clickable(
                enabled = isClickable,
                onClick = { onClick() }
            ),
        tonalElevation = elevation,
        shadowElevation = elevation,
        shape = shape,
        color = color
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val leader = startIcon()
            val trailer = endIcon()

            if (leader != null) {
                Icon(
                    leader,
                    contentDescription = contentDescription,
                    tint = startIconTint,
                    modifier = Modifier
                        .clip(shape)
                        .clickable(enabled = isStartIconEnabled, onClick = onStartIconClicked)
                        .padding(horizontal = 4.dp)
                )
            }

            Text(
                label,
                modifier = Modifier.padding(8.dp),
                color = textColor
            )

            if (trailer != null) {
                Icon(
                    trailer,
                    contentDescription = contentDescription,
                    tint = endIconTint,
                    modifier = Modifier
                        .clip(shape)
                        .clickable(enabled = isEndIconEnabled, onClick = onEndIconClicked)
                        .padding(horizontal = 4.dp)
                )
            }

        }
    }
}

@Composable
fun SelectableChip(
    modifier: Modifier = Modifier,
    label: String,
    contentDescription: String? = null,
    shape: Shape = RoundedCornerShape(10.dp),
    textColor: Color = Color.DarkGray,
    color: Color = MaterialTheme.colorScheme.surface,
    elevation: Dp = 0.dp,
    selected: Boolean,
    onClick: (nowSelected: Boolean) -> Unit
) {
    Chip(
        modifier = modifier,
        startIcon = { if (selected) Icons.Default.Check else null },
        startIconTint = Color.Black.copy(alpha = 0.5f),
        contentDescription = contentDescription,
        shape = shape,
        textColor = textColor,
        color = color,
        elevation = elevation,
        label = label,
        isClickable = true,
        onClick = { onClick(!selected) }
    )
}

@Composable
fun RemovableChip(
    label: String,
    shape: Shape = RoundedCornerShape(10.dp),
    contentDescription: String,
    textColor: Color = Color.DarkGray,
    color: Color = MaterialTheme.colorScheme.surface,
    elevation: Dp = 0.dp,
    onRemove: () -> Unit
) {
    Chip(
        endIcon = { Icons.Default.HighlightOff },
        endIconTint = Color.Black.copy(alpha = 0.5f),
        contentDescription = contentDescription,
        shape = shape,
        textColor = textColor,
        color = color,
        elevation = elevation,
        label = label,
        onEndIconClicked = { onRemove() }
    )
}