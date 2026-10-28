package com.jeluchu.jchucomponents.ui.composables.images

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
private fun JchuZoomablePreview() {
    val zoomState = rememberJchuZoomState(maxScale = 4f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(Color(0xFF28334A))
            .jchuZoomable(zoomState),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Pinch, pan, or double-tap",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
