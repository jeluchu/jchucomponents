/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.ui.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponentscompose.core.extensions.ints.isNotEmpty
import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty
import com.jeluchu.jchucomponentscompose.ui.images.NetworkImage

/**
 *
 * Author: @Jeluchu
 *
 * Component displaying
 * information on a custom-designed card
 *
 * @param modifier modifier that will be used to change the color, size...
 * @param title text to be displayed on the card
 * @param textColor color of text that appears on Card
 * @param iconResource drawable id of the resource you want to be displayed as an icon on Card
 * @param iconTintColor color of the icon (only if [iconResource] is being used) displayed on Card
 * @param iconImage link of the image you want to be displayed on Card
 *
 */

@Composable
fun InfoCard(
    modifier: Modifier,
    title: String,
    textColor: Color = Color.Gray,
    iconResource: Int = 0,
    iconTintColor: Color = Color.White,
    iconImage: String = String.empty()
) {

    Box(
        modifier = modifier,
    ) {

        Row(
            modifier = Modifier
                .padding(8.dp)
                .wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                if (iconImage.isNotEmpty()) {
                    NetworkImage(
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(34.dp),
                        url = iconImage
                    )
                } else if (iconResource.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = iconResource),
                        tint = iconTintColor,
                        contentDescription = null
                    )
                }

            }

            Column {
                Text(
                    text = title,
                    modifier = Modifier.padding(12.dp, 6.dp, 12.dp, 6.dp),
                    color = textColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.overline,
                    textAlign = TextAlign.Start
                )
            }

        }


    }
}

@Preview
@Composable
fun InfoCardPreviewLight() {
    InfoCard(
        title = "Info",
        iconImage = "",
        modifier = Modifier,
        textColor = Color.White
    )
}
