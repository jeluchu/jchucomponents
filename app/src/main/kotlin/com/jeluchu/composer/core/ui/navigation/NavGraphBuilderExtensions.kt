package com.jeluchu.composer.core.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.jeluchu.composer.features.dashboard.view.MainView
import com.jeluchu.composer.core.utils.DestinationsIds
import com.jeluchu.composer.core.utils.NavigationIds
import com.jeluchu.composer.features.bottons.view.ButtonsView
import com.jeluchu.composer.features.progress.view.ProgressView
import com.jeluchu.jchucomponents.ui.composables.button.FloatingButtonPreview
import com.jeluchu.jchucomponents.ui.composables.progress.IconProgressbarPreview
import com.jeluchu.jchucomponents.ui.composables.progress.LinearProgressbarPreview

fun NavGraphBuilder.dashboardNav(nav: Destinations) {
    composable(Feature.DASHBOARD.nav) {
        MainView { id ->
            when(id) {
                DestinationsIds.buttons -> nav.goToButtons()
                DestinationsIds.progress -> nav.goToProgress()
            }
        }
    }
}

fun NavGraphBuilder.buttonsNav(nav: Destinations) {
    navigation(
        startDestination = Feature.BUTTONS.route,
        route = NavigationIds.buttons
    ) {
        composable(Feature.BUTTONS.nav) {
            ButtonsView { id ->
                when(id) {
                    DestinationsIds.floatingButton -> nav.goToFloatingButtons()
                }
            }
        }

        composable(Feature.FLOATING_BUTTONS.nav) {
            FloatingButtonPreview()
        }
    }
}

fun NavGraphBuilder.progressNav(nav: Destinations) {
    navigation(
        startDestination = Feature.PROGRESS.route,
        route = NavigationIds.progress
    ) {
        composable(Feature.PROGRESS.nav) {
            ProgressView { id ->
                when(id) {
                    DestinationsIds.linearProgress -> nav.goToLinearProgress()
                    DestinationsIds.iconProgress -> nav.goToIconProgress()
                }
            }
        }

        composable(Feature.LINEAR_PROGRESS.nav) {
            LinearProgressbarPreview()
        }

        composable(Feature.ICON_PROGRESS.nav) {
            IconProgressbarPreview()
        }
    }
}