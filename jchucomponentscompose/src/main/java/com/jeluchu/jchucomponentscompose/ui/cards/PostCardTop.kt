package com.jeluchu.jchucomponentscompose.ui.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponentscompose.ui.images.NetworkImage

@Composable
fun PostCardTop(
    title: String,
    image: String,
    description: String,
    modifier: Modifier = Modifier
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