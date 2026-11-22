package com.jeluchu.jchucomponents.ui.composables.chips

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/** Amount label preserving the painter-based iNook layout and behavior. */
@Composable
fun JchuAmountCounter(
    amount: String,
    painter: Painter,
    modifier: Modifier = Modifier,
    colors: JchuAmountCounterColors = JchuAmountCounterColors(),
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
) = Row(
    modifier =
        modifier
            .clip(RoundedCornerShape(15.dp))
            .background(colors.containerColor)
            .padding(vertical = 1.dp, horizontal = 3.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
) {
    Image(
        painter = painter,
        modifier = Modifier.size(30.dp).padding(2.dp),
        contentScale = ContentScale.FillWidth,
        contentDescription = null
    )
    Text(
        text = amount,
        color = colors.contentColor,
        style = textStyle,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    )
}

/** Amount label preserving the vector-based iNook layout and behavior. */
@Composable
fun JchuAmountCounter(
    amount: String,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    colors: JchuAmountCounterColors = JchuAmountCounterColors(),
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
) = Row(
    modifier =
        modifier
            .clip(RoundedCornerShape(15.dp))
            .background(colors.containerColor)
            .padding(vertical = 3.dp, horizontal = 3.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
) {
    Icon(
        modifier = Modifier.size(30.dp).padding(2.dp),
        imageVector = imageVector,
        contentDescription = null,
        tint = colors.contentColor
    )
    Text(
        text = amount,
        color = colors.contentColor,
        style = textStyle,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    )
}

@Immutable
class JchuAmountCounterColors(
    val contentColor: Color = Color.Black,
    val containerColor: Color = Color.White
)
