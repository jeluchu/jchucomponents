/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.utilities.zxing.qrcode.encoder

import java.util.*

class ByteMatrix(val width: Int, val height: Int) {

    val array: Array<ByteArray> = Array(height) { ByteArray(width) }

    operator fun get(x: Int, y: Int): Byte = array[y][x]

    operator fun set(x: Int, y: Int, value: Byte) {
        array[y][x] = value
    }

    operator fun set(x: Int, y: Int, value: Int) {
        array[y][x] = value.toByte()
    }

    operator fun set(x: Int, y: Int, value: Boolean) {
        array[y][x] = (if (value) 1 else 0).toByte()
    }

    fun clear(value: Byte) {
        for (aByte in array) {
            Arrays.fill(aByte, value)
        }
    }

    override fun toString(): String {
        val result = StringBuilder(2 * width * height + 2)
        for (y in 0 until height) {
            val bytesY = array[y]
            for (x in 0 until width) {
                when (bytesY[x].toInt()) {
                    0 -> result.append(" 0")
                    1 -> result.append(" 1")
                    else -> result.append("  ")
                }
            }
            result.append('\n')
        }
        return result.toString()
    }

}