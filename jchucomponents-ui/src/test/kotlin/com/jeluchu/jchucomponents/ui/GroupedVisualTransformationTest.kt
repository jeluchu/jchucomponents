package com.jeluchu.jchucomponents.ui

import androidx.compose.ui.text.AnnotatedString
import com.jeluchu.jchucomponents.ui.composables.textfields.JchuGroupedVisualTransformation
import org.junit.Assert.assertEquals
import org.junit.Test

class GroupedVisualTransformationTest {
    @Test
    fun groupedTransformation_formatsTextWithoutChangingSourceOffsets() {
        val result =
            JchuGroupedVisualTransformation(
                groupSize = 4,
                separator = "--"
            ).filter(AnnotatedString("12345678"))

        assertEquals("1234--5678", result.text.text)
        assertEquals(
            listOf(0, 1, 2, 3, 4, 7, 8, 9, 10),
            (0..8).map(result.offsetMapping::originalToTransformed)
        )
        assertEquals(
            listOf(0, 1, 2, 3, 4, 4, 4, 5, 6, 7, 8),
            (0..10).map(result.offsetMapping::transformedToOriginal)
        )
    }
}
