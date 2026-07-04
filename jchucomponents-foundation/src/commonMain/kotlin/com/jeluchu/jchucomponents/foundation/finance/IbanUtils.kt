package com.jeluchu.jchucomponents.foundation.finance

object IbanUtils {
    private const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val COUNTRY_CODE = "ES"
    private const val DEFAULT_CHECK_DIGITS = "00"
    private const val MODULUS = 97
    private const val CHECK_BASE = 98

    /** Returns Spanish IBAN check digits for the provided account number. */
    fun getSpanishCheckDigits(account: String): String {
        val normalizedAccount = account.uppercase().trim()
        val provisionalIban = "$COUNTRY_CODE$DEFAULT_CHECK_DIGITS$normalizedAccount"
        val numericIban =
            provisionalIban
                .map { char -> char.toIbanNumber() }
                .joinToString(separator = "")
        val rearrangedIban = numericIban.drop(6) + numericIban.take(6)
        val checkDigits = CHECK_BASE - rearrangedIban.mod97()

        return checkDigits.toString().padStart(length = 2, padChar = '0')
    }

    /** Returns Spanish IBAN check digits for the provided account number. */
    fun getIban(account: String): String = getSpanishCheckDigits(account)

    private fun Char.toIbanNumber(): String = if (isLetter()) (ALPHABET.indexOf(this) + 10).toString() else toString()

    private fun String.mod97(): Int {
        var result = 0
        forEach { char ->
            result = (result * 10 + char.digitToInt()) % MODULUS
        }
        return result
    }
}
