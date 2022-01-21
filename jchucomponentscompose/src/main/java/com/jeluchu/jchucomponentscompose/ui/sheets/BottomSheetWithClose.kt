package com.jeluchu.jchucomponentscompose.ui.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 *
 * Author: @Jeluchu
 *
 * This component displays a BottomSheet with an icon to close it
 *
 * @param content change the color of the system notification bar
 * @param modifier modifier that will be used to change the color, size...
 * @param onClosePressed action that will be performed when the close button is pressed
 * @param closeButtonColor color of the close button
 *
 */

@Composable
fun BottomSheetWithCloseDialog(
    modifier: Modifier = Modifier,
    cardCloseModifier: Modifier = Modifier
        .width(125.dp)
        .height(20.dp)
        .padding(vertical = 8.dp),
    isCardCloseShow: Boolean = true,
    isCloseIconShow: Boolean = false,
    closeButtonColor: Color = Color.Gray,
    onClosePressed: () -> Unit = {},
    content: @Composable () -> Unit
) {

    Box(modifier.fillMaxWidth()) {

        if (isCardCloseShow)
            Card(
                modifier = cardCloseModifier.align(Alignment.TopCenter),
                backgroundColor = closeButtonColor.copy(.6f),
                shape = CircleShape,
                elevation = 0.dp
            ) {}

        content()

        if (isCloseIconShow)
            IconButton(
                onClick = onClosePressed,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(29.dp)
            ) { Icon(Icons.Filled.Close, tint = closeButtonColor, contentDescription = null) }


    }
}