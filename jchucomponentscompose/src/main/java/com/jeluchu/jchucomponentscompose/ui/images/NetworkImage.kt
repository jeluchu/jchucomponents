package com.jeluchu.jchucomponentscompose.ui.images

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty


@Composable
fun NetworkImage(
    url: String?,
    modifier: Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {

    Image(
        painter = rememberImagePainter(
            data = url ?: String.empty(),
            builder = {
                crossfade(true)
            }
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )

}
