/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.toolbars

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.core.extensions.strings.empty
import com.jeluchu.jchucomponents.ui.text.MarqueeText

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