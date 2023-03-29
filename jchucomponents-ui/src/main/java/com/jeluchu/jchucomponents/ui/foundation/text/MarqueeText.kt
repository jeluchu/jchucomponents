/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.foundation.text

import androidx.annotation.StringRes
import androidx.compose.foundation.DefaultMarqueeDelayMillis
import androidx.compose.foundation.DefaultMarqueeVelocity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ktx.compose.toStringRes

/**
 *
 *  Author: @Jeluchu
 *
 *  Auto scrollable text to display the content that
 *  is over passed to the view (This design element is estimated
 *  to be temporary until the Jetpack Compose team releases an official solution
 *  for this behavior that exists in XML)
 *
 * @param text: es un texto
 * @param modifier modifier that will be used to change the color, size...
 * @param gradientEdgeColor: es un texto
 * @param color [Color] to apply to the text
 * @param fontSize the size of glyphs to use when painting the text. See [TextStyle.fontSize]
 * @param fontStyle the typeface variant to use when drawing the letters (e.g., italic)
 * @param fontWeight the typeface thickness to use when painting the text (e.g., [FontWeight.Bold])
 * @param fontFamily the font family to be used when rendering the text. See [TextStyle.fontFamily]
 * @param letterSpacing the amount of space to add between each letter.
 * @param textDecoration the decorations to paint on the text (e.g., an underline).
 * See [TextStyle.textDecoration]
 * @param textAlign the alignment of the text within the lines of the paragraph.
 * See [TextStyle.textAlign]
 * @param lineHeight line height for the [Paragraph] in [TextUnit] unit, e.g. SP or EM.
 * See [TextStyle.lineHeight]
 * @param overflow how visual overflow should be handled.
 * @param softWrap whether the text should break at soft line breaks
 * @param onTextLayout callback that is executed when a new text layout is calculated
 * A [TextLayoutResult] object that callback provides contains paragraph information, size of the
 * text, baselines and other details. The callback can be used to add additional decoration or
 * functionality to the text
 * @param style style configuration for the text such as color, font, line height etc.
 *
 */

fun ContentDrawScope.drawFadedEdge(
    leftEdge: Boolean,
    edgeWidth: Dp
) {
    val edgeWidthPx = edgeWidth.toPx()
    drawRect(
        topLeft = Offset(if (leftEdge) 0f else size.width - edgeWidthPx, 0f),
        size = Size(edgeWidthPx, size.height),
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startX = if (leftEdge) 0f else size.width,
            endX = if (leftEdge) edgeWidthPx else size.width - edgeWidthPx
        ),
        blendMode = BlendMode.DstIn
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@NonRestartableComposable
fun MarqueeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = TextAlign.Center,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    iterations: Int = Int.MAX_VALUE,
    edgeWidthGradient: Dp = 10.dp,
    marqueeSpacing: MarqueeSpacing = MarqueeSpacing(30.dp),
    delayMillis: Int = DefaultMarqueeDelayMillis,
    animationMode: MarqueeAnimationMode = MarqueeAnimationMode.Immediately,
    velocity: Dp = DefaultMarqueeVelocity,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) = Text(
    text = text,
    modifier = modifier
        .fillMaxWidth()
        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithContent {
            drawContent()
            drawFadedEdge(leftEdge = true, edgeWidth = edgeWidthGradient)
            drawFadedEdge(leftEdge = false, edgeWidth = edgeWidthGradient)
        }
        .basicMarquee(
            animationMode = animationMode,
            iterations = iterations,
            delayMillis = delayMillis,
            spacing = marqueeSpacing,
            velocity = velocity
        ),
    color = color,
    fontSize = fontSize,
    fontStyle = fontStyle,
    fontWeight = fontWeight,
    fontFamily = fontFamily,
    letterSpacing = letterSpacing,
    textDecoration = textDecoration,
    textAlign = textAlign,
    lineHeight = lineHeight,
    overflow = overflow,
    softWrap = softWrap,
    maxLines = maxLines,
    onTextLayout = onTextLayout,
    style = style
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
@NonRestartableComposable
fun MarqueeText(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = TextAlign.Center,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    iterations: Int = Int.MAX_VALUE,
    edgeWidthGradient: Dp = 10.dp,
    marqueeSpacing: MarqueeSpacing = MarqueeSpacing(30.dp),
    delayMillis: Int = DefaultMarqueeDelayMillis,
    animationMode: MarqueeAnimationMode = MarqueeAnimationMode.Immediately,
    velocity: Dp = DefaultMarqueeVelocity,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) = Text(
    text = text.toStringRes(),
    modifier = modifier
        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithContent {
            drawContent()
            drawFadedEdge(leftEdge = true, edgeWidth = edgeWidthGradient)
            drawFadedEdge(leftEdge = false, edgeWidth = edgeWidthGradient)
        }
        .basicMarquee(
            animationMode = animationMode,
            iterations = iterations,
            delayMillis = delayMillis,
            spacing = marqueeSpacing,
            velocity = velocity
        ),
    color = color,
    fontSize = fontSize,
    fontStyle = fontStyle,
    fontWeight = fontWeight,
    fontFamily = fontFamily,
    letterSpacing = letterSpacing,
    textDecoration = textDecoration,
    textAlign = textAlign,
    lineHeight = lineHeight,
    overflow = overflow,
    softWrap = softWrap,
    maxLines = maxLines,
    onTextLayout = onTextLayout,
    style = style
)