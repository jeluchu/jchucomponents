package com.jeluchu.jchucomponentscompose.ui.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 *
 * Author: @Jeluchu
 *
 * This component is used to upload images via network
 *
 * @param url link to image requiring internet (based on Coil)
 * @param modifier custom modifier for the displayed icon (currently there is a default padding)
 * @param contentScale type of scale for the image
 *
 */

@Deprecated("isOriginalSize param not use for next version")
@Composable
fun NetworkImage(
    url: Any,
    modifier: Modifier = Modifier,
    isOriginalSize: Boolean = false,
    isCrossfade: Boolean = true,
    isAllowHardware: Boolean = true,
    contentScale: ContentScale = ContentScale.Crop
) {

    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(isCrossfade)
            .allowHardware(isAllowHardware)
            .build(),
        contentScale = contentScale,
        contentDescription = null
    )

}
