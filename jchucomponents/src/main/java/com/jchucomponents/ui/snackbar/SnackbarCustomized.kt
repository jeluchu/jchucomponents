package com.jchucomponents.ui.snackbar

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.jchucomponents.ui.theme.artichoke
import com.jchucomponents.ui.theme.cosmicLatte

@Composable
fun SnackbarCustomized(
    modifier: Modifier = Modifier,
    snackHost: SnackbarHostState,
    bgColor: Color = artichoke,
    contentColor: Color = cosmicLatte,
    textStyle: TextStyle = LocalTextStyle.current,
    shape: Shape = RoundedCornerShape(15.dp),
) = SnackbarHost(snackHost) { data ->
    CustomSnackbar(
        modifier = modifier,
        backgroundColor = bgColor,
        contentColor = contentColor,
        textStyle = textStyle,
        shape = shape,
        elevation = 0.dp,
        snackbarData = data
    )
}