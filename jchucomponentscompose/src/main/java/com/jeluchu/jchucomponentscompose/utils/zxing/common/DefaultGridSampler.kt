package com.jeluchu.jchucomponentscompose.utils.zxing.common

import com.jeluchu.jchucomponentscompose.utils.zxing.NotFoundException

class DefaultGridSampler : GridSampler() {

    @Throws(NotFoundException::class)
    override fun sampleGrid(
        image: BitMatrix?,
        dimensionX: Int,
        dimensionY: Int,
        transform: PerspectiveTransform?
    ): BitMatrix {
        if (dimensionX <= 0 || dimensionY <= 0) {
            throw NotFoundException.getNotFoundInstance()
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
            // Quick check to see if points transformed to something inside the image;
            // sufficient to check the endpoints
            checkAndNudgePoints(image!!, points)
            try {
                var x1 = 0
                while (x1 < max) {
                    if (image[points[x1].toInt(), points[x1 + 1].toInt()]) {
                        // Black(-ish) pixel
                        bits[x1 / 2] = y
                    }
                    x1 += 2
                }
            } catch (aioobe: ArrayIndexOutOfBoundsException) {
                // This feels wrong, but, sometimes if the finder patterns are misidentified, the resulting
                // transform gets "twisted" such that it maps a straight line of points to a set of points
                // whose endpoints are in bounds, but others are not. There is probably some mathematical
                // way to detect this about the transformation that I don't know yet.
                // This results in an ugly runtime exception despite our clever checks above -- can't have
                // that. We could check each point's coordinates but that feels duplicative. We settle for
                // catching and wrapping ArrayIndexOutOfBoundsException.
                throw NotFoundException.getNotFoundInstance()
            }
        }
        return bits
    }
}