package com.jeluchu.jchucomponentscompose.ui.images

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import coil.size.OriginalSize

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

@Composable
fun NetworkImage(
    url: Any,
    modifier: Modifier = Modifier,
    isOriginalSize: Boolean = false,
    isCrossfade: Boolean = true,
    isAllowHardware: Boolean = true,
    contentScale: ContentScale = ContentScale.Crop
) {

    Image(
        painter = rememberImagePainter(
            data = url,
            builder = {
                if (isOriginalSize) size(OriginalSize)
                crossfade(isCrossfade)
                allowHardware(isAllowHardware)
            }
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )

}
