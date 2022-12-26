package com.jeluchu.jchucomponents.ui.migration.snackbar

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
import com.jeluchu.jchucomponents.ui.migration.theme.artichoke
import com.jeluchu.jchucomponents.ui.migration.theme.cosmicLatte

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