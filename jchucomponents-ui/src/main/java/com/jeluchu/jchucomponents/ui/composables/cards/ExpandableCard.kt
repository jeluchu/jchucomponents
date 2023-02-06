/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.cards

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.R

const val ExpandAnimation = 300
const val CollapseAnimation = 300
const val FadeInAnimation = 300
const val FadeOutAnimation = 300

@ExperimentalAnimationApi
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
    modifier: Modifier = Modifier,
    title: String,
    cardBackgroundColor: Color = Color.DarkGray,
    tintIcon: Color = Color.White,
    textColor: Color = Color.White,
    style: TextStyle = LocalTextStyle.current,
    showArrow: Boolean = true,
    content: @Composable () -> Unit,
    onCardArrowClick: () -> Unit,
    expanded: Boolean
) {

    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(targetState = transitionState, label = "transition")
    val cardBgColor by transition.animateColor({
        tween(durationMillis = ExpandAnimation)
    }, label = "bgColorTransition") {
        if (expanded) cardBackgroundColor else cardBackgroundColor
    }
    val cardPaddingHorizontal by transition.animateDp({
        tween(durationMillis = ExpandAnimation)
    }, label = "paddingTransition") {
        10.dp
    }
    val cardRoundedCorners by transition.animateDp({
        tween(
            durationMillis = ExpandAnimation,
            easing = FastOutSlowInEasing
        )
    }, label = "cornersTransition") {
        15.dp
    }
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = ExpandAnimation)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0f else 180f
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = cardPaddingHorizontal,
                vertical = 5.dp
            )
            .clip(RoundedCornerShape(cardRoundedCorners))
            .background(cardBgColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(if (showArrow) 0.85f else 1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { if (!showArrow) onCardArrowClick() }
                    )
            ) {
                Text(
                    text = title,
                    color = textColor,
                    textAlign = TextAlign.Start,
                    style = style,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }
            if (showArrow)
                Column(
                    modifier = Modifier.weight(0.15f)
                ) {
                    CardArrow(
                        degrees = arrowRotationDegree,
                        onClick = onCardArrowClick,
                        tintIcon = tintIcon
                    )
                }
        }
        ExpandableContent(content, expanded)
    }

}

@Composable
fun CardArrow(
    degrees: Float,
    tintIcon: Color,
    @StringRes contentDescription: Int? = null,
    onClick: () -> Unit
) = Icon(
    modifier = Modifier
        .rotate(degrees)
        .clickable(onClick = onClick),
    painter = painterResource(id = R.drawable.ic_up_arrow),
    contentDescription = contentDescription?.let { stringResource(id = it) },
    tint = tintIcon
)

@ExperimentalAnimationApi
@Composable
fun ExpandableContent(
    content: @Composable () -> Unit,
    expanded: Boolean = true
) {

    val enterFadeIn = remember {
        fadeIn(
            animationSpec = TweenSpec(
                durationMillis = FadeInAnimation,
                easing = FastOutLinearInEasing
            )
        )
    }
    val enterExpand = remember { expandVertically(animationSpec = tween(ExpandAnimation)) }
    val exitFadeOut = remember {
        fadeOut(
            animationSpec = TweenSpec(
                durationMillis = FadeOutAnimation,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val exitCollapse = remember { shrinkVertically(animationSpec = tween(CollapseAnimation)) }

    AnimatedVisibility(
        visible = expanded,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) { content() }

}