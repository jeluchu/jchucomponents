package com.jeluchu.jchucomponents.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.jeluchu.jchucomponents.ui.composables.text.JchuLegacyExpandableDescriptionColors
import com.jeluchu.jchucomponents.ui.composables.text.JchuLegacySimpleDescription
import org.junit.Rule
import org.junit.Test

class LegacySimpleDescriptionFidelityTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun staticCardDisplaysOriginalTitleAndDescriptionWithoutTransformingThem() {
        val title = "Pocket Camp\nappearance"
        val description = "First paragraph\n\n\nSecond paragraph  "

        composeRule.setContent {
            JchuLegacySimpleDescription(
                image = "",
                title = title,
                description = description,
                colors = JchuLegacyExpandableDescriptionColors(
                    contentColor = Color.White,
                    gradientColor = Color.Magenta,
                    containerColor = Color.DarkGray
                )
            )
        }

        composeRule.onNodeWithText(title).assertExists()
        composeRule.onNodeWithText(description).assertExists()
    }
}
