package com.jeluchu.jchucomponents.ui.graphics

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

actual fun ImageBitmap.toPngBytesOrNull(): ByteArray? =
    runCatching {
        ByteArrayOutputStream().use { out ->
            if (asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, out)) out.toByteArray() else null
        }
    }.getOrNull()
