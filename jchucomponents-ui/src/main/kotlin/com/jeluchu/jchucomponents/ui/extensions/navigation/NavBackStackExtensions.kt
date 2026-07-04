package com.jeluchu.jchucomponents.ui.extensions.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

/**
 * Adds [screen] to the end of this Navigation 3 back stack.
 */
fun <T : NavKey> NavBackStack<T>.navigateTo(screen: T) {
    add(screen)
}

/**
 * Adds [screen] only when it is not already the current destination.
 */
fun <T : NavKey> NavBackStack<T>.navigateSingleTop(screen: T): Boolean {
    if (lastOrNull() == screen) return false
    add(screen)
    return true
}

/**
 * Removes the current destination when the stack can safely go back.
 */
fun <T : NavKey> NavBackStack<T>.back(): Boolean {
    if (size <= 1) return false
    removeLastOrNull()
    return true
}

/**
 * Goes back or runs [fallback] when the stack is already at its root.
 */
fun <T : NavKey> NavBackStack<T>.backOr(fallback: () -> Unit): Boolean {
    val handled = back()
    if (!handled) fallback()
    return handled
}

/**
 * Removes destinations until [targetScreen] becomes the current destination.
 */
fun <T : NavKey> NavBackStack<T>.backTo(targetScreen: T): Boolean {
    if (targetScreen !in this) return false

    while (lastOrNull() != targetScreen) {
        removeLastOrNull()
    }
    return true
}

/**
 * Replaces the whole stack with [screen].
 */
fun <T : NavKey> NavBackStack<T>.clearAndNavigateTo(screen: T) {
    clear()
    add(screen)
}
