/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils.zxing.common.reedsolomon

/**
 *
 * Represents a polynomial whose coefficients are elements of a GF.
 * Instances of this class are immutable.
 *
 *
 * Much credit is due to William Rucklidge since portions of this code are an indirect
 * port of his C++ Reed-Solomon implementation.
 *
 */
class GenericGFPoly(field: GenericGF, coefficients: IntArray) {

    private val field: GenericGF
    val coefficients: IntArray

    /**
     * @return degree of this polynomial
     */
    val degree: Int
        get() = coefficients.size - 1

    /**
     * @return true iff this polynomial is the monomial "0"
     */
    val isZero: Boolean
        get() = coefficients[0] == 0

    /**
     * @return coefficient of x^degree term in this polynomial
     */
    fun getCoefficient(degree: Int): Int = coefficients[coefficients.size - 1 - degree]

    /**
     * @return evaluation of this polynomial at a given point
     */
    fun evaluateAt(a: Int): Int {
        if (a == 0) return getCoefficient(0)
        if (a == 1) {
            var result = 0
            for (coefficient in coefficients) {
                result = GenericGF.addOrSubtract(result, coefficient)
            }
            return result
        }
        var result = coefficients[0]
        val size = coefficients.size
        for (i in 1 until size) {
            result = GenericGF.addOrSubtract(field.multiply(a, result), coefficients[i])
        }
        return result
    }

    fun addOrSubtract(other: GenericGFPoly): GenericGFPoly {
        require(field == other.field) { "GenericGFPolys do not have same GenericGF field" }
        if (isZero) {
            return other
        }
        if (other.isZero) {
            return this
        }
        var smallerCoefficients = coefficients
        var largerCoefficients = other.coefficients
        if (smallerCoefficients.size > largerCoefficients.size) {
            val temp = smallerCoefficients
            smallerCoefficients = largerCoefficients
            largerCoefficients = temp
        }
        val sumDiff = IntArray(largerCoefficients.size)
        val lengthDiff = largerCoefficients.size - smallerCoefficients.size
        System.arraycopy(largerCoefficients, 0, sumDiff, 0, lengthDiff)
        for (i in lengthDiff until largerCoefficients.size) {
            sumDiff[i] =
                GenericGF.addOrSubtract(smallerCoefficients[i - lengthDiff], largerCoefficients[i])
        }
        return GenericGFPoly(field, sumDiff)
    }

    fun multiply(other: GenericGFPoly): GenericGFPoly {
        require(field == other.field) { "GenericGFPolys do not have same GenericGF field" }
        if (isZero || other.isZero) {
            return field.zero
        }
        val aCoefficients = coefficients
        val aLength = aCoefficients.size
        val bCoefficients = other.coefficients
        val bLength = bCoefficients.size
        val product = IntArray(aLength + bLength - 1)
        for (i in 0 until aLength) {
            val aCoeff = aCoefficients[i]
            for (j in 0 until bLength) {
                product[i + j] = GenericGF.addOrSubtract(
                    product[i + j],
                    field.multiply(aCoeff, bCoefficients[j])
                )
            }
        }
        return GenericGFPoly(field, product)
    }

    fun multiply(scalar: Int): GenericGFPoly {
        if (scalar == 0) {
            return field.zero
        }
        if (scalar == 1) {
            return this
        }
        val size = coefficients.size
        val product = IntArray(size)
        for (i in 0 until size) {
            product[i] = field.multiply(coefficients[i], scalar)
        }
        return GenericGFPoly(field, product)
    }

    fun multiplyByMonomial(degree: Int, coefficient: Int): GenericGFPoly {
        require(degree >= 0)
        if (coefficient == 0) {
            return field.zero
        }
        val size = coefficients.size
        val product = IntArray(size + degree)
        for (i in 0 until size) {
            product[i] = field.multiply(coefficients[i], coefficient)
        }
        return GenericGFPoly(field, product)
    }

    fun divide(other: GenericGFPoly): Array<GenericGFPoly> {
        require(field == other.field) { "GenericGFPolys do not have same GenericGF field" }
        require(!other.isZero) { "Divide by 0" }
        var quotient = field.zero
        var remainder = this
        val denominatorLeadingTerm = other.getCoefficient(other.degree)
        val inverseDenominatorLeadingTerm = field.inverse(denominatorLeadingTerm)
        while (remainder.degree >= other.degree && !remainder.isZero) {
            val degreeDifference = remainder.degree - other.degree
            val scale = field.multiply(
                remainder.getCoefficient(remainder.degree),
                inverseDenominatorLeadingTerm
            )
            val term = other.multiplyByMonomial(degreeDifference, scale)
            val iterationQuotient = field.buildMonomial(degreeDifference, scale)
            quotient = quotient.addOrSubtract(iterationQuotient)
            remainder = remainder.addOrSubtract(term)
        }
        return arrayOf(quotient, remainder)
    }

    override fun toString(): String {
        if (isZero) return "0"
        val result = StringBuilder(8 * degree)
        for (degree in degree downTo 0) {
            var coefficient = getCoefficient(degree)
            if (coefficient != 0) {
                if (coefficient < 0) {
                    if (degree == degree) {
                        result.append("-")
                    } else {
                        result.append(" - ")
                    }
                    coefficient = -coefficient
                } else {
                    if (result.isNotEmpty()) {
                        result.append(" + ")
                    }
                }
                if (degree == 0 || coefficient != 1) {
                    when (val alphaPower = field.log(coefficient)) {
                        0 -> result.append('1')
                        1 -> result.append('a')
                        else -> {
                            result.append("a^")
                            result.append(alphaPower)
                        }
                    }
                }
                if (degree != 0) {
                    if (degree == 1) result.append('x')
                    else {
                        result.append("x^")
                        result.append(degree)
                    }
                }
            }
        }
        return result.toString()
    }

    init {
        require(coefficients.isNotEmpty())
        this.field = field
        val coefficientsLength = coefficients.size
        if (coefficientsLength > 1 && coefficients[0] == 0) {
            var firstNonZero = 1
            while (firstNonZero < coefficientsLength && coefficients[firstNonZero] == 0) {
                firstNonZero++
            }
            if (firstNonZero == coefficientsLength) this.coefficients = intArrayOf(0)
            else {
                this.coefficients = IntArray(coefficientsLength - firstNonZero)
                System.arraycopy(
                    coefficients,
                    firstNonZero,
                    this.coefficients,
                    0,
                    this.coefficients.size
                )
            }
        } else this.coefficients = coefficients

    }
}