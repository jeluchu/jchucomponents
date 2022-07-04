/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.ui.toolbars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun TopBar(
    modifier: Modifier,
    titleBar: String,
    colorBar: Color,
    iconAction: Int = 0,
    leftIcon: Int,
    navigateToCustomAction: () -> Unit = {},
    navigateToBackScreen: () -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = modifier,
        color = colorBar
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            ConstraintLayout(
                modifier = Modifier.height(52.dp)
            ) {

                val (box, title) = createRefs()

                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                bottomStart = 0.dp,
                                topEnd = 6.dp,
                                bottomEnd = 6.dp
                            )
                        )
                        .background(Color.White)
                        .constrainAs(box) {
                            start.linkTo(parent.start, margin = 0.dp)
                            top.linkTo(parent.top, margin = 5.dp)
                            bottom.linkTo(parent.bottom, margin = 5.dp)
                        }
                ) {

                    Row {

                        IconButton(
                            modifier = Modifier.then(Modifier.size(40.dp)),
                            onClick = {},
                        ) {
                            Icon(
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { interactionSource },
                                    onClick = { navigateToBackScreen() }
                                ),
                                painter = painterResource(id = leftIcon),
                                contentDescription = null
                            )
                        }

                        if (iconAction != 0) {

                            IconButton(
                                modifier = Modifier.then(Modifier.size(40.dp)),
                                onClick = {},
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { interactionSource },
                                            onClick = { navigateToCustomAction() }
                                        ),
                                    painter = painterResource(id = iconAction),
                                    contentDescription = null
                                )
                            }

                        }

                    }

                }

                Spacer(modifier = Modifier.fillMaxWidth())

                Text(
                    titleBar,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.constrainAs(title) {
                        end.linkTo(parent.end, margin = 12.dp)
                        top.linkTo(parent.top, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    })

            }

        }

    }

}