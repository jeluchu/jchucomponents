/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.qr.common.reedsolomon

/**
 *
 * This class contains utility methods for performing mathematical operations over
 * the Galois Fields. Operations use a given primitive polynomial in calculations.
 *
 *
 * Throughout this package, elements of the GF are represented as an `int`
 * for convenience and speed (but at the cost of memory).
 *
 */
class GenericGF(private val primitive: Int, val size: Int, val generatorBase: Int) {

    private val expTable: IntArray = IntArray(size)
    private val logTable: IntArray = IntArray(size)
    val zero: GenericGFPoly
    val one: GenericGFPoly

    /**
     * @return the monomial representing coefficient * x^degree
     */
    fun buildMonomial(degree: Int, coefficient: Int): GenericGFPoly {
        require(degree >= 0)
        if (coefficient == 0) {
            return zero
        }
        val coefficients = IntArray(degree + 1)
        coefficients[0] = coefficient
        return GenericGFPoly(this, coefficients)
    }

    /**
     * @return 2 to the power of a in GF(size)
     */
    fun exp(a: Int): Int {
        return expTable[a]
    }

    /**
     * @return base 2 log of a in GF(size)
     */
    fun log(a: Int): Int {
        require(a != 0)
        return logTable[a]
    }

    /**
     * @return multiplicative inverse of a
     */
    fun inverse(a: Int): Int {
        if (a == 0) {
            throw ArithmeticException()
        }
        return expTable[size - logTable[a] - 1]
    }

    /**
     * @return product of a and b in GF(size)
     */
    fun multiply(a: Int, b: Int): Int {
        return if (a == 0 || b == 0) {
            0
        } else expTable[(logTable[a] + logTable[b]) % (size - 1)]
    }

    override fun toString(): String {
        return "GF(0x" + Integer.toHexString(primitive) + ',' + size + ')'
    }

    companion object {

        @JvmField
        val QR_CODE_FIELD_256 = GenericGF(0x011D, 256, 0) // x^8 + x^4 + x^3 + x^2 + 1

        /**
         * Implements both addition and subtraction -- they are the same in GF(size).
         *
         * @return sum/difference of a and b
         */
        fun addOrSubtract(a: Int, b: Int): Int = a xor b

    }

    init {
        var x = 1
        for (i in 0 until size) {
            expTable[i] = x
            x *= 2
            if (x >= size) {
                x = x xor primitive
                x = x and size - 1
            }
        }
        for (i in 0 until size - 1) {
            logTable[expTable[i]] = i
        }
        zero = GenericGFPoly(this, intArrayOf(0))
        one = GenericGFPoly(this, intArrayOf(1))
    }

}