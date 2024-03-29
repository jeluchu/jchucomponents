/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.loaders

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 *
 * Author: @Jeluchu
 *
 * This component is a loading with pulse animation
 *
 * @sample PulseLoadingPreview
 *
 * @param durationMillis animation duration
 * @param maxPulseSize maximum size of circular animation
 * @param minPulseSize minimum size of circular animation
 * @param pulseColor color of the circular animation
 * @param centreColor color of the main circle
 *
 */

@Composable
fun PulseLoading(
    durationMillis: Int = 1000,
    maxPulseSize: Float = 300f,
    minPulseSize: Float = 50f,
    pulseColor: Color = Color(234, 240, 246),
    centreColor: Color = Color(66, 133, 244)
) {
    val infiniteTransition = rememberInfiniteTransition()
    val size by infiniteTransition.animateFloat(
        initialValue = minPulseSize,
        targetValue = maxPulseSize,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .size(size.dp)
                .align(Alignment.Center)
                .alpha(alpha),
            colors = CardDefaults.cardColors(
                containerColor = pulseColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                focusedElevation = 0.dp,
                hoveredElevation = 0.dp,
                draggedElevation = 0.dp,
                disabledElevation = 0.dp
            )
        ) {}
        Card(
            modifier = Modifier
                .size(minPulseSize.dp)
                .align(Alignment.Center),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = centreColor
            ),
        ) {}
    }
}

@Preview
@Composable
fun PulseLoadingPreview() {
    PulseLoading()
}