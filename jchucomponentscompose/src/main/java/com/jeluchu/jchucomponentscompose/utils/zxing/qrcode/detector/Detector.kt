package com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.detector

import com.jeluchu.jchucomponentscompose.utils.zxing.*
import com.jeluchu.jchucomponentscompose.utils.zxing.NotFoundException.notFoundInstance
import com.jeluchu.jchucomponentscompose.utils.zxing.ResultPoint.Companion.distance
import com.jeluchu.jchucomponentscompose.utils.zxing.common.BitMatrix
import com.jeluchu.jchucomponentscompose.utils.zxing.common.DetectorResult
import com.jeluchu.jchucomponentscompose.utils.zxing.common.GridSampler.Companion.instance
import com.jeluchu.jchucomponentscompose.utils.zxing.common.PerspectiveTransform
import com.jeluchu.jchucomponentscompose.utils.zxing.common.PerspectiveTransform.Companion.quadrilateralToQuadrilateral
import com.jeluchu.jchucomponentscompose.utils.zxing.common.detector.MathUtils.distance
import com.jeluchu.jchucomponentscompose.utils.zxing.common.detector.MathUtils.round
import com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.decoder.Version.Companion.getProvisionalVersionForDimension
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Detector(private val image: BitMatrix) {

    private var resultPointCallback: ResultPointCallback? = null

    /**
     *
     * Detects a QR Code in an image.
     *
     * @return [DetectorResult] encapsulating results of detecting a QR Code
     * @throws NotFoundException if QR Code cannot be found
     * @throws FormatException   if a QR Code cannot be decoded
     */
    /**
     *
     * Detects a QR Code in an image.
     *
     * @return [DetectorResult] encapsulating results of detecting a QR Code
     * @throws NotFoundException if QR Code cannot be found
     * @throws FormatException   if a QR Code cannot be decoded
     */
    @JvmOverloads
    @Throws(NotFoundException::class, FormatException::class)
    fun detect(hints: Map<DecodeHintType?, *>? = null): DetectorResult {
        resultPointCallback =
            if (hints == null) null else hints[DecodeHintType.NEED_RESULT_POINT_CALLBACK] as ResultPointCallback?
        val finder = FinderPatternFinder(image, resultPointCallback)
        val info = finder.find(hints)
        return processFinderPatternInfo(info)
    }

    @Throws(NotFoundException::class, FormatException::class)
    private fun processFinderPatternInfo(info: FinderPatternInfo): DetectorResult {
        val topLeft = info.topLeft
        val topRight = info.topRight
        val bottomLeft = info.bottomLeft
        val moduleSize = calculateModuleSize(topLeft, topRight, bottomLeft)
        if (moduleSize < 1.0f) {
            throw notFoundInstance
        }
        val dimension = computeDimension(topLeft, topRight, bottomLeft, moduleSize)
        val provisionalVersion = getProvisionalVersionForDimension(dimension)
        val modulesBetweenFPCenters = provisionalVersion.dimensionForVersion - 7
        var alignmentPattern: AlignmentPattern? = null
        // Anything above version 1 has an alignment pattern
        if (provisionalVersion.alignmentPatternCenters.isNotEmpty()) {

            // Guess where a "bottom right" finder pattern would have been
            val bottomRightX = topRight.x - topLeft.x + bottomLeft.x
            val bottomRightY = topRight.y - topLeft.y + bottomLeft.y

            // Estimate that alignment pattern is closer by 3 modules
            // from "bottom right" to known top left location
            val correctionToTopLeft = 1.0f - 3.0f / modulesBetweenFPCenters
            val estAlignmentX =
                (topLeft.x + correctionToTopLeft * (bottomRightX - topLeft.x)).toInt()
            val estAlignmentY =
                (topLeft.y + correctionToTopLeft * (bottomRightY - topLeft.y)).toInt()

            // Kind of arbitrary -- expand search radius before giving up
            var i = 4
            while (i <= 16) {
                runCatching {
                    alignmentPattern = findAlignmentInRegion(
                        moduleSize,
                        estAlignmentX,
                        estAlignmentY,
                        i.toFloat()
                    )
                }
                i = i shl 1
            }
            // If we didn't find alignment pattern... well try anyway without it
        }
        val transform = createTransform(topLeft, topRight, bottomLeft, alignmentPattern, dimension)
        val bits = sampleGrid(image, transform, dimension)
        val points: Array<ResultPoint?> =
            alignmentPattern?.let { arrayOf(bottomLeft, topLeft, topRight, it) }
                ?: arrayOf(bottomLeft, topLeft, topRight)
        return DetectorResult(bits!!, points)
    }

    /**
     *
     * Computes an average estimated module size based on estimated derived from the positions
     * of the three finder patterns.
     *
     * @param topLeft    detected top-left finder pattern center
     * @param topRight   detected top-right finder pattern center
     * @param bottomLeft detected bottom-left finder pattern center
     * @return estimated module size
     */
    private fun calculateModuleSize(
        topLeft: ResultPoint,
        topRight: ResultPoint,
        bottomLeft: ResultPoint
    ): Float {
        // Take the average
        return (calculateModuleSizeOneWay(topLeft, topRight) +
                calculateModuleSizeOneWay(topLeft, bottomLeft)) / 2.0f
    }

    /**
     *
     * Estimates module size based on two finder patterns -- it uses
     * [.sizeOfBlackWhiteBlackRunBothWays] to figure the
     * width of each, measuring along the axis between their centers.
     */
    private fun calculateModuleSizeOneWay(pattern: ResultPoint, otherPattern: ResultPoint): Float {
        val moduleSizeEst1 = sizeOfBlackWhiteBlackRunBothWays(
            pattern.x.toInt(),
            pattern.y.toInt(),
            otherPattern.x.toInt(),
            otherPattern.y.toInt()
        )
        val moduleSizeEst2 = sizeOfBlackWhiteBlackRunBothWays(
            otherPattern.x.toInt(),
            otherPattern.y.toInt(),
            pattern.x.toInt(),
            pattern.y.toInt()
        )
        if (java.lang.Float.isNaN(moduleSizeEst1)) {
            return moduleSizeEst2 / 7.0f
        }
        return if (java.lang.Float.isNaN(moduleSizeEst2)) {
            moduleSizeEst1 / 7.0f
        } else (moduleSizeEst1 + moduleSizeEst2) / 14.0f
        // Average them, and divide by 7 since we've counted the width of 3 black modules,
        // and 1 white and 1 black module on either side. Ergo, divide sum by 14.
    }

    /**
     * See [.sizeOfBlackWhiteBlackRun]; computes the total width of
     * a finder pattern by looking for a black-white-black run from the center in the direction
     * of another point (another finder pattern center), and in the opposite direction too.
     */
    private fun sizeOfBlackWhiteBlackRunBothWays(
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Float {
        var result = sizeOfBlackWhiteBlackRun(fromX, fromY, toX, toY)

        // Now count other way -- don't run off image though of course
        var scale = 1.0f
        var otherToX = fromX - (toX - fromX)
        if (otherToX < 0) {
            scale = fromX / (fromX - otherToX).toFloat()
            otherToX = 0
        } else if (otherToX >= image.width) {
            scale = (image.width - 1 - fromX) / (otherToX - fromX).toFloat()
            otherToX = image.width - 1
        }
        var otherToY = (fromY - (toY - fromY) * scale).toInt()
        scale = 1.0f
        if (otherToY < 0) {
            scale = fromY / (fromY - otherToY).toFloat()
            otherToY = 0
        } else if (otherToY >= image.height) {
            scale = (image.height - 1 - fromY) / (otherToY - fromY).toFloat()
            otherToY = image.height - 1
        }
        otherToX = (fromX + (otherToX - fromX) * scale).toInt()
        result += sizeOfBlackWhiteBlackRun(fromX, fromY, otherToX, otherToY)

        // Middle pixel is double-counted this way; subtract 1
        return result - 1.0f
    }

    /**
     *
     * This method traces a line from a point in the image, in the direction towards another point.
     * It begins in a black region, and keeps going until it finds white, then black, then white again.
     * It reports the distance from the start to this point.
     *
     *
     * This is used when figuring out how wide a finder pattern is, when the finder pattern
     * may be skewed or rotated.
     */
    private fun sizeOfBlackWhiteBlackRun(fromX: Int, fromY: Int, toX: Int, toY: Int): Float {
        // Mild variant of Bresenham's algorithm;
        // see http://en.wikipedia.org/wiki/Bresenham's_line_algorithm
        var mFromX = fromX
        var mFromY = fromY
        var mToX = toX
        var mToY = toY
        val steep = abs(mToY - mFromY) > abs(mToX - mFromX)
        if (steep) {
            var temp = mFromX
            mFromX = mFromY
            mFromY = temp
            temp = mToX
            mToX = mToY
            mToY = temp
        }
        val dx = abs(mToX - mFromX)
        val dy = abs(mToY - mFromY)
        var error = -dx / 2
        val xstep = if (mFromX < mToX) 1 else -1
        val ystep = if (mFromY < mToY) 1 else -1

        // In black pixels, looking for white, first or second time.
        var state = 0
        // Loop up until x == toX, but not beyond
        val xLimit = mToX + xstep
        var x = mFromX
        var y = mFromY
        while (x != xLimit) {
            val realX = if (steep) y else x
            val realY = if (steep) x else y

            // Does current pixel mean we have moved white to black or vice versa?
            // Scanning black in state 0,2 and white in state 1, so if we find the wrong
            // color, advance to next state or end if we are in state 2 already
            if (state == 1 == image[realX, realY]) {
                if (state == 2) {
                    return distance(x, y, mFromX, mFromY)
                }
                state++
            }
            error += dy
            if (error > 0) {
                if (y == mToY) {
                    break
                }
                y += ystep
                error -= dx
            }
            x += xstep
        }
        // Found black-white-black; give the benefit of the doubt that the next pixel outside the image
        // is "white" so this last point at (toX+xStep,toY) is the right ending. This is really a
        // small approximation; (toX+xStep,toY+yStep) might be really correct. Ignore this.
        return if (state == 2) {
            distance(
                mToX + xstep,
                mToY,
                mFromX,
                mFromY
            )
        } else Float.NaN
        // else we didn't find even black-white-black; no estimate is really possible
    }

    /**
     *
     * Attempts to locate an alignment pattern in a limited region of the image, which is
     * guessed to contain it. This method uses [AlignmentPattern].
     *
     * @param overallEstModuleSize estimated module size so far
     * @param estAlignmentX        x coordinate of center of area probably containing alignment pattern
     * @param estAlignmentY        y coordinate of above
     * @param allowanceFactor      number of pixels in all directions to search from the center
     * @return [AlignmentPattern] if found, or null otherwise
     * @throws NotFoundException if an unexpected error occurs during detection
     */
    @Throws(NotFoundException::class)
    private fun findAlignmentInRegion(
        overallEstModuleSize: Float,
        estAlignmentX: Int,
        estAlignmentY: Int,
        allowanceFactor: Float
    ): AlignmentPattern {
        // Look for an alignment pattern (3 modules in size) around where it
        // should be
        val allowance = (allowanceFactor * overallEstModuleSize).toInt()
        val alignmentAreaLeftX = max(0, estAlignmentX - allowance)
        val alignmentAreaRightX = min(image.width - 1, estAlignmentX + allowance)
        if (alignmentAreaRightX - alignmentAreaLeftX < overallEstModuleSize * 3) {
            throw notFoundInstance
        }
        val alignmentAreaTopY = max(0, estAlignmentY - allowance)
        val alignmentAreaBottomY = min(image.height - 1, estAlignmentY + allowance)
        if (alignmentAreaBottomY - alignmentAreaTopY < overallEstModuleSize * 3) {
            throw notFoundInstance
        }
        val alignmentFinder = AlignmentPatternFinder(
            image,
            alignmentAreaLeftX,
            alignmentAreaTopY,
            alignmentAreaRightX - alignmentAreaLeftX,
            alignmentAreaBottomY - alignmentAreaTopY,
            overallEstModuleSize,
            resultPointCallback
        )
        return alignmentFinder.find()
    }

    companion object {
        private fun createTransform(
            topLeft: ResultPoint,
            topRight: ResultPoint,
            bottomLeft: ResultPoint,
            alignmentPattern: ResultPoint?,
            dimension: Int
        ): PerspectiveTransform {
            val dimMinusThree = dimension - 3.5f
            val bottomRightX: Float
            val bottomRightY: Float
            val sourceBottomRightX: Float
            val sourceBottomRightY: Float
            if (alignmentPattern != null) {
                bottomRightX = alignmentPattern.x
                bottomRightY = alignmentPattern.y
                sourceBottomRightX = dimMinusThree - 3.0f
                sourceBottomRightY = sourceBottomRightX
            } else {
                // Don't have an alignment pattern, just make up the bottom-right point
                bottomRightX = topRight.x - topLeft.x + bottomLeft.x
                bottomRightY = topRight.y - topLeft.y + bottomLeft.y
                sourceBottomRightX = dimMinusThree
                sourceBottomRightY = dimMinusThree
            }
            return quadrilateralToQuadrilateral(
                3.5f,
                3.5f,
                dimMinusThree,
                3.5f,
                sourceBottomRightX,
                sourceBottomRightY,
                3.5f,
                dimMinusThree,
                topLeft.x,
                topLeft.y,
                topRight.x,
                topRight.y,
                bottomRightX,
                bottomRightY,
                bottomLeft.x,
                bottomLeft.y
            )
        }

        @Throws(NotFoundException::class)
        private fun sampleGrid(
            image: BitMatrix,
            transform: PerspectiveTransform,
            dimension: Int
        ): BitMatrix? {
            val sampler = instance
            return sampler.sampleGrid(image, dimension, dimension, transform)
        }

        /**
         *
         * Computes the dimension (number of modules on a size) of the QR Code based on the position
         * of the finder patterns and estimated module size.
         */
        @Throws(NotFoundException::class)
        private fun computeDimension(
            topLeft: ResultPoint,
            topRight: ResultPoint,
            bottomLeft: ResultPoint,
            moduleSize: Float
        ): Int {
            val tltrCentersDimension = round(distance(topLeft, topRight) / moduleSize)
            val tlblCentersDimension = round(distance(topLeft, bottomLeft) / moduleSize)
            var dimension = (tltrCentersDimension + tlblCentersDimension) / 2 + 7
            when (dimension and 0x03) {
                0 -> dimension++
                2 -> dimension--
                3 -> dimension += 2
            }
            return dimension
        }
    }
}