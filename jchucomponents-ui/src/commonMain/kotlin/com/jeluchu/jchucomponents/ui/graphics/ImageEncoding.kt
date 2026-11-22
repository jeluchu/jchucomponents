package com.jeluchu.jchucomponents.ui.graphics

import androidx.compose.ui.graphics.ImageBitmap

/** Encodes this bitmap as PNG, for sharing or saving. Null when the platform could not encode it. */
expect fun ImageBitmap.toPngBytesOrNull(): ByteArray?
