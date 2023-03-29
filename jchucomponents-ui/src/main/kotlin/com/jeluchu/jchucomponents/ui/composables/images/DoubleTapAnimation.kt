/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.images

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ktx.numbers.empty
import com.jeluchu.jchucomponents.ktx.strings.empty
import com.jeluchu.jchucomponents.ui.runtime.remember.rememberMutableStateOf

/**
 *
 * Author: @Jeluchu
 *
 * This component performs the same function as the animation
 * when you double click on an image to "like" it on Instagram
 *
 * @param image drawable resource of the remote image to be displayed
 * @param icon drawable of the icon you want to display when pressing
 * @param size maximum animation size
 * @param onDoubleTap action to be performed after the double-tap animation is performed
 *
 */

@Composable
fun DoubleTapAnimation(
    image: Int = Int.empty(),
    icon: Int,
    size: Dp = 250.dp,
    onDoubleTap: () -> Unit
) {
    var isLike by rememberMutableStateOf(false)
    val animatedSize by animateDpAsState(
        targetValue = if (isLike) size else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 500f
        )
    )
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            imageVector = ImageVector.vectorResource(id = image),
            contentDescription = String.empty(),
            modifier = Modifier
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            isLike = true
                            onDoubleTap()
                        }
                    )
                })

        if (isLike) {
            Icon(
                painterResource(id = icon),
                tint = Color.White,
                modifier = Modifier
                    .size(animatedSize)
                    .align(Alignment.Center),
                contentDescription = String.empty()
            )
            if (animatedSize == size) isLike = false
        }
    }
}

/**
 *
 * Author: @Jeluchu
 *
 * This component performs the same function as the animation
 * when you double click on an image to "like" it on Instagram
 *
 * @param url link of the remote image to be displayed
 * @param icon drawable of the icon you want to display when pressing
 * @param size maximum animation size
 * @param onDoubleTap action to be performed after the double-tap animation is performed
 *
 */

@Composable
fun DoubleTapAnimation(
    url: String = String.empty(),
    icon: Int,
    size: Dp = 250.dp,
    onDoubleTap: () -> Unit
) {
    var isLike by rememberMutableStateOf(false)
    val animatedSize by animateDpAsState(
        targetValue = if (isLike) size else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 500f
        )
    )
    Box(modifier = Modifier.fillMaxSize()) {
        NetworkImage(
            url = url,
            modifier = Modifier
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            isLike = true
                            onDoubleTap()
                        }
                    )
                }
        )
        if (isLike) {
            Icon(
                painterResource(id = icon),
                tint = Color.White,
                modifier = Modifier
                    .size(animatedSize)
                    .align(Alignment.Center),
                contentDescription = String.empty()
            )
            if (animatedSize == size) isLike = false
        }

    }
}
