package com.jeluchu.jchucomponents.ui.extensions.modifier

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.debugInspectorInfo

/**
 *
 * Makes a view shake [enabled] is the view should shake [fromLeft]
 * if the view should start the shake from the left,
 * else it starts from the right [infiniteTransition]
 *
 */
fun Modifier.shake(
    enabled: Boolean = true,
    fromLeft: Boolean = true,
    rotation: Float = 3f,
    animation: DurationBasedAnimationSpec<Float> = tween(200, easing = LinearEasing),
    infiniteTransition: InfiniteTransition
) = composed(
    factory = {
        val initialPosition = if (fromLeft) -rotation else rotation
        val rotationValue by
        infiniteTransition.animateFloat(
            initialValue = initialPosition,
            targetValue = -initialPosition,
            animationSpec = infiniteRepeatable(
                animation = animation,
                repeatMode = RepeatMode.Reverse
            )
        )

        Modifier.graphicsLayer { rotationZ = if (enabled) rotationValue else 0f }
    },
    inspectorInfo =
    debugInspectorInfo {
        name = "shake"
        properties["enabled"] = enabled
        properties["fromLeft"] = fromLeft
        properties["infiniteTransition"] = infiniteTransition
    }
)
