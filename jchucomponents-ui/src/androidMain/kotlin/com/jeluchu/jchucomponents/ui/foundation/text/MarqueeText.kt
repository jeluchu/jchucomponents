/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.foundation.text

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeDefaults.RepeatDelayMillis
import androidx.compose.foundation.MarqueeDefaults.Velocity
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.jeluchu.jchucomponents.ui.extensions.toStringRes

/** Android resource overload retained for existing Android callers. */
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
    delayMillis: Int = RepeatDelayMillis,
    animationMode: MarqueeAnimationMode = MarqueeAnimationMode.Immediately,
    velocity: Dp = Velocity,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) = MarqueeText(
    text = text.toStringRes(),
    modifier = modifier,
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
    iterations = iterations,
    edgeWidthGradient = edgeWidthGradient,
    marqueeSpacing = marqueeSpacing,
    delayMillis = delayMillis,
    animationMode = animationMode,
    velocity = velocity,
    maxLines = maxLines,
    onTextLayout = onTextLayout,
    style = style
)
