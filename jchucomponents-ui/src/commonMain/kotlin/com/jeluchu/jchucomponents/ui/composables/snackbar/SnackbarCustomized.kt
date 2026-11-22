package com.jeluchu.jchucomponents.ui.composables.snackbar

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
    shape: Shape = RoundedCornerShape(15.dp)
) = SnackbarHost(snackHost) { data ->
    Snackbar(
        modifier = modifier,
        shape = shape,
        containerColor = bgColor,
        contentColor = contentColor,
        actionContentColor = contentColor,
        action =
            data.visuals.actionLabel?.let { label ->
                {
                    TextButton(onClick = data::performAction) {
                        Text(text = label, style = textStyle)
                    }
                }
            }
    ) {
        Text(text = data.visuals.message, style = textStyle)
    }
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
