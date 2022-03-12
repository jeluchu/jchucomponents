/*
 * Copyright 2007 ZXing authors
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
package com.jeluchu.jchucomponentscompose.utils.zxing.common.reedsolomon

/**
 *
 * Implements Reed-Solomon decoding, as the name implies.
 *
 *
 * The algorithm will not be explained here, but the following references were helpful
 * in creating this implementation:
 *
 *
 *  * Bruce Maggs.
 * [
 * "Decoding Reed-Solomon Codes"](http://www.cs.cmu.edu/afs/cs.cmu.edu/project/pscico-guyb/realworld/www/rs_decode.ps) (see discussion of Forney's Formula)
 *  * J.I. Hall. [
 * "Chapter 5. Generalized Reed-Solomon Codes"](www.mth.msu.edu/~jhall/classes/codenotes/GRS.pdf)
 * (see discussion of Euclidean algorithm)
 *
 *
 *
 * Much credit is due to William Rucklidge since portions of this code are an indirect
 * port of his C++ Reed-Solomon implementation.
 *
 * @author Sean Owen
 * @author William Rucklidge
 * @author sanfordsquires
 */
class ReedSolomonDecoder(private val field: GenericGF) {
    /**
     *
     * Decodes given set of received codewords, which include both data and error-correction
     * codewords. Really, this means it uses Reed-Solomon to detect and correct errors, in-place,
     * in the input.
     *
     * @param received data and error-correction codewords
     * @param twoS     number of error-correction codewords available
     * @throws ReedSolomonException if decoding fails for any reason
     */
    @Throws(ReedSolomonException::class)
    fun decode(received: IntArray, twoS: Int) {
        val poly = GenericGFPoly(field, received)
        val syndromeCoefficients = IntArray(twoS)
        var noError = true
        for (i in 0 until twoS) {
            val eval = poly.evaluateAt(field.exp(i + field.generatorBase))
            syndromeCoefficients[syndromeCoefficients.size - 1 - i] = eval
            if (eval != 0) {
                noError = false
            }
        }
        if (noError) {
            return
        }
        val syndrome = GenericGFPoly(field, syndromeCoefficients)
        val sigmaOmega = runEuclideanAlgorithm(field.buildMonomial(twoS, 1), syndrome, twoS)
        val sigma = sigmaOmega[0]
        val omega = sigmaOmega[1]
        val errorLocations = findErrorLocations(sigma)
        val errorMagnitudes = findErrorMagnitudes(omega, errorLocations)
        for (i in errorLocations.indices) {
            val position = received.size - 1 - field.log(errorLocations[i])
            if (position < 0) {
                throw ReedSolomonException("Bad error location")
            }
            received[position] = GenericGF.addOrSubtract(received[position], errorMagnitudes[i])
        }
    }

    @Throws(ReedSolomonException::class)
    private fun runEuclideanAlgorithm(
        a: GenericGFPoly,
        b: GenericGFPoly,
        R: Int
    ): Array<GenericGFPoly> {
        // Assume a's degree is >= b's
        var a1 = a
        var b1 = b
        if (a1.degree < b1.degree) {
            val temp = a1
            a1 = b1
            b1 = temp
        }
        var rLast = a1
        var r = b1
        var tLast = field.zero
        var t = field.one

        // Run Euclidean algorithm until r's degree is less than R/2
        while (r.degree >= R / 2) {
            val rLastLast = rLast
            val tLastLast = tLast
            rLast = r
            tLast = t

            // Divide rLastLast by rLast, with quotient in q and remainder in r
            if (rLast.isZero) {
                // Oops, Euclidean algorithm already terminated?
                throw ReedSolomonException("r_{i-1} was zero")
            }
            r = rLastLast
            var q = field.zero
            val denominatorLeadingTerm = rLast.getCoefficient(rLast.degree)
            val dltInverse = field.inverse(denominatorLeadingTerm)
            while (r.degree >= rLast.degree && !r.isZero) {
                val degreeDiff = r.degree - rLast.degree
                val scale = field.multiply(r.getCoefficient(r.degree), dltInverse)
                q = q.addOrSubtract(field.buildMonomial(degreeDiff, scale))
                r = r.addOrSubtract(rLast.multiplyByMonomial(degreeDiff, scale))
            }
            t = q.multiply(tLast).addOrSubtract(tLastLast)
            check(r.degree < rLast.degree) { "Division algorithm failed to reduce polynomial?" }
        }
        val sigmaTildeAtZero = t.getCoefficient(0)
        if (sigmaTildeAtZero == 0) {
            throw ReedSolomonException("sigmaTilde(0) was zero")
        }
        val inverse = field.inverse(sigmaTildeAtZero)
        val sigma = t.multiply(inverse)
        val omega = r.multiply(inverse)
        return arrayOf(sigma, omega)
    }

    @Throws(ReedSolomonException::class)
    private fun findErrorLocations(errorLocator: GenericGFPoly): IntArray {
        // This is a direct application of Chien's search
        val numErrors = errorLocator.degree
        if (numErrors == 1) { // shortcut
            return intArrayOf(errorLocator.getCoefficient(1))
        }
        val result = IntArray(numErrors)
        var e = 0
        var i = 1
        while (i < field.size && e < numErrors) {
            if (errorLocator.evaluateAt(i) == 0) {
                result[e] = field.inverse(i)
                e++
            }
            i++
        }
        if (e != numErrors) {
            throw ReedSolomonException("Error locator degree does not match number of roots")
        }
        return result
    }

    private fun findErrorMagnitudes(
        errorEvaluator: GenericGFPoly,
        errorLocations: IntArray
    ): IntArray {
        // This is directly applying Forney's Formula
        val s = errorLocations.size
        val result = IntArray(s)
        for (i in 0 until s) {
            val xiInverse = field.inverse(errorLocations[i])
            var denominator = 1
            for (j in 0 until s) {
                if (i != j) {
                    //denominator = field.multiply(denominator,
                    //    GenericGF.addOrSubtract(1, field.multiply(errorLocations[j], xiInverse)));
                    // Above should work but fails on some Apple and Linux JDKs due to a Hotspot bug.
                    // Below is a funny-looking workaround from Steven Parkes
                    val term = field.multiply(errorLocations[j], xiInverse)
                    val termPlus1 = if (term and 0x1 == 0) term or 1 else term and 1.inv()
                    denominator = field.multiply(denominator, termPlus1)
                }
            }
            result[i] = field.multiply(
                errorEvaluator.evaluateAt(xiInverse),
                field.inverse(denominator)
            )
            if (field.generatorBase != 0) {
                result[i] = field.multiply(result[i], xiInverse)
            }
        }
        return result
    }
}