/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.ui.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Path
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen

/**
 *
 * Author: @Jeluchu
 *
 * This class allows you to quickly and easily
 * implement a Splash Screen with the new Jetpack API
 *
 * @param splashScreen you must pass by parameter Activity.installSplashScreen() from [SplashScreen]
 * @param defaultExitDuration you will be able to spend a certain amount of time
 * for the duration of the animation. By default it is set to 300
 *
 */

@SuppressLint("CustomSplashScreen")
class SplashScreenController(
    private val splashScreen: SplashScreen,
    private val defaultExitDuration: Long = 300,
) {

    fun customizeSplashScreenExit(
        keys: List<SplashAnimations>,
        onExitExtraActions: () -> Unit = {}
    ) =
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            val onExit = {
                splashScreenViewProvider.remove()
                onExitExtraActions()
            }
            showSplashExitAnimator(splashScreenViewProvider.view, keys, onExit)
            showSplashIconExitAnimator(splashScreenViewProvider.iconView, onExit)
        }

    private fun showSplashExitAnimator(
        splashScreenView: View,
        keys: List<SplashAnimations>,
        onExit: () -> Unit = {}
    ) {

        AnimatorSet().run {
            duration = defaultExitDuration
            interpolator = AnticipateInterpolator()
            splashScreenView.apply {
                playTogether(getAnimation(keys, splashScreenView).toList())
            }
            doOnEnd { onExit() }
            start()
        }

    }

    private fun showSplashIconExitAnimator(iconView: View, onExit: () -> Unit = {}) {

        val alphaOut = ObjectAnimator.ofFloat(
            iconView,
            View.ALPHA,
            1f,
            0f
        )

        val scaleOut = ObjectAnimator.ofFloat(
            iconView,
            View.SCALE_X,
            View.SCALE_Y,
            Path().apply {
                moveTo(1.0f, 1.0f)
                lineTo(0.3f, 0.3f)
            }
        )

        val slideUp = ObjectAnimator.ofFloat(
            iconView,
            View.TRANSLATION_Y,
            0f,
            -(iconView.height).toFloat() * 2.25f
        )

        AnimatorSet().run {
            interpolator = AnticipateInterpolator()
            duration = defaultExitDuration

            playTogether(alphaOut, scaleOut, slideUp)
            doOnEnd { onExit() }
            start()
        }

    }

    private fun getAnimation(
        keys: List<SplashAnimations>,
        splashScreenView: View
    ): MutableList<ObjectAnimator> {

        val listAnimations: MutableList<ObjectAnimator> = mutableListOf()

        for (key in keys) {
            val animation = when (key) {
                SplashAnimations.SlideUp -> ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.height.toFloat()
                )
                SplashAnimations.SlideLeft -> ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_X,
                    0f,
                    -splashScreenView.width.toFloat()
                )
                SplashAnimations.ScaleXOut -> ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.SCALE_X,
                    1.0f,
                    0f
                )
                SplashAnimations.AlphaOut -> ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.ALPHA,
                    1f,
                    0f
                )
                SplashAnimations.ScaleOut -> ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.SCALE_X,
                    View.SCALE_Y,
                    Path().apply {
                        moveTo(1.0f, 1.0f)
                        lineTo(0f, 0f)
                    }
                )
            }

            listAnimations.add(animation)

        }

        return listAnimations

    }

}

enum class SplashAnimations {
    SlideUp,
    SlideLeft,
    ScaleXOut,
    AlphaOut,
    ScaleOut
}