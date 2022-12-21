/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils.zxing.qrcode.detector

import com.jeluchu.jchucomponents.utils.zxing.ResultPoint
import kotlin.math.abs

/**
 *
 * Encapsulates an alignment pattern, which are the smaller square patterns found in
 * all but the simplest QR Codes.
 *
 */
class AlignmentPattern internal constructor(
    posX: Float,
    posY: Float,
    private val estimatedModuleSize: Float
) : ResultPoint(posX, posY) {
    /**
     *
     * Determines if this alignment pattern "about equals" an alignment pattern at the stated
     * position and size -- meaning, it is at nearly the same center with nearly the same size.
     */
    fun aboutEquals(moduleSize: Float, i: Float, j: Float): Boolean {
        if (abs(i - y) <= moduleSize && abs(j - x) <= moduleSize) {
            val moduleSizeDiff = abs(moduleSize - estimatedModuleSize)
            return moduleSizeDiff <= 1.0f || moduleSizeDiff <= estimatedModuleSize
        }
        return false
    }

    /**
     * Combines this object's current estimate of a finder pattern position and module size
     * with a new estimate. It returns a new `FinderPattern` containing an average of the two.
     */
    fun combineEstimate(i: Float, j: Float, newModuleSize: Float): AlignmentPattern {
        val combinedX = (x + j) / 2.0f
        val combinedY = (y + i) / 2.0f
        val combinedModuleSize = (estimatedModuleSize + newModuleSize) / 2.0f
        return AlignmentPattern(combinedX, combinedY, combinedModuleSize)
    }
}