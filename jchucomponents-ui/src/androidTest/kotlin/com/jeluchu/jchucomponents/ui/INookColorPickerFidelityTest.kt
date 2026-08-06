package com.jeluchu.jchucomponents.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.performClick
import com.jeluchu.jchucomponents.ui.composables.colors.JchuINookColorPicker
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class INookColorPickerFidelityTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun duplicateValuesAreRemovedAndSelectionReportsOriginalValue() {
        var selected: String? = null

        composeRule.setContent {
            JchuINookColorPicker(
                title = "Phone color",
                description = "Choose a color",
                colors = listOf("green", "blue", "green"),
                selectedColor = "green",
                darkTheme = false,
                colorResolver = { if (it == "green") Color.Green else Color.Blue },
                onColorSelected = { selected = it }
            )
        }

        val options = composeRule.onAllNodes(hasClickAction())
        assertEquals(2, options.fetchSemanticsNodes().size)
        options[1].performClick()
        assertEquals("blue", selected)
    }
}
