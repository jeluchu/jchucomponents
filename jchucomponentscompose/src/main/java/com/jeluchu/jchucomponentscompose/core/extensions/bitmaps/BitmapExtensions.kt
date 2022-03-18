package com.jeluchu.jchucomponentscompose.core.extensions.bitmaps

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun Bitmap?.orEmpty(
    defaultValue: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
): Bitmap = this ?: defaultValue

fun ImageBitmap?.orEmpty(
    defaultValue: ImageBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888).asImageBitmap()
): ImageBitmap = this ?: defaultValue