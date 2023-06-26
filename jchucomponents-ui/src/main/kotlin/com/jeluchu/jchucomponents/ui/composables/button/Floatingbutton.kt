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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ktx.strings.empty
import com.jeluchu.jchucomponents.ui.R
import com.jeluchu.jchucomponents.ui.animations.navigation.enterTransition
import com.jeluchu.jchucomponents.ui.animations.navigation.exitTransition
import com.jeluchu.jchucomponents.ui.themes.artichoke
import com.jeluchu.jchucomponents.ui.themes.cosmicLatte

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FloatingButton(
    enabled: Boolean = true,
    isVisible: Boolean = true,
    exit: ExitTransition = scaleOut(),
    enter: EnterTransition = scaleIn(),
    size: FloatingButtonSize = FloatingButtonSize.Medium,
    floatButton: FloatingButtonSettings = FloatingButtonSettings(),
    onClick: () -> Unit = {}
) = AnimatedVisibility(
    visible = isVisible,
    enter = enter,
    exit = exit
) {
    FloatingActionButton(
        modifier = Modifier.size(size.buttonSize),
        containerColor = if (enabled) floatButton.background else floatButton.disabledBackground,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        ),
        shape = RoundedCornerShape(size.shape),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(size.iconSize),
            imageVector = ImageVector.vectorResource(id = floatButton.icon),
            contentDescription = String.empty(),
            tint = if (enabled) floatButton.tint else floatButton.disabledTint
        )
    }
}

@Immutable
class FloatingButtonSettings constructor(
    @DrawableRes val icon: Int = R.drawable.ic_btn_share,
    val tint: Color = cosmicLatte,
    val background: Color = artichoke,
    val disabledBackground: Color = Color.LightGray,
    val disabledTint: Color = Color.Gray,
)

@Immutable sealed class FloatingButtonSize(val buttonSize: Dp, val iconSize: Dp, val shape: Dp) {
    @Immutable object Large : FloatingButtonSize(buttonSize = 64.dp, iconSize = 30.dp, shape = 15.dp)
    @Immutable object Medium : FloatingButtonSize(buttonSize = 48.dp, iconSize = 24.dp, shape = 12.dp)
    @Immutable object Small : FloatingButtonSize(buttonSize = 32.dp, iconSize = 16.dp, shape = 8.dp)
    @Immutable object Border : FloatingButtonSize(buttonSize = 32.dp, iconSize = 24.dp, shape = 8.dp)
    @Immutable class Custom(buttonSize: Dp, iconSize: Dp, shape: Dp) : FloatingButtonSize(buttonSize, iconSize, shape)
}

@Preview(showBackground = true)
@Composable
fun FloatingButtonPreview() {
    Column {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Box(Modifier.weight(1f)) {}
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("Large")
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("Medium")
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("Small")
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("Border")
            }
        }
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enabled", Modifier.weight(1f))
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                FloatingButton(size = FloatingButtonSize.Large)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp), contentAlignment = Alignment.Center
            ) {
                FloatingButton(size = FloatingButtonSize.Medium)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp), contentAlignment = Alignment.Center
            ) {
                FloatingButton(size = FloatingButtonSize.Small)
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                FloatingButton(size = FloatingButtonSize.Border)
            }
        }
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Disable", Modifier.weight(1f))
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                FloatingButton(
                    enabled = false,
                    size = FloatingButtonSize.Large,
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp), contentAlignment = Alignment.Center
            ) {
                FloatingButton(
                    enabled = false,
                    size = FloatingButtonSize.Medium
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp), contentAlignment = Alignment.Center
            ) {
                FloatingButton(
                    enabled = false,
                    size = FloatingButtonSize.Small
                )
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                FloatingButton(
                    enabled = false,
                    size = FloatingButtonSize.Border
                )
            }
        }
    }
}