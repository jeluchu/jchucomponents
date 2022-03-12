/*
 * Copyright 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.encoder

import java.util.*

class ByteMatrix(val width: Int, val height: Int) {
    /**
     * @return an internal representation as bytes, in row-major order. array[y][x] represents point (x,y)
     */
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