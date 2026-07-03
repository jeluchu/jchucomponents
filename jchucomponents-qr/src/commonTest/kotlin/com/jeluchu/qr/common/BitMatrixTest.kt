package com.jeluchu.qr.common

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BitMatrixTest {

    @Test
    fun setsFlipsAndClearsBits() {
        val matrix = BitMatrix(width = 5, height = 4, multiple = 1)

        matrix.set(1, 2)
        assertTrue(matrix[1, 2])

        matrix.flip(1, 2)
        assertFalse(matrix[1, 2])

        matrix.setRegion(left = 2, top = 1, width = 2, height = 2)
        assertTrue(matrix[2, 1])
        assertTrue(matrix[3, 2])

        matrix.clear()
        assertFalse(matrix[2, 1])
    }

    @Test
    fun reportsOuterSetBits() {
        val matrix = BitMatrix(6)
        matrix.set(1, 2)
        matrix.set(4, 5)

        assertContentEquals(intArrayOf(1, 2), matrix.topLeftOnBit)
        assertContentEquals(intArrayOf(4, 5), matrix.bottomRightOnBit)
    }
}
