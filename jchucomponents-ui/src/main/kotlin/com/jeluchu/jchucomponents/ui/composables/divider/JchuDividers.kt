package com.jeluchu.jchucomponents.ui.composables.divider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** A centered icon with divider lines on either side. */
@Composable
fun JchuIconDivider(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    contentDescription: String? = null,
    lineColor: Color = tint,
    lineThickness: Dp = 1.dp,
    lineWeight: Float = 1f,
    iconWeight: Float = 0.4f,
    spacing: Dp = 10.dp
) {
    require(lineWeight > 0f) { "lineWeight must be greater than zero." }
    require(iconWeight > 0f) { "iconWeight must be greater than zero." }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(lineWeight),
            thickness = lineThickness,
            color = lineColor
        )
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = iconModifier.weight(iconWeight),
            tint = tint
        )
        HorizontalDivider(
            modifier = Modifier.weight(lineWeight),
            thickness = lineThickness,
            color = lineColor
        )
    }
}

/** A centered label with divider lines on either side. */
@Composable
fun JchuTextDivider(
    text: String,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    lineColor: Color = tint.copy(alpha = 0.3f),
    lineThickness: Dp = 1.dp,
    lineWeight: Float = 0.4f,
    spacing: Dp = 10.dp
) {
    require(lineWeight > 0f) { "lineWeight must be greater than zero." }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(lineWeight),
            thickness = lineThickness,
            color = lineColor
        )
        Text(
            text = text,
            color = tint,
            textAlign = TextAlign.Center,
            style = textStyle
        )
        HorizontalDivider(
            modifier = Modifier.weight(lineWeight),
            thickness = lineThickness,
            color = lineColor
        )
    }
}
