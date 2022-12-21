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
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.core.extensions.strings.empty
import com.jeluchu.jchucomponents.ui.chips.Type
import com.jeluchu.jchucomponents.ui.theme.artichoke
import com.jeluchu.jchucomponents.core.R

@Composable
fun LinearProgressbar(
    number: Float = 5f,
    maxNumber: Float = 10f,
    @DrawableRes icon: Int? = null,
    @DrawableRes img: Int? = null,
    indicatorHeight: Dp = 30.dp,
    backgroundIndicatorColor: Color = Color.LightGray.copy(alpha = 0.3f),
    foregroundIndicatorColor: Color = Color(0xFF35898f),
    foregroundIndicatorAllColor: Color = Color(0xFF7DA88C),
    indicatorPadding: Dp = 20.dp,
    animationDuration: Int = 1000,
    animationDelay: Int = 0,
    style: TextStyle = LocalTextStyle.current
) {

    val numberTimes = remember { mutableStateOf(number) }
    numberTimes.value = number

    val animateNumber = animateFloatAsState(
        targetValue = numberTimes.value,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animationDelay
        )
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        if (icon != null)
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .padding(start = 10.dp),
                imageVector = ImageVector.vectorResource(id = icon),
                tint = if (number != maxNumber) artichoke else foregroundIndicatorAllColor,
                contentDescription = String.empty()
            )
        else if (img != null)
            Image(
                modifier = Modifier
                    .size(25.dp)
                    .padding(start = 15.dp),
                painter = painterResource(id = img),
                contentDescription = null
            )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(indicatorHeight)
                .weight(6f)
                .padding(
                    start = indicatorPadding,
                    end = indicatorPadding,
                    top = 15.dp
                )
        ) {

            // Background indicator
            drawLine(
                color = backgroundIndicatorColor,
                cap = StrokeCap.Round,
                strokeWidth = size.height,
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = size.width, y = 0f)
            )

            // Convert the downloaded percentage into progress (width of foreground indicator)
            val progress = (animateNumber.value / maxNumber) * size.width

            // Foreground indicator
            if (animateNumber.value != 0f)
                drawLine(
                    color = if (number != maxNumber) foregroundIndicatorColor else foregroundIndicatorAllColor,
                    cap = StrokeCap.Round,
                    strokeWidth = size.height,
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = progress, y = 0f)
                )

        }

        Type(
            modifier = Modifier
                .fillMaxWidth()
                .weight(5f)
                .padding(end = 10.dp),
            type = "${number.toInt()}/${maxNumber.toInt()}",
            textAlign = TextAlign.Center,
            style = style
        )

    }

}

@Preview
@Composable
fun LinearProgressbarPreview() {
    LinearProgressbar(
        icon = R.drawable.ic_up_arrow
    )
}