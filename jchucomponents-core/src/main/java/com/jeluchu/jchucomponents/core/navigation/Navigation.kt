/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.core.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry

inline fun <reified T> NavBackStackEntry.findArg(key: String): T {
    val value = arguments?.get(key)
    requireNotNull(value)
    return value as T
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED