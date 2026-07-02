package com.jeluchu.jchucomponents.foundation.components

/**
 * Platform-neutral state for progress buttons rendered by Compose or SwiftUI.
 */
public data class JchuProgressButtonState(
    public val title: String,
    public val isLoading: Boolean = false,
    public val isEnabled: Boolean = true,
)
