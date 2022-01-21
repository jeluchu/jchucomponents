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
import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty
import com.jeluchu.jchucomponentscompose.ui.images.NetworkImage
import com.jeluchu.jchucomponentscompose.ui.text.MarqueeText

/**
 *
 * Author: @Jeluchu
 *
 * Component displaying
 * information on a custom-designed card
 *
 * @param modifier modifier that will be used to change the color, size...
 * @param title text to be displayed on the card
 * @param image link of the image you want to be displayed on Card
 * @param iconDebut  link of the icon you want to be displayed on Card
 * @param isDebut status to display a banner with information at the top
 * @param bgDebut background color of the flag state with information
 * @param navigateToScreen action to be performed after pressing
 *
 */

@Composable
fun DebutCard(
    modifier: Modifier = Modifier,
    title: String,
    image: String,
    titleColor: Color = Color.Black,
    debutColor: Color = Color.Black,
    iconDebut: String,
    nameOfDebut: String = String.empty(),
    isDebut: Boolean = false,
    bgDebut: Color = Color.Black,
    debubtAlignment: Alignment = Alignment.TopStart,
    debutShape: RoundedCornerShape = RoundedCornerShape(bottomEnd = 20.dp),
    navigateToScreen: () -> Unit = {}
) {

    Column {

        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = modifier
                .width(130.dp)
                .height(190.dp)
                .padding(4.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = navigateToScreen),
            backgroundColor = Color.Gray
        ) {

            Box {

                NetworkImage(url = image)

                if (isDebut) {

                    Box(
                        modifier = Modifier
                            .clip(debutShape)
                            .background(bgDebut)
                            .align(debubtAlignment),
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
                                    text = nameOfDebut,
                                    modifier = Modifier.padding(8.dp, 6.dp, 12.dp, 6.dp),
                                    color = debutColor,
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

        MarqueeText(
            text = title,
            fontSize = 12.sp,
            color = titleColor,
            modifier = Modifier
                .width(130.dp)
                .padding(7.dp),
            fontWeight = FontWeight.Bold
        )

    }

}