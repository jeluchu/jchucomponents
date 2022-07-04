/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.animations

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
 * This is an extension of animations for list item modifiers
 *
 * @param animationType in this parameter you have to pass a type of animation,
 * by default this value is set to Default, you have the types of animations at [Animations]
 * @param animatedProgressSpec complete duration of the animation [Int]
 * @param opacityAnimationSpec complete duration of the animation [Int]
 *
 */

fun Modifier.animateItem(
    animationType: Animations = Animations.Default,
    animatedProgressSpec: Int = 300,
    opacityAnimationSpec: Int = 600
): Modifier =
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
                        animationSpec = tween(animatedProgressSpec, easing = LinearEasing)
                    )
                }
                graphicsLayer(scaleY = animatedProgress.value, scaleX = animatedProgress.value)
            }
            Animations.Slide -> {
                val animatedProgress =
                    remember { Animatable(initialValue = animatedProgressSpec.toFloat()) }
                LaunchedEffect(Unit) {
                    animatedProgress.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(animatedProgressSpec, easing = FastOutSlowInEasing)
                    )
                }
                graphicsLayer(translationX = animatedProgress.value)
            }
            Animations.FadeAndSlide -> {
                val animatedProgress =
                    remember { Animatable(initialValue = -animatedProgressSpec.toFloat()) }
                val opacityProgress = remember { Animatable(initialValue = 0f) }
                LaunchedEffect(Unit) {
                    animatedProgress.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(animatedProgressSpec, easing = LinearEasing)
                    )
                    opacityProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(opacityAnimationSpec)
                    )
                }
                graphicsLayer(translationX = animatedProgress.value)
                alpha(opacityProgress.value)
            }
            Animations.SlideUp -> {
                val animatedProgress =
                    remember { Animatable(initialValue = animatedProgressSpec.toFloat()) }
                val opacityProgress = remember { Animatable(initialValue = 0f) }
                LaunchedEffect(Unit) {
                    animatedProgress.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(animatedProgressSpec, easing = LinearEasing)
                    )
                    opacityProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(opacityAnimationSpec)
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
                        animationSpec = tween(animatedProgressSpec, easing = FastOutSlowInEasing)
                    )
                }
                graphicsLayer(rotationX = animatedProgress.value)
            }
            Animations.Default -> {
                val animatedProgress = remember { Animatable(initialValue = 0.8f) }
                LaunchedEffect(Unit) {
                    animatedProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(animatedProgressSpec)
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
