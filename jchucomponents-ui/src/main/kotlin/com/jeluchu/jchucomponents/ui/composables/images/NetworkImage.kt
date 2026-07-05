/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.images

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import coil3.request.transformations
import coil3.size.Size
import coil3.transform.Transformation
import com.jeluchu.jchucomponents.ui.R
import com.jeluchu.jchucomponents.ui.extensions.toPainter

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
 * @param image link/resource to image requiring (based on Coil)
 * @param modifier custom modifier for the displayed icon (currently there is a default padding)
 * @param contentScale type of scale for the image
 *
 */

@Composable
fun NetworkImage(
    image: Any,
    modifier: Modifier = Modifier,
    isCrossfade: Boolean = true,
    isAllowHardware: Boolean = true,
    alpha: Float = DefaultAlpha,
    @DrawableRes loading: Int = R.drawable.ic_deco_jeluchu,
    @DrawableRes error: Int = R.drawable.ic_deco_jeluchu,
    transformations: List<Transformation> = emptyList(),
    size: Size? = null,
    requestBuilder: ImageRequest.Builder.() -> Unit = {},
    onLoading: (() -> Unit)? = null,
    onSuccess: (() -> Unit)? = null,
    onError: ((Throwable?) -> Unit)? = null,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null
) = AsyncImage(
    modifier = modifier,
    model =
        ImageRequest
            .Builder(LocalContext.current)
            .data(image)
            .apply { size?.let { size(it) } }
            .transformations(transformations)
            .crossfade(isCrossfade)
            .allowHardware(isAllowHardware)
            .apply(requestBuilder)
            .build(),
    alpha = alpha,
    placeholder = loading.toPainter(),
    error = error.toPainter(),
    fallback = error.toPainter(),
    onLoading = { onLoading?.invoke() },
    onSuccess = { onSuccess?.invoke() },
    onError = { onError?.invoke(it.result.throwable) },
    contentScale = contentScale,
    contentDescription = contentDescription
)

@Preview(showBackground = true)
@Composable
private fun NetworkImagePreview() {
    NetworkImage(
        image = R.drawable.ic_deco_jeluchu,
        modifier = Modifier.size(120.dp),
        contentDescription = "Preview image"
    )
}

/**
 *
 * Author: @Jeluchu
 *
 * This component is used to upload images via network
 *
 * @param url link/resource to image requiring (based on Coil)
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
    size: Size? = null,
    requestBuilder: ImageRequest.Builder.() -> Unit = {},
    onLoading: (() -> Unit)? = null,
    onSuccess: (() -> Unit)? = null,
    onError: ((Throwable?) -> Unit)? = null,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null
) = AsyncImage(
    modifier = modifier,
    model =
        ImageRequest
            .Builder(LocalContext.current)
            .data(url)
            .apply { size?.let { size(it) } }
            .transformations(transformations)
            .crossfade(isCrossfade)
            .allowHardware(isAllowHardware)
            .apply(requestBuilder)
            .build(),
    alpha = alpha,
    onLoading = { onLoading?.invoke() },
    onSuccess = { onSuccess?.invoke() },
    onError = { onError?.invoke(it.result.throwable) },
    contentScale = contentScale,
    contentDescription = contentDescription
)
