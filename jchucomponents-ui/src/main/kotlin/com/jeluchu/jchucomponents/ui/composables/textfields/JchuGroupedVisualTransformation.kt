package com.jeluchu.jchucomponents.ui.composables.textfields

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.jeluchu.jchucomponents.foundation.text.formatInGroups

/**
 * Visually separates text into fixed-size groups without changing the source value.
 *
 * This is useful for codes, identifiers and card-like values where separators should
 * remain presentation-only. Cursor offsets are preserved for separators of any length.
 *
 * @param groupSize number of source characters in each group.
 * @param separator text inserted between groups.
 */
class JchuGroupedVisualTransformation(
    private val groupSize: Int = 4,
    private val separator: String = "-"
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val transformedText = originalText.formatInGroups(groupSize, separator)

        val offsetMapping =
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    if (offset == 0) return 0

                    val separatorCount = (offset - 1) / groupSize
                    return offset + separatorCount * separator.length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    if (offset == 0) return 0

                    var originalOffset = 0
                    var transformedOffset = 0

                    while (
                        transformedOffset < offset &&
                        originalOffset < originalText.length
                    ) {
                        if (originalOffset > 0 && originalOffset % groupSize == 0) {
                            transformedOffset += separator.length
                        }
                        if (transformedOffset < offset) {
                            transformedOffset++
                            originalOffset++
                        }
                    }

                    return originalOffset.coerceAtMost(originalText.length)
                }
            }

        return TransformedText(
            text = AnnotatedString(transformedText),
            offsetMapping = offsetMapping
        )
    }
}
