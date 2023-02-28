/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.images

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.Transformation

/**
 *
 * Author: @Jeluchu
 *
 * This component is used to return [ImageRequest.Builder]
 *
 */
fun Context.imageBuilder() = ImageRequest.Builder(this)

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
    isCrossfade: Boolean = true,
    isAllowHardware: Boolean = true,
    alpha: Float = DefaultAlpha,
    transformations: List<Transformation> = emptyList(),
    contentScale: ContentScale = ContentScale.Crop
) = AsyncImage(
    modifier = modifier,
    model = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .transformations(transformations)
        .crossfade(isCrossfade)
        .allowHardware(isAllowHardware)
        .build(),
    alpha = alpha,
    contentScale = contentScale,
    contentDescription = null
)
