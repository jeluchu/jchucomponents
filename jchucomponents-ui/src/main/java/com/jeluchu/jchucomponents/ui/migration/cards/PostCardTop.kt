/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.migration.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.composables.images.NetworkImage

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
    title: String,
    image: String,
    description: String,
) {

    val typography = MaterialTheme.typography

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        val imageModifier = Modifier
            .heightIn(min = 180.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))

        NetworkImage(
            modifier = imageModifier,
            url = image
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = title,
            style = typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = description,
            style = typography.subtitle2,
            modifier = Modifier.padding(bottom = 4.dp)
        )

    }
}
