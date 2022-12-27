/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.ui.composables.images.NetworkImage
import com.jeluchu.jchucomponents.ui.migration.modifier.cornerRadius

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
 * @param description text to be displayed on the card
 *
 */

@Composable
fun PostCardTop(
    modifier: Modifier = Modifier,
    image: String,
    title: String,
    description: String,
    style: TextStyle = LocalTextStyle.current
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .padding(16.dp)
) {
    NetworkImage(
        modifier = Modifier
            .heightIn(min = 180.dp)
            .fillMaxWidth()
            .clip(10.cornerRadius()),
        url = image
    )

    Text(
        modifier = Modifier.padding(
            top = 10.dp,
            bottom = 2.dp
        ),
        text = title,
        style = style,
        fontSize = 20.sp
    )

    Text(
        modifier = Modifier.padding(bottom = 4.dp),
        text = description,
        style = style,
        fontSize = 16.sp
    )
}
