/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.qr.qrcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import androidx.annotation.DrawableRes
import com.jeluchu.qr.EncodeHintType
import com.jeluchu.qr.WriterException
import com.jeluchu.qr.qrcode.decoder.ErrorCorrectionLevel
import com.jeluchu.qr.qrcode.encoder.ByteMatrix
import com.jeluchu.qr.qrcode.encoder.Encoder
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class QRCodeWriter {
    private var input: ByteMatrix? = null
    private val radii = FloatArray(8)
    private var imageBloks = 0
    private var imageBlockX = 0
    private var sideQuadSize = 0
    private var imageSize = 0

    @Throws(WriterException::class)
    fun encode(
        contents: String?,
        width: Int,
        height: Int,
        hints: HashMap<EncodeHintType, Any?>,
        bitmap: Bitmap?,
        context: Context?,
        @DrawableRes iconLogo: Int
    ): Bitmap {
        var bitmap1 = bitmap
        require(contents?.isNotEmpty() == true) { "Found empty contents" }
        require(!(width < 0 || height < 0)) { "Requested dimensions are too small: " + width + 'x' + height }
        var errorCorrectionLevel = ErrorCorrectionLevel.L
        var quietZone = QUIET_ZONE_SIZE
        if (hints.containsKey(EncodeHintType.ERROR_CORRECTION)) errorCorrectionLevel =
            ErrorCorrectionLevel.valueOf(hints[EncodeHintType.ERROR_CORRECTION].toString())
        if (hints.containsKey(EncodeHintType.MARGIN)) quietZone =
            hints[EncodeHintType.MARGIN].toString().toInt()

        val code = Encoder.encode(contents.orEmpty(), errorCorrectionLevel, hints)
        input = code.matrix
        checkNotNull(input)
        val inputWidth = input!!.width
        val inputHeight = input!!.height
        for (x in 0 until inputWidth) {
            if (has(x, 0)) sideQuadSize++
            else break
        }
        val qrWidth = inputWidth + quietZone * 2
        val qrHeight = inputHeight + quietZone * 2
        val outputWidth = max(width, qrWidth)
        val outputHeight = max(height, qrHeight)
        val multiple = min(outputWidth / qrWidth, outputHeight / qrHeight)
        val padding = 16
        val size = multiple * inputWidth + padding * 2
        if (bitmap1 == null || bitmap1.width != size) bitmap1 =
            Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap1!!)
        canvas.drawColor(-0x1)
        val blackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        blackPaint.color = -0x1000000
        val rect = GradientDrawable()
        rect.shape = GradientDrawable.RECTANGLE
        rect.cornerRadii = radii
        imageBloks = ((size - 32) / 4.65f / multiple).roundToInt()
        if (imageBloks % 2 != inputWidth % 2) imageBloks++
        imageBlockX = (inputWidth - imageBloks) / 2
        imageSize = imageBloks * multiple - 24
        val imageX = (size - imageSize) / 2
        for (a in 0..2) {
            var x: Int
            var y: Int
            when (a) {
                0 -> {
                    x = padding
                    y = padding
                }
                1 -> {
                    x = size - sideQuadSize * multiple - padding
                    y = padding
                }
                else -> {
                    x = padding
                    y = size - sideQuadSize * multiple - padding
                }
            }
            var r = sideQuadSize * multiple / 3.0f
            Arrays.fill(radii, r)
            rect.setColor(-0x1000000)
            rect.setBounds(x, y, x + sideQuadSize * multiple, y + sideQuadSize * multiple)
            rect.draw(canvas)
            canvas.drawRect(
                (x + multiple).toFloat(),
                (y + multiple).toFloat(),
                (x + (sideQuadSize - 1) * multiple).toFloat(),
                (y + (sideQuadSize - 1) * multiple).toFloat(),
                blackPaint
            )
            r = sideQuadSize * multiple / 4.0f
            Arrays.fill(radii, r)
            rect.setColor(-0x1)
            rect.setBounds(
                x + multiple,
                y + multiple,
                x + (sideQuadSize - 1) * multiple,
                y + (sideQuadSize - 1) * multiple
            )
            rect.draw(canvas)
            r = (sideQuadSize - 2) * multiple / 4.0f
            Arrays.fill(radii, r)
            rect.setColor(-0x1000000)
            rect.setBounds(
                x + multiple * 2,
                y + multiple * 2,
                x + (sideQuadSize - 2) * multiple,
                y + (sideQuadSize - 2) * multiple
            )
            rect.draw(canvas)
        }
        val r = multiple / 2.0f
        var y = 0
        var outputY = padding
        while (y < inputHeight) {
            var x = 0
            var outputX = padding
            while (x < inputWidth) {
                if (has(x, y)) {
                    Arrays.fill(radii, r)
                    if (has(x, y - 1)) {
                        radii[1] = 0F
                        radii[0] = radii[1]
                        radii[3] = 0F
                        radii[2] = radii[3]
                    }
                    if (has(x, y + 1)) {
                        radii[7] = 0F
                        radii[6] = radii[7]
                        radii[5] = 0F
                        radii[4] = radii[5]
                    }
                    if (has(x - 1, y)) {
                        radii[1] = 0F
                        radii[0] = radii[1]
                        radii[7] = 0F
                        radii[6] = radii[7]
                    }
                    if (has(x + 1, y)) {
                        radii[3] = 0F
                        radii[2] = radii[3]
                        radii[5] = 0F
                        radii[4] = radii[5]
                    }
                    rect.setColor(-0x1000000)
                    rect.setBounds(outputX, outputY, outputX + multiple, outputY + multiple)
                    rect.draw(canvas)
                } else {
                    var has = false
                    Arrays.fill(radii, 0f)
                    if (has(x - 1, y - 1) && has(x - 1, y) && has(x, y - 1)) {
                        radii[1] = r
                        radii[0] = radii[1]
                        has = true
                    }
                    if (has(x + 1, y - 1) && has(x + 1, y) && has(x, y - 1)) {
                        radii[3] = r
                        radii[2] = radii[3]
                        has = true
                    }
                    if (has(x - 1, y + 1) && has(x - 1, y) && has(x, y + 1)) {
                        radii[7] = r
                        radii[6] = radii[7]
                        has = true
                    }
                    if (has(x + 1, y + 1) && has(x + 1, y) && has(x, y + 1)) {
                        radii[5] = r
                        radii[4] = radii[5]
                        has = true
                    }
                    if (has) {
                        canvas.drawRect(
                            outputX.toFloat(),
                            outputY.toFloat(),
                            (outputX + multiple).toFloat(),
                            (outputY + multiple).toFloat(),
                            blackPaint
                        )
                        rect.setColor(-0x1)
                        rect.setBounds(outputX, outputY, outputX + multiple, outputY + multiple)
                        rect.draw(canvas)
                    }
                }
                x++
                outputX += multiple
            }
            y++
            outputY += multiple
        }

        val icon = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(
                context?.resources,
                iconLogo,
            ), imageSize, imageSize, false
        )
        canvas.drawBitmap(icon!!, imageX.toFloat(), imageX.toFloat(), null)
        icon.recycle()
        canvas.setBitmap(null)
        return bitmap1
    }

    private fun has(x: Int, y: Int): Boolean {
        if (x >= imageBlockX && x < imageBlockX + imageBloks && y >= imageBlockX && y < imageBlockX + imageBloks) return false
        if ((x < sideQuadSize || x >= input!!.width - sideQuadSize) && y < sideQuadSize) return false

        return if (x < sideQuadSize && y >= input!!.height - sideQuadSize) false
        else x >= 0 && y >= 0 && x < input!!.width && y < input!!.height && input!![x, y].toInt() == 1
    }

    companion object {
        private const val QUIET_ZONE_SIZE = 4
    }
}