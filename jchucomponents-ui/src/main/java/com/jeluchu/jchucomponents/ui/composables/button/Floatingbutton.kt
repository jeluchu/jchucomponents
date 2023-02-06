/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.button

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ktx.strings.empty
import com.jeluchu.jchucomponents.ui.R
import com.jeluchu.jchucomponents.ui.themes.artichoke
import com.jeluchu.jchucomponents.ui.themes.cosmicLatte

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FloatingButton(
    isVisible: Boolean,
    enter: EnterTransition = scaleIn(),
    exit: ExitTransition = scaleOut(),
    floatButton: FloatingButtonSettings = FloatingButtonSettings()
) = AnimatedVisibility(
    visible = isVisible,
    enter = enter,
    exit = exit
) {
    FloatingActionButton(
        containerColor = floatButton.background,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        ),
        onClick = floatButton.onClick
    ) {
        Icon(
            modifier = Modifier.size(floatButton.size),
            imageVector = ImageVector.vectorResource(id = floatButton.icon),
            contentDescription = String.empty(),
            tint = floatButton.tint
        )
    }
}

@Immutable
class FloatingButtonSettings constructor(
    @DrawableRes val icon: Int = R.drawable.ic_btn_share,
    val size: Dp = 40.dp,
    val tint: Color = cosmicLatte,
    val background: Color = artichoke,
    val onClick: () -> Unit = {}
)