package com.jeluchu.jchucomponents.ui.composables.premium

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.ui.extensions.modifier.bounceClick
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.ui.themes.primary

@Composable
fun TimeCard(
    title: String,
    amount: String,
    description: String,
    colors: TimeCardColors = TimeCardColors(),
    style: TextStyle = MaterialTheme.typography.subtitle2
) =
    Column(
        modifier = Modifier
            .bounceClick {
            }
            .background(
                shape = 16.cornerRadius(),
                color = colors.containerColor
            )
            .border(
                1.dp,
                color = colors.contentPrimaryColor,
                shape = 16.cornerRadius(),
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(
            text = title,
            color = colors.contentPrimaryColor,
            style = style.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                lineHeight = 25.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 15.dp)
        )
        Divider(
            color = colors.contentPrimaryColor,
            thickness = 1.dp
        )
        Text(
            text = amount,
            color = colors.contentSecondaryColor,
            style = style.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                lineHeight = 25.sp
            ),
            textAlign = TextAlign.Center,
            //modifier = Modifier.padding(15.dp)
        )

        Text(
            text = description,
            color = colors.contentSecondaryColor.copy(.5f),
            style = style.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.W500,
                lineHeight = 25.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 15.dp)
        )
    }

@Immutable
data class TimeCardColors(
    val containerColor: Color = Color.DarkGray,
    val contentPrimaryColor: Color = primary,
    val contentSecondaryColor: Color = Color.White
)


@Preview
@Composable
fun TimeCardPreview() {
    TimeCard(
        title = "Mensual",
        amount = "1,99€",
        description = "por mes"
    )
}