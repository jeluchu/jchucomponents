/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.foundation.canva

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.nativePaint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.R
import com.jeluchu.jchucomponents.ui.extensions.getBitmapFromVectorDrawable

@Composable
fun CanvasBackground(
    modifier: Modifier = Modifier,
    with: Int,
    height: Int,
    drawable: Int
) {
    val context = LocalContext.current

    Canvas(
        modifier = modifier
    ) {
        val pattern =
            context
                .getBitmapFromVectorDrawable(
                    with,
                    height,
                    drawable
                ).asImageBitmap()

        val paint =
            Paint().nativePaint.apply {
                isAntiAlias = true
                shader = ImageShader(pattern, TileMode.Repeated, TileMode.Repeated)
            }

        drawIntoCanvas {
            it.nativeCanvas.drawPaint(paint)
        }
        paint.reset()
    }
}

@Preview(showBackground = true)
@Composable
private fun CanvasBackgroundPreview() {
    CanvasBackground(
        modifier = Modifier.size(160.dp),
        with = 32,
        height = 32,
        drawable = R.drawable.ic_deco_jeluchu
    )
}
