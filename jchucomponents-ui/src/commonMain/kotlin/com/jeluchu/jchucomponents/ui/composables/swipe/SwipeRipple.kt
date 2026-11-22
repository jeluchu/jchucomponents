@file:Suppress("NAME_SHADOWING")

package com.jeluchu.jchucomponents.ui.composables.swipe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

internal const val swipeAnimationDurationMs = 400

@Stable
internal class SwipeRippleState {
    private val ripple = mutableStateOf<SwipeRipple?>(null)

    suspend fun animate(action: SwipeActionMeta) {
        val drawOnRightSide = action.isOnRightSide
        val actionValue = action.value
        ripple.value = SwipeRipple(
            isUndo = actionValue.isUndo,
            rightSide = drawOnRightSide,
            color = actionValue.background,
            alpha = 0f,
            progress = 0f
        )

        val duration = (swipeAnimationDurationMs * if (actionValue.isUndo) 1.75f else 1f).roundToInt()
        coroutineScope {
            launch {
                Animatable(initialValue = 0f).animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = duration)
                ) {
                    ripple.value = ripple.value?.copy(progress = value)
                }
            }
            launch {
                Animatable(initialValue = if (actionValue.isUndo) 0f else 0.25f).animateTo(
                    targetValue = if (actionValue.isUndo) 0.5f else 0f,
                    animationSpec = tween(
                        durationMillis = duration,
                        delayMillis = if (actionValue.isUndo) 0 else duration / 2
                    )
                ) {
                    ripple.value = ripple.value?.copy(alpha = value)
                }
            }
        }
    }

    fun draw(scope: DrawScope) {
        ripple.value?.let { currentRipple ->
            scope.clipRect {
                val size = scope.size
                val startRadius = if (currentRipple.isUndo) size.width + size.height else size.height
                val endRadius = if (currentRipple.isUndo) size.height else size.width + size.height
                val radius = lerp(startRadius, endRadius, currentRipple.progress)

                drawCircle(
                    color = currentRipple.color,
                    radius = radius,
                    alpha = currentRipple.alpha,
                    center = this.center.copy(
                        x = if (currentRipple.rightSide) size.width + size.height else -size.height
                    )
                )
            }
        }
    }
}

private data class SwipeRipple(
    val isUndo: Boolean,
    val rightSide: Boolean,
    val color: Color,
    val alpha: Float,
    val progress: Float
)

private fun lerp(start: Float, stop: Float, fraction: Float): Float =
    start * (1f - fraction) + stop * fraction
