package com.jeluchu.composer.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable

fun NavGraphBuilder.composable(
    navItem: NavItem,
    content: @Composable (NavBackStackEntry) -> Unit
) = composable(
    route = navItem.route,
    arguments = navItem.args,
) { content(it) }

inline fun <reified T> NavBackStackEntry.findArg(navArg: NavArgs): T {

    val value = when (navArg.navType) {
        NavType.StringType -> arguments?.getString(navArg.key)
        NavType.IntType -> arguments?.getInt(navArg.key)
        else -> arguments?.getString(navArg.key)
    }

    requireNotNull(value)
    return value as T
}