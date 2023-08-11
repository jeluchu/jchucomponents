package com.jeluchu.composer.core.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavItem(
    internal val feature: Feature,
    private val arguments: List<NavArgs> = emptyList()
) {

    class ContentScreen(feature: Feature) : NavItem(feature)

    class ContentDetails(feature: Feature) : NavItem(feature, listOf(NavArgs.ItemId)) {
        fun createRoute(itemId: String) = "${feature.route}/$itemId"
    }

    class ContentAdded(feature: Feature) : NavItem(feature, listOf(NavArgs.ItemAdded)) {
        fun createRoute(itemId: Int) = "${feature.route}/$itemId"
    }

    class ContentDetailType(feature: Feature) : NavItem(
        feature, listOf(NavArgs.ItemType, NavArgs.ItemId)
    ) {
        fun createRoute(itemType: String, itemId: String) = "${feature.route}/$itemType/$itemId"
    }

    class ContentTypeName(feature: Feature) : NavItem(
        feature, listOf(NavArgs.ItemType, NavArgs.ItemId, NavArgs.ItemName)
    ) {
        fun createRoute(itemType: String, itemId: String, itemName: String) =
            "${feature.route}/$itemType/$itemId/$itemName"
    }

    val route = run {
        val argValues = arguments.map { "{${it.key}}" }
        listOf(feature.route)
            .plus(argValues)
            .joinToString("/")
    }

    val args = arguments.map {
        navArgument(it.key) { type = it.navType }
    }
}

enum class NavArgs(
    val key: String,
    val navType: NavType<*>
) {
    ItemId("itemId", NavType.StringType),
    ItemAdded("itemAdded", NavType.IntType),
    ItemType("itemType", NavType.StringType),
    ItemName("itemName", NavType.StringType)
}