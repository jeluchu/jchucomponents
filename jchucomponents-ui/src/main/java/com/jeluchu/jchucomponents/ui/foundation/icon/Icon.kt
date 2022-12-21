@file:Suppress("unused")

package com.jeluchu.jchucomponents.ui.foundation.icon

import androidx.annotation.StringRes
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.material.Icon
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

/**
 *
 * Icon component that draws [bitmap] using [tint], defaulting to [LocalContentColor]
 *
 * @param bitmap [ImageBitmap] to draw inside this Icon
 * @param contentDescription [StringRes] used by accessibility services to describe what this icon
 * represents
 *
 * This should always be provided unless this icon is used for decorative purposes,
 * and does not represent a meaningful action that a user can take
 *
 * @param modifier optional [Modifier] for this Icon
 * @param tint tint to be applied to [bitmap]
 *
 * If [Color.Unspecified] is provided, then no
 * tint is applied
 *
 */
@Composable
@NonRestartableComposable
fun Icon(
    bitmap: ImageBitmap,
    @StringRes contentDescription: Int?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) = Icon(
    bitmap = bitmap,
    contentDescription = contentDescription?.let { stringResource(id = it) },
    modifier = modifier,
    tint = tint
)

/**
 *
 * Icon component that draws [imageVector] using [tint], defaulting to [LocalContentColor]
 *
 * @param imageVector [ImageVector] to draw inside this Icon
 * @param contentDescription [StringRes] used by accessibility services to describe what this icon
 * represents
 *
 * This should always be provided unless this icon is used for decorative purposes,
 * and does not represent a meaningful action that a user can take
 *
 * @param modifier optional [Modifier] for this Icon
 * @param tint tint to be applied to [imageVector]
 *
 * If [Color.Unspecified] is provided, then no
 * tint is applied
 *
 */
@Composable
@NonRestartableComposable
fun Icon(
    imageVector: ImageVector,
    @StringRes contentDescription: Int?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) = Icon(
    imageVector = imageVector,
    contentDescription = contentDescription?.let { stringResource(it) },
    modifier = modifier,
    tint = tint
)

/**
 *
 * Icon component that draws a [painter] using [tint], defaulting to [LocalContentColor].
 *
 * @param painter [Painter] to draw inside this Icon
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents
 *
 * This should always be provided unless this icon is used for decorative purposes,
 * and does not represent a meaningful action that a user can take
 *
 * This text should be localized, such as by using [androidx.compose.ui.res.stringResource]
 * or similar
 *
 * @param modifier optional [Modifier] for this Icon
 * @param tint tint to be applied to [painter]
 *
 * If [Color.Unspecified] is provided, then no
 * tint is applied
 *
 */
@Composable
fun Icon(
    painter: Painter,
    @StringRes contentDescription: Int?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) = Icon(
    painter = painter,
    contentDescription = contentDescription?.let { stringResource(id = it) },
    modifier = modifier,
    tint = tint
)