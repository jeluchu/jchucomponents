package com.jeluchu.jchucomponents.foundation.components

/**
 * Platform-neutral state for progress buttons rendered by Compose or SwiftUI.
 */
data class JchuProgressButtonState(
    val title: String,
    val isLoading: Boolean = false,
    val isEnabled: Boolean = true
)
