package com.jeluchu.composer.core.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.jeluchu.composer.features.dashboard.view.MainView
import com.jeluchu.composer.core.utils.DestinationsIds
import com.jeluchu.composer.core.utils.NavigationIds
import com.jeluchu.composer.features.bottons.view.ButtonsView
import com.jeluchu.jchucomponents.ui.composables.button.FloatingButtonPreview

fun NavGraphBuilder.dashboardNav(nav: Destinations) {
    composable(Feature.DASHBOARD.nav) {
        MainView { id ->
            when(id) {
                DestinationsIds.buttons -> nav.goToButtons()
            }
        }
    }
}

fun NavGraphBuilder.buttonsNav(nav: Destinations) {
    navigation(
        startDestination = Feature.DASHBOARD.route,
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