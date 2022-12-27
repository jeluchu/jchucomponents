/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.migration.floatbutton

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ktx.strings.empty
import com.jeluchu.jchucomponents.ui.themes.artichoke
import com.jeluchu.jchucomponents.ui.themes.cosmicLatte
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FloatingButton(
    isVisible: Boolean,
    @DrawableRes icon: Int,
    iconSize: Dp = 40.dp,
    tint: Color = cosmicLatte,
    background: Color = artichoke,
    onClick: () -> Unit = {}
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        androidx.compose.material3.FloatingActionButton(
            containerColor = background,
            elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            ),
            onClick = onClick
        ) {
            Icon(
                modifier = Modifier.size(iconSize),
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = String.empty(),
                tint = tint
            )
        }
    }
}