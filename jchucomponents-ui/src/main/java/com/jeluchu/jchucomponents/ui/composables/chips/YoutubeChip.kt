/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.chips

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 *
 * Author: @Jeluchu
 *
 * This component is similar to the chips in the YouTube app
 *
 * @sample YoutubeChipPreview
 *
 * @param modifier modifier that will be used to change the color, size...
 * @param selected status (selected or not)
 * @param text text to be displayed inside the chip
 *
 */

@Composable
fun YoutubeChip(
    modifier: Modifier = Modifier,
    selected: Boolean,
    text: String,
) {
    Surface(
        color = when {
            selected -> MaterialTheme.colorScheme.onSurface.copy(
                alpha = if (isSystemInDarkTheme()) 0.7f else 1f
            )
            else -> MaterialTheme.colorScheme.onSurface.copy(
                alpha = if (isSystemInDarkTheme()) 0.04f else 0.07f
            )
        },
        contentColor = when {
            selected -> MaterialTheme.colorScheme.surface
            else -> MaterialTheme.colorScheme.onSurface
        },
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            color = when {
                selected -> MaterialTheme.colorScheme.surface
                else -> if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
            }
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(
                vertical = 8.dp,
                horizontal = 12.dp,
            )
        )
    }
}

@Preview
@Composable
fun YoutubeChipPreview() {
    YoutubeChip(
        modifier = Modifier.padding(horizontal = 4.dp),
        selected = true,
        text = "Name"
    )
}