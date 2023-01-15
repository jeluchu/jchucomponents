/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jeluchu.jchucomponents.ktx.ints.isNotEmpty
import com.jeluchu.jchucomponents.ktx.strings.empty
import com.jeluchu.jchucomponents.ui.composables.images.NetworkImage

/**
 *
 * Author: @Jeluchu
 *
 * This component displays the same design as Facebook stories
 *
 * @param modifier modifier that will be used to added animations (for example)
 * @param title text to be displayed on the chip
 * @param textColor color of title
 * @param circleImage link of the image you want to be displayed as an icon on top storie
 * @param iconMainUrl link of the image you want to be displayed as an image
 * @param iconMainResource drawable id of the resource you want to be displayed as an image
 * @param navigateToScreen action to be performed after pressing
 *
 */

@Composable
fun StoryCard(
    modifier: Modifier = Modifier,
    title: String,
    textColor: Color = Color.Black,
    circleImage: Int,
    iconMainUrl: String = String.empty(),
    iconMainResource: Int = 0,
    navigateToScreen: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .width(130.dp)
            .height(190.dp)
            .padding(4.dp),
        backgroundColor = Color.Gray
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable { navigateToScreen() }
        ) {

            val (profileImg, storyImg, name) = createRefs()

            if (iconMainUrl.isNotEmpty()) {

                NetworkImage(
                    url = iconMainUrl,
                    modifier = Modifier
                        .constrainAs(storyImg) {
                            linkTo(parent.start, parent.end)
                            linkTo(parent.top, parent.bottom)
                        },
                    contentScale = ContentScale.Crop,
                )

            } else if (iconMainResource.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .size(23.dp)
                        .padding(start = 7.dp),
                    painter = painterResource(id = iconMainResource),
                    contentDescription = null
                )
            }

            Text(
                text = title,
                fontSize = 13.sp,
                color = textColor,
                modifier = Modifier
                    .constrainAs(name) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(8.dp)
            )

            Card(
                shape = CircleShape,
                modifier = Modifier
                    .constrainAs(profileImg) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding(8.dp)
                    .width(40.dp)
                    .height(40.dp)
                    .border(
                        width = 2.dp,
                        color = Color.Blue,
                        shape = CircleShape
                    )
                    .padding(4.dp)
            ) {

                Image(
                    painter = painterResource(circleImage),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )

            }

        }


    }

}
