/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.progress

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.core.extensions.strings.empty
import com.jeluchu.jchucomponents.ui.modifier.noRippleClickable
import com.jeluchu.jchucomponents.ui.theme.artichoke

@Composable
fun CircularProgressbar(
    number: Float = 5f,
    maxNumber: Float = 10f,
    @DrawableRes icon: Int? = null,
    @DrawableRes img: Int? = null,
    size: Dp = 45.dp,
    indicatorThickness: Dp = 10.dp,
    animationDuration: Int = 1000,
    animationDelay: Int = 0,
    foregroundIndicatorColor: Color = Color(0xFF35898f),
    foregroundIndicatorAllColor: Color = Color(0xFF7DA88C),
    backgroundIndicatorColor: Color = Color.LightGray.copy(alpha = 0.3f),
    onClick: (MutableState<Float>, Float) -> Unit = { _, _ -> }
) {

    // It remembers the number value
    val numberTimes = remember { mutableStateOf(0f) }

    // Number Animation
    val animateNumber = animateFloatAsState(
        targetValue = numberTimes.value,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animationDelay
        )
    )

    // This is to start the animation when the activity is opened
    LaunchedEffect(Unit) {
        numberTimes.value = number
    }

    Box(
        modifier = Modifier
            .padding(5.dp)
            .size(size = size)
            .noRippleClickable { onClick(numberTimes, maxNumber) },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(size = size)) {

            // Background circle
            drawCircle(
                color = backgroundIndicatorColor,
                radius = size.toPx() / 2,
                style = Stroke(width = indicatorThickness.toPx(), cap = StrokeCap.Round)
            )

            val sweepAngle = (animateNumber.value / maxNumber) * 360

            // Foreground circle
            drawArc(
                color = if (number != maxNumber) foregroundIndicatorColor else foregroundIndicatorAllColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(indicatorThickness.toPx(), cap = StrokeCap.Round)
            )
        }

        if (icon != null)
            Icon(
                modifier = Modifier.size(25.dp),
                imageVector = ImageVector.vectorResource(id = icon),
                tint = if (number != maxNumber) artichoke else foregroundIndicatorAllColor,
                contentDescription = String.empty()
            )
        else if (img != null)
            Image(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = img),
                contentDescription = null
            )

    }

}

@Preview
@Composable
fun CircularProgressbarPreview() {
    CircularProgressbar()
}