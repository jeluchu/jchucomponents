/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.chips

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.ui.foundation.text.MarqueeText
import com.jeluchu.jchucomponents.ui.themes.artichoke
import com.jeluchu.jchucomponents.ui.themes.cosmicLatte

@Composable
fun Type(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    colors: TypeColors = TypeColors(),
    textAlign: TextAlign? = null,
    fontWeight: FontWeight? = null,
    fontSize: TextUnit = 12.sp,
    style: TextStyle = LocalTextStyle.current
) = MarqueeText(
    modifier = modifier
        .clip(shape)
        .background(colors.container)
        .padding(10.dp, 2.dp),
    text = text,
    style = style,
    fontSize = fontSize,
    fontWeight = fontWeight,
    gradientEdgeColor = colors.gradientEdge,
    color = colors.content,
    textAlign = textAlign
)

@Immutable
class TypeColors constructor(
    val content: Color = artichoke,
    val gradientEdge: Color = Color.White,
    val container: Color = artichoke.copy(alpha = 0.1f),
)

@ExperimentalFoundationApi
@Preview
@Composable
fun TypePreview() {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("A simple text")
        Type("20/09")
        Text("A long simple text with default gradients")
        Type("The world is a Vampire! And this text is a example text of Marquee Type to check feature")
        Text("A long simple text with custom gradients")
        Type(
            text = "The world is a Vampire! And this text is a example text of Marquee Type to check feature",
            colors = TypeColors(
                container = artichoke,
                content = cosmicLatte,
                gradientEdge = artichoke
            )
        )
        Text("A long simple text with custom shape")
        Type(
            text = "The world is a Vampire! And this text is a example text of Marquee Type to check feature",
            shape = 5.cornerRadius(),
            colors = TypeColors(
                container = artichoke,
                content = cosmicLatte,
                gradientEdge = artichoke
            )
        )

        Text("An example in a Row Composable")
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Type(
                modifier = Modifier.weight(1f),
                text = "The world is a Vampire! And this text is a example text of Marquee Type to check feature",
                shape = 5.cornerRadius(),
                colors = TypeColors(
                    container = artichoke,
                    content = cosmicLatte,
                    gradientEdge = artichoke
                )
            )
            Type(
                modifier = Modifier.weight(1f),
                text = "The world is a Vampire! And this text is a example text of Marquee Type to check feature",
                colors = TypeColors(
                    container = artichoke,
                    content = cosmicLatte,
                    gradientEdge = artichoke
                )
            )
        }
    }
}