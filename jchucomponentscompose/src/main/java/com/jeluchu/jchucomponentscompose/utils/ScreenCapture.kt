package com.jeluchu.jchucomponentscompose.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import android.view.Window
import com.jeluchu.jchucomponentscompose.core.extensions.packageutils.buildIsOAndUp

/**
 *
 * Author: @Jeluchu
 *
 * With this class you will be able to take screenshots
 * of the current screen quickly and easily
 *
 */

class ScreenCapture {

    fun captureView(view: View, window: Window, bitmapCallback: (Bitmap) -> Unit) {
        if (buildIsOAndUp) {

            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val location = IntArray(2)
            view.getLocationInWindow(location)
            PixelCopy.request(
                window,
                Rect(location[0], location[1], location[0] + view.width, location[1] + view.height),
                bitmap,
                {
                    if (it == PixelCopy.SUCCESS) {
                        bitmapCallback.invoke(bitmap)
                    }
                }, Handler(Looper.getMainLooper())
            )

        } else {

            val tBitmap = Bitmap.createBitmap(
                view.width, view.height, Bitmap.Config.RGB_565
            )
            val canvas = Canvas(tBitmap)
            view.draw(canvas)
            canvas.setBitmap(null)
            bitmapCallback.invoke(tBitmap)

        }
    }

}