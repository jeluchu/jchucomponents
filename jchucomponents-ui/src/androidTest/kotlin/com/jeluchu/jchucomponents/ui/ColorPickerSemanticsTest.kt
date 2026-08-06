package com.jeluchu.jchucomponents.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.jeluchu.jchucomponents.ui.composables.colors.JchuColorOption
import com.jeluchu.jchucomponents.ui.composables.colors.JchuColorPicker
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ColorPickerSemanticsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun paletteExposesSelectionAndReportsValue() {
        var selectedValue = "green"

        composeRule.setContent {
            JchuColorPicker(
                options =
                    listOf(
                        JchuColorOption("green", Color.Green, "Green"),
                        JchuColorOption("blue", Color.Blue, "Blue")
                    ),
                selectedValue = selectedValue,
                onValueSelected = { selectedValue = it }
            )
        }

        composeRule.onNodeWithContentDescription("Green").assertIsSelected()
        composeRule
            .onNodeWithContentDescription("Blue")
            .assertIsNotSelected()
            .performClick()

        assertEquals("blue", selectedValue)
    }
}
