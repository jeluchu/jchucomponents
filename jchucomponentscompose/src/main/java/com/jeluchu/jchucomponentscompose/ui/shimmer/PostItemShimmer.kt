package com.jeluchu.jchucomponentscompose.ui.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerItemBig(
    lists: List<Color>,
    floatAnim: Float = 0f,
    isVertical: Boolean
) {

    val brush = if (isVertical) Brush.verticalGradient(lists, 0f, floatAnim) else
        Brush.horizontalGradient(lists, 0f, floatAnim)

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .size(200.dp)
                .background(
                    brush = brush
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .padding(vertical = 8.dp)
                .background(brush = brush)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .padding(vertical = 8.dp)
                .background(brush = brush)
        )
    }
}