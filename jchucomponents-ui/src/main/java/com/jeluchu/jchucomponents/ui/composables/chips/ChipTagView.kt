/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.chips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ktx.ints.isNotEmpty
import com.jeluchu.jchucomponents.ktx.strings.empty
import com.jeluchu.jchucomponents.ui.composables.images.NetworkImage

/**
 *
 * Author: @Jeluchu
 *
 * This component is similar to the Chips,
 * in which you can display a text or a text and an icon
 *
 * @sample ChipTagViewPreview
 *
 * @param modifier modifier that will be used to change the color, size...
 * @param title text to be displayed on the chip
 * @param textColor color of the text to be displayed inside the chip
 * @param isIconShow in case you want to show an icon or not (by default it is deactivated)
 * @param iconUrl link of the image you want to be displayed as an icon on the chip
 * @param iconResource drawable id of the resource you want to be displayed as an icon on the chip
 * @param iconTintColor color of the icon (only if [iconResource] is being used) displayed on the chip
 * @param modifierIcon custom modifier for the displayed icon (currently there is a default padding)
 *
 */

@Composable
fun ChipTagView(
    modifier: Modifier,
    title: String,
    textColor: Color,
    isIconShow: Boolean = false,
    iconUrl: String = String.empty(),
    iconResource: Int = 0,
    iconTintColor: Color = Color.White,
    modifierIcon: Modifier = Modifier.padding(8.dp, 6.dp, 12.dp, 6.dp),
) {
    Box(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier.wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (isIconShow) {

                Column {

                    if (iconUrl.isNotEmpty()) {
                        NetworkImage(
                            modifier = Modifier
                                .size(23.dp)
                                .padding(start = 7.dp),
                            contentScale = ContentScale.Fit,
                            url = iconUrl
                        )
                    } else if (iconResource.isNotEmpty()) {
                        Icon(
                            modifier = Modifier
                                .size(23.dp)
                                .padding(start = 7.dp),
                            painter = painterResource(id = iconResource),
                            tint = iconTintColor,
                            contentDescription = null
                        )
                    }

                }

            }

            Column {
                Text(
                    text = title,
                    modifier = if (isIconShow) modifierIcon else Modifier.padding(
                        12.dp,
                        6.dp,
                        12.dp,
                        6.dp
                    ),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor
                )
            }

        }

    }
}

@Preview
@Composable
fun ChipTagViewPreview() {
    ChipTagView(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Blue.copy(.2f)),
        title = "Name",
        textColor = Color.White
    )
}
