/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.qr.qrcode.detector

import com.jeluchu.qr.ResultPoint
import kotlin.math.abs

class FinderPattern constructor(
    posX: Float,
    posY: Float,
    val estimatedModuleSize: Float,
    val count: Int
) : ResultPoint(posX, posY) {

    internal constructor(posX: Float, posY: Float, estimatedModuleSize: Float) : this(
        posX,
        posY,
        estimatedModuleSize,
        1
    )

    /**
     *
     * Determines if this finder pattern "about equals" a finder pattern at the stated
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
     * with a new estimate. It returns a new `FinderPattern` containing a weighted average
     * based on count.
     */
    fun combineEstimate(i: Float, j: Float, newModuleSize: Float): FinderPattern {
        val combinedCount = count + 1
        val combinedX = (count * x + j) / combinedCount
        val combinedY = (count * y + i) / combinedCount
        val combinedModuleSize = (count * estimatedModuleSize + newModuleSize) / combinedCount
        return FinderPattern(combinedX, combinedY, combinedModuleSize, combinedCount)
    }
}