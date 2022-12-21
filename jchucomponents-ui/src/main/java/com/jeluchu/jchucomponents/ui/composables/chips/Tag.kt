/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.chips

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 *
 * Author: @Jeluchu
 *
 * This component is similar to the Chips,
 * in which you can display a text or a text and an icon
 *
 * @sample TagPreview()
 *
 * @param modifier modifier that will be used to change the color, size...
 * @param title text to be displayed on the chip
 * @param colors color of the text and background
 * @param shape type of shape desired for this chip
 * @param style style of text to be displayed
 * @param navigateToScreen action to be performed after pressing
 *
 */

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    title: String,
    colors: TagColors = TagDefaults.tagColors(),
    shape: Shape = RoundedCornerShape(4.dp),
    style: TextStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
    navigateToScreen: () -> Unit = {}
) {
    val tagModifier = modifier
        .padding(4.dp)
        .clickable(onClick = navigateToScreen)
        .clip(shape = shape)
        .background(colors.backgroundColor(enabled = true).value)
        .padding(horizontal = 8.dp, vertical = 4.dp)
    Text(
        text = title,
        color = colors.contentColor(enabled = true).value,
        modifier = tagModifier,
        style = style
    )
}

@Stable
interface TagColors {
    @Composable
    fun backgroundColor(enabled: Boolean): State<Color>

    @Composable
    fun contentColor(enabled: Boolean): State<Color>
}

@Immutable
private class DefaultTagColors(
    private val backgroundColor: Color,
    private val contentColor: Color
) : TagColors {
    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(newValue = backgroundColor)
    }

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(newValue = contentColor)
    }
}

object TagDefaults {
    @Composable
    fun tagColors(
        backgroundColor: Color = Color.White.copy(alpha = .2f),
        contentColor: Color = Color.White
    ): TagColors = DefaultTagColors(backgroundColor = backgroundColor, contentColor = contentColor)
}