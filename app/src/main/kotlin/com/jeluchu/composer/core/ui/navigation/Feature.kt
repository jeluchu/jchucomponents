package com.jeluchu.composer.core.ui.navigation

enum class Feature(val route: String) {
    DASHBOARD("dashboard"),
    BUTTONS("buttons"),
    FLOATING_BUTTONS("floating_buttons"),
}

val Feature.baseRoute: String
    get() = NavItem.ContentScreen(this).route

val Feature.nav: NavItem.ContentScreen
    get() = NavItem.ContentScreen(this)
