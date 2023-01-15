/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.sheets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 *
 * Author: @Jeluchu
 *
 * This component displays a BottomSheet with an icon to close it
 *
 * @param modifier modifier that will be used to change the color, size...
 * @param bottomSheetSettings Configuration and customization of colors and actions of the modal,
 * to learn more you can see [BottomSheetSettings]
 * @param content will include the [Composable] that will subsequently be displayed in the modal
 *
 */

@Composable
fun BottomSheetWithCloseDialog(
    modifier: Modifier = Modifier,
    bottomSheetSettings: BottomSheetSettings = BottomSheetSettings(),
    content: @Composable () -> Unit
) = Box(modifier.fillMaxWidth()) {

    if (bottomSheetSettings.isCardCloseShow)
        Card(
            modifier = bottomSheetSettings.contentModifier.align(Alignment.TopCenter),
            colors = CardDefaults.cardColors(
                containerColor = bottomSheetSettings.buttonTint.copy(.6f)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                focusedElevation = 0.dp,
                hoveredElevation = 0.dp,
                draggedElevation = 0.dp,
                disabledElevation = 0.dp,
            ),
            shape = CircleShape,
            content = {}
        )

    content()

    if (bottomSheetSettings.isCloseIconShow)
        IconButton(
            onClick = bottomSheetSettings.onClosePressed,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(29.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                tint = bottomSheetSettings.buttonTint,
                contentDescription = null
            )
        }
}

@Immutable
class BottomSheetSettings constructor(
    val contentModifier: Modifier = Modifier
        .width(125.dp)
        .height(20.dp)
        .padding(vertical = 8.dp),
    val buttonTint: Color = Color.Gray,
    val isCardCloseShow: Boolean = true,
    val isCloseIconShow: Boolean = false,
    val onClosePressed: () -> Unit = {}
)