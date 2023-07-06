package com.jeluchu.jchucomponents.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString

/**
 * @property displayString
 * The text to be displayed.
 *
 * @property accessibleString
 * The text to be spelled by the talkback.
 */
data class AccessibleString(val displayString: AnnotatedString, val accessibleString: String? = null)

fun buildContentDescription(list: List<CharSequence?>): String = buildString {
    list.forEach {
        append(it)
        if (it?.endsWith('.') == false) {
            append(".")
        }
        appendLine()
    }
}

/**
 *
 * Returns a [Boolean] if font scaling
 * is enabled, performing a font size check
 *
 */
@Composable
@ReadOnlyComposable
fun isFontScalingEnabled() = LocalDensity.current.fontScale >= 1.3f