package com.jeluchu.jchucomponents.navigation.ksp.utils

import com.jeluchu.jchucomponents.navigation.ksp.models.ScreenModel

internal fun isTypeIdentifier(value: String): Boolean =
    value.matches(Regex("[A-Z][A-Za-z0-9_]*")) && value !in KOTLIN_KEYWORDS

internal fun String.asKotlinIdentifier(): String =
    if (this in KOTLIN_KEYWORDS) "`$this`" else this

internal fun String.lowercaseFirst(): String =
    replaceFirstChar { first -> first.lowercase() }

internal fun generatedPackage(screens: List<ScreenModel>): String {
    val segments = screens.map { it.packageName.split('.').filter(String::isNotBlank) }
    val common = segments.first().takeWhileIndexed { index, part ->
        segments.all { it.getOrNull(index) == part }
    }
    return (common + "navigation").joinToString(".")
}

private val KOTLIN_KEYWORDS = setOf(
    "as", "break", "class", "continue", "do", "else", "false", "for", "fun", "if", "in",
    "interface", "is", "null", "object", "package", "return", "super", "this", "throw",
    "true", "try", "typealias", "typeof", "val", "var", "when", "while"
)

private inline fun <T> List<T>.takeWhileIndexed(predicate: (Int, T) -> Boolean): List<T> {
    val result = mutableListOf<T>()
    forEachIndexed { index, item ->
        if (!predicate(index, item)) return result
        result += item
    }
    return result
}
