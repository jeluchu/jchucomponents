/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.core.extensions.bitmaps

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 *
 * [Bitmap] Extension to show the original [Bitmap] that we pass from
 * the extension or in case of being null we will pass a default value.
 * This default value can be the value you want or the defined one
 * (this would be an empty one)
 *
 * @param defaultValue [Bitmap] the value we want in case the Bitmap is null,
 * by default it will be an empty one
 *
 */
fun Bitmap?.orEmpty(
    defaultValue: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
): Bitmap = this ?: defaultValue


/**
 *
 * [ImageBitmap] Extension to show the original [ImageBitmap] that we pass from
 * the extension or in case of being null we will pass a default value.
 * This default value can be the value you want or the defined one
 * (this would be an empty one)
 *
 * @param defaultValue [ImageBitmap] the value we want in case the Bitmap is null,
 * by default it will be an empty one
 *
 * @see androidx.compose.ui.graphics.ImageBitmap
 *
 */
fun ImageBitmap?.orEmpty(
    defaultValue: ImageBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888).asImageBitmap()
): ImageBitmap = this ?: defaultValue
