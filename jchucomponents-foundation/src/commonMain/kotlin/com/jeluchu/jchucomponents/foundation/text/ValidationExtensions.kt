package com.jeluchu.jchucomponents.foundation.text

val String.containsLetters: Boolean get() = any { it.isLetter() }
val String.containsNumbers: Boolean get() = any { it.isDigit() }

fun String.removeFirstLastChar(): String = substring(1, length - 1)

fun String.addSpaceAfterEvery4Chars(): String = replace("....".toRegex(), "$0 ")

fun String.addSpaceAfterEvery3Chars(): String = replace("...".toRegex(), "$0 ")

fun String.onlyDigits(): String = filter { it.isDigit() }

fun String.isNumber(): Boolean = toDoubleOrNull() != null

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { word -> word.capitalizeFirstLetter() }

fun String.capitalizeFirstLetter(): String = replaceFirstChar { char -> char.uppercase() }

fun String.remove(
    value: String,
    ignoreCase: Boolean = false,
): String = replace(oldValue = value, newValue = "", ignoreCase = ignoreCase)

fun String.remove(regex: Regex): String = replace(regex = regex, replacement = "")

fun String.wordCount(): Int = trim().takeIf { it.isNotEmpty() }?.split("\\s+".toRegex())?.size ?: 0

fun String?.returnNullIfEmpty(): String? = takeUnless { it.isNullOrEmpty() }

fun String?.toHttpsUrl(): String =
    this?.takeIf { it.startsWith("http://") || it.startsWith("https://") }
        ?: "https://$this"
