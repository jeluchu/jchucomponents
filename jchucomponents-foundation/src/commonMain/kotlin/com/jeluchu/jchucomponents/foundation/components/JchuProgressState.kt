package com.jeluchu.jchucomponents.foundation.components

/**
 * Platform-neutral state for determinate and indeterminate progress views.
 */
data class JchuProgressState(
    val title: String,
    val value: Double,
    val maxValue: Double,
    val isEnabled: Boolean,
    val isIndeterminate: Boolean
) {
    val fraction: Double
        get() =
            when {
                maxValue <= 0.0 -> 0.0
                else -> (value / maxValue).coerceIn(0.0, 1.0)
            }
}
