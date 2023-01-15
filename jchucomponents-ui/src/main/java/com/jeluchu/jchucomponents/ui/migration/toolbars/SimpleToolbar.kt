/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.migration.toolbars

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.ktx.strings.empty
import com.jeluchu.jchucomponents.ui.R
import com.jeluchu.jchucomponents.ui.foundation.text.MarqueeText
import com.jeluchu.jchucomponents.ui.themes.artichoke

@Composable
fun SimpleToolbar(
    title: String = String.empty(),
    backgroundColor: Color,
    tintContent: Color = Color.DarkGray,
    style: TextStyle = LocalTextStyle.current,
    leftIcon: Int,
    largeText: Boolean = false,
    navigateAction: () -> Unit
) {
    TopAppBar(
        actions = {
            if (!largeText)
                Text(
                    text = title,
                    modifier = Modifier.padding(end = 15.dp),
                    color = tintContent,
                    style = style,
                    fontSize = 20.sp,
                    textAlign = TextAlign.End
                )
            else
                MarqueeText(
                    text = title,
                    modifier = Modifier.padding(end = 15.dp),
                    color = tintContent,
                    style = style,
                    fontSize = 20.sp,
                    textAlign = TextAlign.End,
                    gradientEdgeColor = Color.Transparent
                )
        },
        title = {},
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        navigationIcon = {
            IconButton(onClick = navigateAction) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = leftIcon),
                    tint = tintContent,
                    contentDescription = null
                )
            }
        }
    )
}

@Preview
@Composable
fun ToolbarSPreview() {
    SimpleToolbar(
        title = "titleBar",
        backgroundColor = artichoke,
        leftIcon = R.drawable.ic_arrow_left,
        style = MaterialTheme.typography.titleLarge,
        navigateAction = { }
    )
}