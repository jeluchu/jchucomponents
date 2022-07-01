/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.utils.zxing.common

import com.jchucomponents.utils.zxing.NotFoundException

class DefaultGridSampler : GridSampler() {

    @Throws(NotFoundException::class)
    override fun sampleGrid(
        image: BitMatrix?,
        dimensionX: Int,
        dimensionY: Int,
        transform: PerspectiveTransform?
    ): BitMatrix {
        if (dimensionX <= 0 || dimensionY <= 0) {
            throw NotFoundException.notFoundInstance
        }
        val bits = BitMatrix(dimensionX, dimensionY, 1)
        val points = FloatArray(2 * dimensionX)
        for (y in 0 until dimensionY) {
            val max = points.size
            val iValue = y + 0.5f
            var x = 0
            while (x < max) {
                points[x] = (x / 2).toFloat() + 0.5f
                points[x + 1] = iValue
                x += 2
            }
            transform!!.transformPoints(points)
            checkAndNudgePoints(image!!, points)
            runCatching {
                var x1 = 0
                while (x1 < max) {
                    if (image[points[x1].toInt(), points[x1 + 1].toInt()]) bits[x1 / 2] = y
                    x1 += 2
                }
            }
        }
        return bits
    }
}