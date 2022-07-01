/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.utils.zxing

import com.jchucomponents.utils.zxing.common.detector.MathUtils.distance

open class ResultPoint(val x: Float, val y: Float) {
    override fun equals(other: Any?): Boolean {
        if (other is ResultPoint) return x == other.x && y == other.y
        return false
    }

    override fun hashCode(): Int =
        31 * java.lang.Float.floatToIntBits(x) + java.lang.Float.floatToIntBits(y)

    override fun toString(): String = "($x,$y)"

    companion object {
        /**
         * Orders an array of three ResultPoints in an order [A,B,C] such that AB is less than AC
         * and BC is less than AC, and the angle between BC and BA is less than 180 degrees.
         *
         * @param patterns array of three `ResultPoint` to order
         */
        @JvmStatic
        fun orderBestPatterns(patterns: Array<ResultPoint>) {

            // Find distances between pattern centers
            val zeroOneDistance = distance(patterns[0], patterns[1])
            val oneTwoDistance = distance(patterns[1], patterns[2])
            val zeroTwoDistance = distance(patterns[0], patterns[2])
            var pointA: ResultPoint
            val pointB: ResultPoint
            var pointC: ResultPoint
            // Assume one closest to other two is B; A and C will just be guesses at first
            if (oneTwoDistance >= zeroOneDistance && oneTwoDistance >= zeroTwoDistance) {
                pointB = patterns[0]
                pointA = patterns[1]
                pointC = patterns[2]
            } else if (zeroTwoDistance >= oneTwoDistance && zeroTwoDistance >= zeroOneDistance) {
                pointB = patterns[1]
                pointA = patterns[0]
                pointC = patterns[2]
            } else {
                pointB = patterns[2]
                pointA = patterns[0]
                pointC = patterns[1]
            }

            // Use cross product to figure out whether A and C are correct or flipped.
            // This asks whether BC x BA has a positive z component, which is the arrangement
            // we want for A, B, C. If it's negative, then we've got it flipped around and
            // should swap A and C.
            if (crossProductZ(pointA, pointB, pointC) < 0.0f) {
                val temp = pointA
                pointA = pointC
                pointC = temp
            }
            patterns[0] = pointA
            patterns[1] = pointB
            patterns[2] = pointC
        }

        /**
         * @param pattern1 first pattern
         * @param pattern2 second pattern
         * @return distance between two points
         */
        @JvmStatic
        fun distance(pattern1: ResultPoint, pattern2: ResultPoint): Float =
            distance(pattern1.x, pattern1.y, pattern2.x, pattern2.y)

        /**
         * Returns the z component of the cross product between vectors BC and BA.
         */
        private fun crossProductZ(
            pointA: ResultPoint,
            pointB: ResultPoint,
            pointC: ResultPoint
        ): Float {
            val bX = pointB.x
            val bY = pointB.y
            return (pointC.x - bX) * (pointA.y - bY) - (pointC.y - bY) * (pointA.x - bX)
        }
    }
}