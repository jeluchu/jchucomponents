package com.jeluchu.jchucomponentscompose.ui.images

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
import com.jeluchu.jchucomponentscompose.core.extensions.ints.empty
import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty

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
