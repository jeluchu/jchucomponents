package com.jeluchu.jchucomponents.foundation.crypto

object CaesarCipher {
    const val DEFAULT_KEY = 8

    fun encrypt(
        value: String,
        key: Int = DEFAULT_KEY,
    ): String {
        val offset = key.mod(ALPHABET_SIZE)
        if (offset == 0) return value

        return buildString(value.length) {
            value.forEach { char ->
                append(char.shiftLatinLetter(offset))
            }
        }
    }

    fun decrypt(
        value: String,
        key: Int = DEFAULT_KEY,
    ): String = encrypt(value, ALPHABET_SIZE - key.mod(ALPHABET_SIZE))

    private fun Char.shiftLatinLetter(offset: Int): Char =
        when (this) {
            in 'A'..'Z' -> shiftWithin('A', 'Z', offset)
            in 'a'..'z' -> shiftWithin('a', 'z', offset)
            else -> this
        }

    private fun Char.shiftWithin(
        start: Char,
        end: Char,
        offset: Int,
    ): Char {
        val shifted = this + offset
        return if (shifted > end) shifted - ALPHABET_SIZE else shifted
    }

    private const val ALPHABET_SIZE = 26
}
