package com.jeluchu.pay.revenuecat.utils

import kotlin.math.roundToLong

object PriceUtil {
    internal const val TAG = "JchuPay/PriceUtil"

    var enableLogging: Boolean = false

    /**
     * Convert a formatted price to a formatted price with a given divider.
     * For example: "12.80 EUR" with a divider of 4 will return "4.20 EUR"
     *
     * @param price formatted price input
     * @param divider divide the extracted price
     * @return formatted divided price or null on error
     */
    fun toDividedPrice(
        price: String,
        divider: Int
    ): String? {
        var fullPrice = price
        if (divider == 1) return price
        else try {
            fullPrice = fullPrice.replace("(?<=\\d)\\p{javaSpaceChar}+(?=\\d)".toRegex(), "").trim()
            fullPrice =
                if (fullPrice.contains(",") && fullPrice.contains(".") && fullPrice.last() != '.')
                    fullPrice.replace(",", "")
                else fullPrice.replace(",", ".")

            var digit: String? = null
            val currency: String
            val regex = Regex("(\\d+(?:\\.\\d+)?)")
            regex.findAll(fullPrice).forEach { match ->
                digit = match.groupValues[1]
            }
            if (digit != null) {
                currency = fullPrice.replace(digit, "")
                val digitValue = digit.toDouble() / divider

                return if (fullPrice.startsWith(currency)) currency + roundDigitString(digitValue)
                else roundDigitString(digitValue) + currency
            }
        } catch (_: Exception) {
        }

        return null
    }

    /**
     * Convert a formatted price to a formatted price with a given divider.
     * For example: "4.20 EUR" with a multiply of 4 will return "12.80 EUR"
     *
     * @param price formatted price input
     * @param divider divide the extracted price
     * @return formatted divided price or null on error
     */
    fun toFullPrice(
        price: String,
        divider: Int
    ): String? {
        var fullPrice = price
        if (divider == 1) return price
        else try {
            fullPrice = fullPrice.replace("(?<=\\d)\\p{javaSpaceChar}+(?=\\d)".toRegex(), "").trim()
            fullPrice =
                if (fullPrice.contains(",") && fullPrice.contains(".") && fullPrice.last() != '.')
                    fullPrice.replace(",", "")
                else fullPrice.replace(",", ".")

            var digit: String? = null
            val currency: String
            val regex = Regex("(\\d+(?:\\.\\d+)?)")
            regex.findAll(fullPrice).forEach { match ->
                digit = match.groupValues[1]
            }
            if (digit != null) {
                currency = fullPrice.replace(digit, "")
                val digitValue = digit.toDouble() * divider

                return if (fullPrice.startsWith(currency)) currency + roundDigitString(digitValue)
                else roundDigitString(digitValue) + currency
            }
        } catch (_: Exception) {
        }

        return null
    }

    private fun roundDigitString(digitValue: Double): String {
        val priceValueString =
            if (digitValue > 1000.0) digitValue.roundToLong().toString() else
                digitValue.roundToTwoDecimals()
        return priceValueString.replace(".", ",")
    }

    private fun Double.roundToTwoDecimals(): String {
        val rounded = (this * 100).roundToLong()
        val integer = rounded / 100
        val decimal = (rounded % 100).toString().padStart(2, '0')
        return "$integer.$decimal"
    }
}
