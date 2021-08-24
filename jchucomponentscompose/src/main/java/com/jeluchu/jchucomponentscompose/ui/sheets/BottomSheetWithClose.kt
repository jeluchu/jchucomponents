package com.jeluchu.jchucomponentscompose.ui.sheets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
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
 * @param content change the color of the system notification bar
 * @param modifier modifier that will be used to change the color, size...
 * @param onClosePressed action that will be performed when the close button is pressed
 * @param closeButtonColor color of the close button
 *
 */

@Composable
fun BottomSheetWithCloseDialog(
    onClosePressed: () -> Unit,
    modifier: Modifier = Modifier,
    closeButtonColor: Color = Color.Gray,
    content: @Composable () -> Unit
) {

    Box(modifier.fillMaxWidth()) {
        content()
        IconButton(
            onClick = onClosePressed,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(29.dp)
        ) { Icon(Icons.Filled.Close, tint = closeButtonColor, contentDescription = null) }
    }
}