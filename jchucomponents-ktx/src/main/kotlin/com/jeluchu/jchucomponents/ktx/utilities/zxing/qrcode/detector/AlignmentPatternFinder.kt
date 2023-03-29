/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.utilities.zxing.qrcode.detector

import com.jeluchu.jchucomponents.ktx.utilities.zxing.NotFoundException
import com.jeluchu.jchucomponents.ktx.utilities.zxing.ResultPointCallback
import com.jeluchu.jchucomponents.ktx.utilities.zxing.common.BitMatrix
import kotlin.math.abs

/**
 *
 * This class attempts to find alignment patterns in a QR Code. Alignment patterns look like finder
 * patterns but are smaller and appear at regular intervals throughout the image.
 *
 *
 * At the moment this only looks for the bottom-right alignment pattern.
 *
 *
 * This is mostly a simplified copy of [FinderPatternFinder]. It is copied,
 * pasted and stripped down here for maximum performance but does unfortunately duplicate
 * some code.
 *
 *
 * This class is thread-safe but not reentrant. Each thread must allocate its own object.
 */
internal class AlignmentPatternFinder(
    private val image: BitMatrix,
    startX: Int,
    startY: Int,
    width: Int,
    height: Int,
    moduleSize: Float,
    resultPointCallback: ResultPointCallback?
) {
    private val possibleCenters: MutableList<AlignmentPattern>
    private val startX: Int
    private val startY: Int
    private val width: Int
    private val height: Int
    private val moduleSize: Float
    private val crossCheckStateCount: IntArray
    private val resultPointCallback: ResultPointCallback?

    /**
     *
     * This method attempts to find the bottom-right alignment pattern in the image. It is a bit messy since
     * it's pretty performance-critical and so is written to be fast foremost.
     *
     * @return [AlignmentPattern] if found
     * @throws NotFoundException if not found
     */
    @Throws(NotFoundException::class)
    fun find(): AlignmentPattern {
        val startX = startX
        val height = height
        val maxJ = startX + width
        val middleI = startY + height / 2
        // We are looking for black/white/black modules in 1:1:1 ratio;
        // this tracks the number of black/white/black modules seen so far
        val stateCount = IntArray(3)
        for (iGen in 0 until height) {
            // Search from middle outwards
            val i = middleI + if (iGen and 0x01 == 0) (iGen + 1) / 2 else -((iGen + 1) / 2)
            stateCount[0] = 0
            stateCount[1] = 0
            stateCount[2] = 0
            var j = startX
            // Burn off leading white pixels before anything else; if we start in the middle of
            // a white run, it doesn't make sense to count its length, since we don't know if the
            // white run continued to the left of the start point
            while (j < maxJ && !image[j, i]) {
                j++
            }
            var currentState = 0
            while (j < maxJ) {
                if (image[j, i]) {
                    // Black pixel
                    if (currentState == 1) { // Counting black pixels
                        stateCount[1]++
                    } else { // Counting white pixels
                        if (currentState == 2) { // A winner?
                            if (foundPatternCross(stateCount)) { // Yes
                                val confirmed = handlePossibleCenter(stateCount, i, j)
                                if (confirmed != null) {
                                    return confirmed
                                }
                            }
                            stateCount[0] = stateCount[2]
                            stateCount[1] = 1
                            stateCount[2] = 0
                            currentState = 1
                        } else {
                            stateCount[++currentState]++
                        }
                    }
                } else { // White pixel
                    if (currentState == 1) { // Counting black pixels
                        currentState++
                    }
                    stateCount[currentState]++
                }
                j++
            }
            if (foundPatternCross(stateCount)) {
                val confirmed = handlePossibleCenter(stateCount, i, maxJ)
                if (confirmed != null) {
                    return confirmed
                }
            }
        }

        // Hmm, nothing we saw was observed and confirmed twice. If we had
        // any guess at all, return it.
        if (possibleCenters.isNotEmpty()) {
            return possibleCenters[0]
        }
        throw NotFoundException.notFoundInstance
    }

    /**
     * @param stateCount count of black/white/black pixels just read
     * @return true iff the proportions of the counts is close enough to the 1/1/1 ratios
     * used by alignment patterns to be considered a match
     */
    private fun foundPatternCross(stateCount: IntArray): Boolean {
        val moduleSize = moduleSize
        val maxVariance = moduleSize / 2.0f
        for (i in 0..2) {
            if (abs(moduleSize - stateCount[i]) >= maxVariance) {
                return false
            }
        }
        return true
    }

    /**
     *
     * After a horizontal scan finds a potential alignment pattern, this method
     * "cross-checks" by scanning down vertically through the center of the possible
     * alignment pattern to see if the same proportion is detected.
     *
     * @param startI   row where an alignment pattern was detected
     * @param centerJ  center of the section that appears to cross an alignment pattern
     * @param maxCount maximum reasonable number of modules that should be
     * observed in any reading state, based on the results of the horizontal scan
     * @return vertical center of alignment pattern, or [Float.NaN] if not found
     */
    private fun crossCheckVertical(
        startI: Int, centerJ: Int, maxCount: Int,
        originalStateCountTotal: Int
    ): Float {
        val image = image
        val maxI = image.height
        val stateCount = crossCheckStateCount
        stateCount[0] = 0
        stateCount[1] = 0
        stateCount[2] = 0

        // Start counting up from center
        var i = startI
        while (i >= 0 && image[centerJ, i] && stateCount[1] <= maxCount) {
            stateCount[1]++
            i--
        }
        // If already too many modules in this state or ran off the edge:
        if (i < 0 || stateCount[1] > maxCount) {
            return Float.NaN
        }
        while (i >= 0 && !image[centerJ, i] && stateCount[0] <= maxCount) {
            stateCount[0]++
            i--
        }
        if (stateCount[0] > maxCount) {
            return Float.NaN
        }

        // Now also count down from center
        i = startI + 1
        while (i < maxI && image[centerJ, i] && stateCount[1] <= maxCount) {
            stateCount[1]++
            i++
        }
        if (i == maxI || stateCount[1] > maxCount) {
            return Float.NaN
        }
        while (i < maxI && !image[centerJ, i] && stateCount[2] <= maxCount) {
            stateCount[2]++
            i++
        }
        if (stateCount[2] > maxCount) {
            return Float.NaN
        }
        val stateCountTotal = stateCount[0] + stateCount[1] + stateCount[2]
        if (5 * abs(stateCountTotal - originalStateCountTotal) >= 2 * originalStateCountTotal) {
            return Float.NaN
        }
        return if (foundPatternCross(stateCount)) centerFromEnd(stateCount, i) else Float.NaN
    }

    /**
     *
     * This is called when a horizontal scan finds a possible alignment pattern. It will
     * cross check with a vertical scan, and if successful, will see if this pattern had been
     * found on a previous horizontal scan. If so, we consider it confirmed and conclude we have
     * found the alignment pattern.
     *
     * @param stateCount reading state module counts from horizontal scan
     * @param i          row where alignment pattern may be found
     * @param j          end of possible alignment pattern in row
     * @return [AlignmentPattern] if we have found the same pattern twice, or null if not
     */
    private fun handlePossibleCenter(stateCount: IntArray, i: Int, j: Int): AlignmentPattern? {
        val stateCountTotal = stateCount[0] + stateCount[1] + stateCount[2]
        val centerJ = centerFromEnd(stateCount, j)
        val centerI = crossCheckVertical(i, centerJ.toInt(), 2 * stateCount[1], stateCountTotal)
        if (!java.lang.Float.isNaN(centerI)) {
            val estimatedModuleSize = (stateCount[0] + stateCount[1] + stateCount[2]) / 3.0f
            for (center in possibleCenters) {
                // Look for about the same center and module size:
                if (center.aboutEquals(estimatedModuleSize, centerI, centerJ)) {
                    return center.combineEstimate(centerI, centerJ, estimatedModuleSize)
                }
            }
            // Hadn't found this before; save it
            val point = AlignmentPattern(centerJ, centerI, estimatedModuleSize)
            possibleCenters.add(point)
            resultPointCallback?.foundPossibleResultPoint(point)
        }
        return null
    }

    companion object {
        /**
         * Given a count of black/white/black pixels just seen and an end position,
         * figures the location of the center of this black/white/black run.
         */
        private fun centerFromEnd(stateCount: IntArray, end: Int): Float {
            return end - stateCount[2] - stateCount[1] / 2.0f
        }
    }

    init {
        possibleCenters = ArrayList(5)
        this.startX = startX
        this.startY = startY
        this.width = width
        this.height = height
        this.moduleSize = moduleSize
        crossCheckStateCount = IntArray(3)
        this.resultPointCallback = resultPointCallback
    }
}