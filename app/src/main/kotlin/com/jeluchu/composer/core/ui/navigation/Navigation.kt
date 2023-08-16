package com.jeluchu.composer.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.jeluchu.pay.playstore.SubscriptionsHelper

@Composable
fun Navigation() = ProvideNavHostController { navHost ->
    ProvideNavigate { nav ->
        NavHost(
            navController = navHost,
            startDestination = Feature.DASHBOARD.route
        ) {
            dashboardNav(nav)
            buttonsNav(nav)
            progressNav(nav)
        }
    }
}
