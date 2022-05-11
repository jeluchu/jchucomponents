/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.utils.zxing

/**
 * Simply encapsulates a width and height.
 */
class Dimension(width: Int, height: Int) {
    val width: Int
    val height: Int
    override fun equals(other: Any?): Boolean {
        if (other is Dimension) {
            return width == other.width && height == other.height
        }
        return false
    }

    override fun hashCode(): Int = width * 32713 + height

    override fun toString(): String = width.toString() + "x" + height

    init {
        require(!(width < 0 || height < 0))
        this.width = width
        this.height = height
    }
}