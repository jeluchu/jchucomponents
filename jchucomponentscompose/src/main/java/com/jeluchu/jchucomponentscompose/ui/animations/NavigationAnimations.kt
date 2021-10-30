package com.jeluchu.jchucomponentscompose.ui.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

@ExperimentalAnimationApi
val enterTransition: (AnimatedContentScope<String>.(
    initial: NavBackStackEntry,
    target: NavBackStackEntry
) -> EnterTransition?) = { _, _ ->
    slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    ) + fadeIn(animationSpec = tween(200))
}

@ExperimentalAnimationApi
val exitTransition: (AnimatedContentScope<String>.(
    initial: NavBackStackEntry,
    target: NavBackStackEntry
) -> ExitTransition?) = { _, _ ->
    slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutLinearInEasing
        )
    )
}