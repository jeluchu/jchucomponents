package com.jeluchu.jchucomponents.navigation3

import androidx.navigation3.runtime.NavKey
import kotlin.reflect.KClass

/**
 * Marks a top-level composable as a Navigation 3 destination for KSP code generation.
 *
 * The generated route name defaults to the composable function name. Parameters without a
 * navigation callback annotation become serializable route arguments unless marked with
 * [ScreenKoinViewModel].
 *
 * @param graph the feature graph that owns this destination
 * @param route an optional route type name; defaults to the annotated function name
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Screen(
    val graph: String,
    val route: String = ""
)

/** Supplies a generated destination with a callback that pops the current route from the back stack. */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class BackStackBack

/**
 * Supplies a generated destination with a callback that navigates to another generated screen in
 * the same or another graph in this KSP module.
 *
 * The callback's parameters are passed, in order, to the target screen's route arguments.
 *
 * @param route the generated route type to open
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class NavigateTo(
    val route: KClass<out NavKey>
)

/**
 * Supplies a feature screen with a custom callback implemented by the application.
 *
 * Use this for behavior that is not a generated route transition, such as launching a platform
 * destination or calling app-specific logic. Generated route transitions should use [NavigateTo].
 * The generated entry-provider function exposes the callback so the application can provide it.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class CustomAction

/** Marks a ViewModel parameter to be resolved by Koin in the generated Navigation 3 entry. */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class ScreenKoinViewModel
