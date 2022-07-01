/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.ui.chips

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jchucomponents.ui.modifier.cornerRadius
import com.jchucomponents.ui.text.MarqueeText
import com.jchucomponents.ui.theme.artichoke

@Composable
fun Type(
    type: String,
    modifier: Modifier = Modifier,
    textColor: Color = artichoke,
    bgColor: Color = artichoke.copy(alpha = 0.1f),
    textAlign: TextAlign? = null,
    fontSize: TextUnit = 12.sp,
    isLongText: Boolean = false,
    style: TextStyle = LocalTextStyle.current
) {
    if (isLongText)
        MarqueeText(
            modifier = modifier
                .padding(vertical = 4.dp)
                .clip(10.cornerRadius())
                .background(bgColor)
                .padding(10.dp, 2.dp),
            text = type,
            style = style,
            fontSize = fontSize,
            color = textColor,
            textAlign = textAlign,
            gradientEdgeColor = Color.Transparent
        )
    else
        Text(
            modifier = modifier
                .padding(vertical = 4.dp)
                .clip(10.cornerRadius())
                .background(bgColor)
                .padding(10.dp, 2.dp),
            text = type,
            style = style,
            fontSize = fontSize,
            textAlign = textAlign,
            color = textColor
        )
}

@ExperimentalFoundationApi
@Preview
@Composable
fun VillagerTypePreview() {
    Type(
        "20/09",
        Modifier
    )
}