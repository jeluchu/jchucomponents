package com.jeluchu.jchucomponents.ui.composables.swipe

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/** An action revealed by [SwipeableActionsBox]. */
class SwipeAction(
    val onSwipe: () -> Unit,
    val icon: @Composable () -> Unit,
    val background: Color,
    val weight: Double = 1.0,
    val isUndo: Boolean = false
) {
    init {
        require(weight > 0.0 && weight.isFinite()) {
            "Swipe action weight must be finite and greater than zero"
        }
    }

    fun copy(
        onSwipe: () -> Unit = this.onSwipe,
        icon: @Composable () -> Unit = this.icon,
        background: Color = this.background,
        weight: Double = this.weight,
        isUndo: Boolean = this.isUndo
    ): SwipeAction = SwipeAction(onSwipe, icon, background, weight, isUndo)
}

/** Creates an action whose icon is supplied as a painter. */
fun SwipeAction(
    onSwipe: () -> Unit,
    icon: Painter,
    tint: Color,
    background: Color,
    contentDescription: String? = null,
    weight: Double = 1.0,
    isUndo: Boolean = false
): SwipeAction = SwipeAction(
    onSwipe = onSwipe,
    icon = {
        Icon(
            modifier = Modifier.padding(16.dp),
            painter = icon,
            tint = tint,
            contentDescription = contentDescription
        )
    },
    background = background,
    weight = weight,
    isUndo = isUndo
)

/** Creates an action whose icon is supplied as an image vector. */
fun SwipeAction(
    onSwipe: () -> Unit,
    icon: ImageVector,
    tint: Color,
    background: Color,
    contentDescription: String? = null,
    weight: Double = 1.0,
    isUndo: Boolean = false
): SwipeAction = SwipeAction(
    onSwipe = onSwipe,
    icon = {
        Icon(
            modifier = Modifier.padding(16.dp),
            imageVector = icon,
            tint = tint,
            contentDescription = contentDescription
        )
    },
    background = background,
    weight = weight,
    isUndo = isUndo
)
