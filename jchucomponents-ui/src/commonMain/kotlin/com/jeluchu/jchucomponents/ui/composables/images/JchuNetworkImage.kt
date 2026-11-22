package com.jeluchu.jchucomponents.ui.composables.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade

/**
 * Multiplatform network image backed by Coil 3.
 *
 * The consuming application is responsible for registering a Coil network fetcher for its
 * targets (for example `coil-network-okhttp` on Android and `coil-network-ktor3` on iOS).
 * Platform-specific request options such as bitmap transformations can be applied through
 * [requestBuilder].
 *
 * @param model data to load: a URL, [coil3.Uri], file path, or any type supported by Coil.
 * @param modifier modifier applied to the image.
 * @param isCrossfade whether to fade the image in once loaded.
 * @param alpha opacity applied to the image.
 * @param placeholder painter shown while the image is loading.
 * @param error painter shown when loading fails or [model] is null.
 * @param requestBuilder extra configuration applied to the [ImageRequest].
 * @param onLoading called when loading starts.
 * @param onSuccess called when the image has loaded.
 * @param onError called with the failure cause when loading fails.
 * @param contentScale how the image is scaled inside its bounds.
 * @param contentDescription accessibility description, or null for decorative images.
 */
@Composable
fun JchuNetworkImage(
    model: Any?,
    modifier: Modifier = Modifier,
    isCrossfade: Boolean = true,
    alpha: Float = DefaultAlpha,
    placeholder: Painter? = null,
    error: Painter? = null,
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
            .Builder(LocalPlatformContext.current)
            .data(model)
            .crossfade(isCrossfade)
            .apply(requestBuilder)
            .build(),
    alpha = alpha,
    placeholder = placeholder,
    error = error,
    fallback = error,
    onLoading = { onLoading?.invoke() },
    onSuccess = { onSuccess?.invoke() },
    onError = { onError?.invoke(it.result.throwable) },
    contentScale = contentScale,
    contentDescription = contentDescription
)
