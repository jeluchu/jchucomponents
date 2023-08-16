package com.jeluchu.composer.core.ui.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.jeluchu.jchucomponents.ktx.navigation.lifecycleIsResumed

class Destinations(private val navController: NavHostController) {

    private fun String.navigate() = navController.navigate(this)

    val goToDashboard: () -> Unit = { Feature.DASHBOARD.route.navigate() }
    val goToButtons: () -> Unit = { Feature.BUTTONS.route.navigate() }
    val goToProgress: () -> Unit = { Feature.PROGRESS.route.navigate() }
    val goToFloatingButtons: () -> Unit = { Feature.FLOATING_BUTTONS.route.navigate() }
    val goToLinearProgress: () -> Unit = { Feature.LINEAR_PROGRESS.route.navigate() }
    val goToIconProgress: () -> Unit = { Feature.ICON_PROGRESS.route.navigate() }

    val goBack: (from: NavBackStackEntry) -> Unit = { from ->
        if (from.lifecycleIsResumed()) navController.popBackStack()
    }
}