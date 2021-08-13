package com.jeluchu.jchucomponentscompose.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jeluchu.jchucomponentscompose.ui.images.NetworkImage

@Composable
fun StoryCard(
    name: String,
    image: String,
    iconDebut: String,
    isDebut: Boolean = false,
    bgDebut: Color = Color.Black,
    navigateToScreen: () -> Unit
) {

    Column {

        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
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

                val (profileImg, storyImg) = createRefs()

                NetworkImage(
                    url = image,
                    modifier = Modifier.constrainAs(storyImg) {
                        linkTo(parent.start, parent.end)
                        linkTo(parent.top, parent.bottom)
                    }
                )

                if (isDebut) {

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(bottomEnd = 20.dp))
                            .background(bgDebut)
                            .constrainAs(profileImg) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            },
                    ) {

                        Row(
                            modifier = Modifier.wrapContentWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column {
                                NetworkImage(
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(start = 8.dp),
                                    url = iconDebut
                                )
                            }

                            Column {
                                Text(
                                    text = "Debut",
                                    modifier = Modifier.padding(8.dp, 6.dp, 12.dp, 6.dp),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.overline,
                                    textAlign = TextAlign.Start
                                )
                            }

                        }


                    }

                }

            }

        }

        Text(
            text = name,
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier.padding(7.dp),
            fontWeight = FontWeight.Bold
        )

    }

}
