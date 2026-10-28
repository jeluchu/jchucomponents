package com.jeluchu.jchucomponents.ui.composables.swipe

import kotlin.math.abs

internal data class SwipeActionMeta(
    val value: SwipeAction,
    val isOnRightSide: Boolean
)

internal data class ActionFinder(
    private val left: List<SwipeAction>,
    private val right: List<SwipeAction>
) {
    fun actionAt(offset: Float, totalWidth: Int): SwipeActionMeta? {
        if (offset == 0f || totalWidth <= 0) return null

        val isOnRightSide = offset < 0f
        val actions = if (isOnRightSide) right else left
        val action = actions.actionAt(
            offset = abs(offset).coerceAtMost(totalWidth.toFloat()),
            totalWidth = totalWidth
        )
        return action?.let { SwipeActionMeta(value = it, isOnRightSide = isOnRightSide) }
    }

    private fun List<SwipeAction>.actionAt(offset: Float, totalWidth: Int): SwipeAction? {
        if (isEmpty()) return null

        val totalWeight = sumOf { it.weight }
        var accumulatedWidth = 0.0
        for (action in this) {
            accumulatedWidth += action.weight / totalWeight * totalWidth
            if (offset <= accumulatedWidth) return action
        }
        return last()
    }
}
