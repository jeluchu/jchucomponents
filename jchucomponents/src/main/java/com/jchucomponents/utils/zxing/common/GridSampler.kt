/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.utils.zxing.common

import com.jchucomponents.utils.zxing.NotFoundException

abstract class GridSampler {

    @Throws(NotFoundException::class)
    abstract fun sampleGrid(
        image: BitMatrix?,
        dimensionX: Int,
        dimensionY: Int,
        transform: PerspectiveTransform?
    ): BitMatrix?

    companion object {
        /**
         * @return the current implementation of GridSampler
         */
        var instance: GridSampler = DefaultGridSampler()
            private set

        /**
         *
         * Checks a set of points that have been transformed to sample points on an image against
         * the image's dimensions to see if the point are even within the image.
         *
         *
         * This method will actually "nudge" the endpoints back onto the image if they are found to be
         * barely (less than 1 pixel) off the image. This accounts for imperfect detection of finder
         * patterns in an image where the QR Code runs all the way to the image border.
         *
         *
         * For efficiency, the method will check points from either end of the line until one is found
         * to be within the image. Because the set of points are assumed to be linear, this is valid.
         *
         * @param image image into which the points should map
         * @param points actual points in x1,y1,...,xn,yn form
         * @throws NotFoundException if an endpoint is lies outside the image boundaries
         */
        @JvmStatic
        @Throws(NotFoundException::class)
        protected fun checkAndNudgePoints(
            image: BitMatrix,
            points: FloatArray
        ) {
            val width = image.width
            val height = image.height
            // Check and nudge points from start until we see some that are OK:
            var nudged = true
            val maxOffset = points.size - 1 // points.length must be even
            run {
                var offset = 0
                while (offset < maxOffset && nudged) {
                    val x = points[offset].toInt()
                    val y = points[offset + 1].toInt()
                    if (x < -1 || x > width || y < -1 || y > height) {
                        throw NotFoundException.notFoundInstance
                    }
                    nudged = false
                    if (x == -1) {
                        points[offset] = 0.0f
                        nudged = true
                    } else if (x == width) {
                        points[offset] = (width - 1).toFloat()
                        nudged = true
                    }
                    if (y == -1) {
                        points[offset + 1] = 0.0f
                        nudged = true
                    } else if (y == height) {
                        points[offset + 1] = (height - 1).toFloat()
                        nudged = true
                    }
                    offset += 2
                }
            }
            // Check and nudge points from end:
            nudged = true
            var offset = points.size - 2
            while (offset >= 0 && nudged) {
                val x = points[offset].toInt()
                val y = points[offset + 1].toInt()
                if (x < -1 || x > width || y < -1 || y > height) {
                    throw NotFoundException.notFoundInstance
                }
                nudged = false
                if (x == -1) {
                    points[offset] = 0.0f
                    nudged = true
                } else if (x == width) {
                    points[offset] = (width - 1).toFloat()
                    nudged = true
                }
                if (y == -1) {
                    points[offset + 1] = 0.0f
                    nudged = true
                } else if (y == height) {
                    points[offset + 1] = (height - 1).toFloat()
                    nudged = true
                }
                offset -= 2
            }
        }
    }
}