package com.jeluchu.jchucomponents.foundation.components

/**
 * Platform-neutral state for determinate and indeterminate progress views.
 */
public data class JchuProgressState(
    public val title: String,
    public val value: Double,
    public val maxValue: Double,
    public val isEnabled: Boolean,
    public val isIndeterminate: Boolean,
) {
    public val fraction: Double
        get() = when {
            maxValue <= 0.0 -> 0.0
            else -> (value / maxValue).coerceIn(0.0, 1.0)
        }
}
