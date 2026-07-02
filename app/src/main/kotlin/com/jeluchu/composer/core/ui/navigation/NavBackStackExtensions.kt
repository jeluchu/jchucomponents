package com.jeluchu.composer.core.ui.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateTo(route: NavKey) {
    add(route)
}

fun NavBackStack<NavKey>.goBack() {
    if (size > 1) removeLastOrNull()
}
