/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.dragndrop.extensions

import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitLongPressOrCancellation
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import com.jeluchu.jchucomponents.ui.dragndrop.states.ReorderableState

fun Modifier.detectReorder(state: ReorderableState<*>) = detect(state) {
    awaitDragOrCancellation(it)
}

fun Modifier.detectReorderAfterLongPress(state: ReorderableState<*>) = detect(state) {
    awaitLongPressOrCancellation(it)
}

private fun Modifier.detect(
    state: ReorderableState<*>,
    detect: suspend AwaitPointerEventScope.(PointerId) -> PointerInputChange?
) = composed {
    val itemPosition = remember { mutableStateOf(Offset.Zero) }

    Modifier
        .onGloballyPositioned { itemPosition.value = it.positionInWindow() }
        .pointerInput(Unit) {
            awaitEachGesture {
                val down = awaitFirstDown(requireUnconsumed = false)
                val start = detect(down.id)

                if (start != null) {
                    val relativePosition =
                        itemPosition.value - state.layoutWindowPosition.value + start.position
                    state.onDragStart(relativePosition.x.toInt(), relativePosition.y.toInt())
                }
            }
        }
}