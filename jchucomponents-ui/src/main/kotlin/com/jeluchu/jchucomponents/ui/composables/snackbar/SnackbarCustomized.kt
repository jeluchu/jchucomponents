package com.jeluchu.jchucomponents.ui.composables.snackbar

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.theme.artichoke
import com.jeluchu.jchucomponents.ui.theme.cosmicLatte

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

@Preview(showBackground = true)
@Composable
private fun SnackbarCustomizedPreview() {
    val snackHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackHostState) {
        snackHostState.showSnackbar("Customized snackbar")
    }

    SnackbarCustomized(snackHost = snackHostState)
}
