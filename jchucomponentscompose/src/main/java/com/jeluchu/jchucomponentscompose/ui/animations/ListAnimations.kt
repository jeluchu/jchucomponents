package com.jeluchu.jchucomponentscompose.ui.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer

/**
 *
 * Author: @Jeluchu
 *
 * This is an extension of animations for list item modifiers
 *
 * @param animationType in this parameter you have to pass a type of animation,
 * by default this value is set to Default, you have the types of animations at [Animations]
 *
 */

fun Modifier.animateItem(animationType: Animations = Animations.Default): Modifier =
    composed {
        when (animationType) {
            Animations.Fade -> {
                val animatedProgress = remember { Animatable(initialValue = 0f) }
                LaunchedEffect(Unit) {
                    animatedProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(600)
                    )
                }
                alpha(animatedProgress.value)
            }
            Animations.Scale -> {
                val animatedProgress = remember { Animatable(initialValue = 0.8f) }
                LaunchedEffect(Unit) {
                    animatedProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(300, easing = LinearEasing)
                    )
                }
                graphicsLayer(scaleY = animatedProgress.value, scaleX = animatedProgress.value)
            }
            Animations.Slide -> {
                val animatedProgress = remember { Animatable(initialValue = 300f) }
                LaunchedEffect(Unit) {
                    animatedProgress.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                }
                graphicsLayer(translationX = animatedProgress.value)
            }
            Animations.FadeAndSlide -> {
                val animatedProgress = remember { Animatable(initialValue = -300f) }
                val opacityProgress = remember { Animatable(initialValue = 0f) }
                LaunchedEffect(Unit) {
                    animatedProgress.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(300, easing = LinearEasing)
                    )
                    opacityProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(600)
                    )
                }
                graphicsLayer(translationX = animatedProgress.value)
                alpha(opacityProgress.value)
            }
            Animations.SlideUp -> {
                val animatedProgress = remember { Animatable(initialValue = 300f) }
                val opacityProgress = remember { Animatable(initialValue = 0f) }
                LaunchedEffect(Unit) {
                    animatedProgress.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(300, easing = LinearEasing)
                    )
                    opacityProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(600)
                    )
                }
                graphicsLayer(translationY = animatedProgress.value)
                alpha(opacityProgress.value)
            }
            Animations.RotateX -> {
                val animatedProgress = remember { Animatable(initialValue = 0f) }
                LaunchedEffect(Unit) {
                    animatedProgress.animateTo(
                        targetValue = 360f,
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    )
                }
                graphicsLayer(rotationX = animatedProgress.value)
            }
            Animations.Default -> {
                val animatedProgress = remember { Animatable(initialValue = 0.8f) }
                LaunchedEffect(Unit) {
                    animatedProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(300)
                    )
                }
                alpha(animatedProgress.value)
            }
        }
    }

enum class Animations {
    Fade,
    Scale,
    Slide,
    FadeAndSlide,
    SlideUp,
    RotateX,
    Default
}
