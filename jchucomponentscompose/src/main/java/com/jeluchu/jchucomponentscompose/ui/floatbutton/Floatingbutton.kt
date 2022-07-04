/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.ui.floatbutton

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.offset
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponentscompose.ui.theme.artichoke
import kotlin.math.roundToInt

@ExperimentalAnimationApi
@Composable
fun FloatingButtonWithAnimation(
    isVisible: Boolean,
    offset: MutableState<Float>,
    bgColor: Color = artichoke,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    if (isVisible)
        FloatingActionButton(
            modifier = Modifier
                .offset {
                    IntOffset(x = 0, y = -offset.value.roundToInt())
                },
            backgroundColor = bgColor,
            elevation = FloatingActionButtonDefaults
                .elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                ),
            onClick = onClick
        ) { content() }
}
