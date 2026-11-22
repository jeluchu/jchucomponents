package com.jeluchu.jchucomponents.ui.composables.swipe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberSwipeableActionsState(): SwipeableActionsState = remember { SwipeableActionsState() }

/** The horizontal drag state used by [SwipeableActionsBox]. */
@Stable
class SwipeableActionsState internal constructor() {
    /** Current horizontal offset in pixels. */
    val offset: State<Float> get() = offsetState

    internal val offsetState = mutableStateOf(0f)

    /** True while the row animates back to its resting position. */
    var isResettingOnRelease: Boolean by mutableStateOf(false)
        private set

    internal var canSwipeTowardsRight: () -> Boolean = { false }
    internal var canSwipeTowardsLeft: () -> Boolean = { false }

    internal val draggableState = DraggableState { delta ->
        val targetOffset = offsetState.value + delta
        val isAllowed = isResettingOnRelease ||
            targetOffset > 0f && canSwipeTowardsRight() ||
            targetOffset < 0f && canSwipeTowardsLeft()

        offsetState.value += if (isAllowed) delta else delta / 10
    }

    internal suspend fun resetOffset() {
        draggableState.drag(MutatePriority.PreventUserInput) {
            isResettingOnRelease = true
            try {
                Animatable(offsetState.value).animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = swipeAnimationDurationMs)
                ) {
                    dragBy(value - offsetState.value)
                }
            } finally {
                isResettingOnRelease = false
            }
        }
    }
}
