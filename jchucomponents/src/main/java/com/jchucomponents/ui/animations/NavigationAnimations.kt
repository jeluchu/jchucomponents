/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.ui.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

@ExperimentalAnimationApi
val enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition) = {
    slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    ) + fadeIn(animationSpec = tween(200))
}

@ExperimentalAnimationApi
val exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition) = {
    slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutLinearInEasing
        )
    )
}