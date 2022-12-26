/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.utilities.zxing.common.reedsolomon

/**
 *
 * Implements Reed-Solomon encoding, as the name implies.
 *
 */
class ReedSolomonEncoder(private val field: GenericGF) {
    private val cachedGenerators: MutableList<GenericGFPoly>
    private fun buildGenerator(degree: Int): GenericGFPoly {
        if (degree >= cachedGenerators.size) {
            var lastGenerator = cachedGenerators[cachedGenerators.size - 1]
            for (d in cachedGenerators.size..degree) {
                val nextGenerator = lastGenerator.multiply(
                    GenericGFPoly(field, intArrayOf(1, field.exp(d - 1 + field.generatorBase)))
                )
                cachedGenerators.add(nextGenerator)
                lastGenerator = nextGenerator
            }
        }
        return cachedGenerators[degree]
    }

    fun encode(toEncode: IntArray, ecBytes: Int) {
        require(ecBytes != 0) { "No error correction bytes" }
        val dataBytes = toEncode.size - ecBytes
        require(dataBytes > 0) { "No data bytes provided" }
        val generator = buildGenerator(ecBytes)
        val infoCoefficients = IntArray(dataBytes)
        System.arraycopy(toEncode, 0, infoCoefficients, 0, dataBytes)
        var info = GenericGFPoly(field, infoCoefficients)
        info = info.multiplyByMonomial(ecBytes, 1)
        val remainder = info.divide(generator)[1]
        val coefficients = remainder.coefficients
        val numZeroCoefficients = ecBytes - coefficients.size
        for (i in 0 until numZeroCoefficients) {
            toEncode[dataBytes + i] = 0
        }
        System.arraycopy(
            coefficients,
            0,
            toEncode,
            dataBytes + numZeroCoefficients,
            coefficients.size
        )
    }

    init {
        cachedGenerators = ArrayList()
        cachedGenerators.add(GenericGFPoly(field, intArrayOf(1)))
    }

}