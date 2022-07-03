/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.images

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.core.extensions.ints.empty
import com.jeluchu.jchucomponents.core.extensions.strings.empty

/**
 *
 * Author: @Jeluchu
 *
 * This component performs the same function as the animation
 * when you double click on an image to "like" it on Instagram
 *
 * @param imageRemote link of the remote image to be displayed
 * @param imageResources drawable resource of the remote image to be displayed
 * @param iconResource drawable of the icon you want to display when pressing
 * @param size maximum animation size
 * @param onDoubleTap action to be performed after the double-tap animation is performed
 *
 */

@Composable
fun DoubleTapAnimation(
    imageRemote: String = String.empty(),
    imageResources: Int = Int.empty(),
    iconResource: Int,
    size: Dp = 250.dp,
    onDoubleTap: () -> Unit
) {
    var isLike by remember { mutableStateOf(false) }
    val animatedSize by animateDpAsState(
        targetValue = if (isLike) size else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 500f
        )
    )
    Box(modifier = Modifier.fillMaxSize()) {

        if (imageRemote.isNotEmpty()) {
            NetworkImage(
                url = imageRemote,
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
        } else {
            Image(
                painter = painterResource(id = imageResources),
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
        }

        if (isLike) {
            Icon(
                painterResource(id = iconResource),
                tint = Color.White,
                modifier = Modifier
                    .size(animatedSize)
                    .align(Alignment.Center),
                contentDescription = ""
            )
            if (animatedSize == size) {
                isLike = false
            }
        }

    }
}
