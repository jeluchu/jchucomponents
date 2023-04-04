/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.qr.common.detector

import kotlin.math.sqrt

/**
 * General math-related and numeric utility functions.
 */
object MathUtils {
    /**
     * Ends up being a bit faster than [Math.round]. This merely rounds its
     * argument to the nearest int, where x.5 rounds up to x+1. Semantics of this shortcut
     * differ slightly from [Math.round] in that half rounds down for negative
     * values. -2.5 rounds to -3, not -2. For purposes here it makes no difference.
     *
     * @param d real value to round
     * @return nearest `int`
     */
    @JvmStatic
    fun round(d: Float): Int {
        return (d + if (d < 0.0f) -0.5f else 0.5f).toInt()
    }

    /**
     * @param aX point A x coordinate
     * @param aY point A y coordinate
     * @param bX point B x coordinate
     * @param bY point B y coordinate
     * @return Euclidean distance between points A and B
     */
    fun distance(aX: Float, aY: Float, bX: Float, bY: Float): Float {
        val xDiff = (aX - bX).toDouble()
        val yDiff = (aY - bY).toDouble()
        return sqrt(xDiff * xDiff + yDiff * yDiff).toFloat()
    }

    /**
     * @param aX point A x coordinate
     * @param aY point A y coordinate
     * @param bX point B x coordinate
     * @param bY point B y coordinate
     * @return Euclidean distance between points A and B
     */
    @JvmStatic
    fun distance(aX: Int, aY: Int, bX: Int, bY: Int): Float {
        val xDiff = (aX - bX).toDouble()
        val yDiff = (aY - bY).toDouble()
        return sqrt(xDiff * xDiff + yDiff * yDiff).toFloat()
    }
}