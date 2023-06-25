package com.jeluchu.jchucomponents.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

object TextUtils {
    fun markedText(
        text: String,
        markedText: String,
        color: Color = Color.Blue
    ) = buildAnnotatedString {
        var i = 0
        val words = markedText.split("\\s+".toRegex())
        words.forEach { word ->
            Regex(Regex.escape(word), RegexOption.IGNORE_CASE).findAll(text).forEach { matchResult ->
                append(text.substring(i, matchResult.range.first))
                withStyle(style = SpanStyle(background = color)) {
                    append(text.substring(matchResult.range.first, matchResult.range.last + 1))
                }
                i = matchResult.range.last + 1
            }
        }

        if (i < text.count()) append(text.substring(i, text.count()))
    }
}