package com.jeluchu.jchucomponentscompose.ui.canvas

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import com.jeluchu.jchucomponentscompose.core.extensions.context.getBitmapFromVectorDrawable

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
            context.getBitmapFromVectorDrawable(
                with, height,
                drawable
            )?.asImageBitmap() ?: Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ).asImageBitmap()

        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            shader = ImageShader(pattern, TileMode.Repeated, TileMode.Repeated)
        }

        drawIntoCanvas {
            it.nativeCanvas.drawPaint(paint)
        }
        paint.reset()
    }

}