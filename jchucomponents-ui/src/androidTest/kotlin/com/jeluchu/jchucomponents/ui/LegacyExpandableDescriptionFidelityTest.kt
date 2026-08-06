package com.jeluchu.jchucomponents.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jeluchu.jchucomponents.ui.composables.text.JchuLegacyExpandableDescription
import com.jeluchu.jchucomponents.ui.composables.text.JchuLegacyExpandableDescriptionColors
import org.junit.Rule
import org.junit.Test

class LegacyExpandableDescriptionFidelityTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun legacySummaryRemainsInternallyExpandable() {
        val description = "Legacy description content ".repeat(12)

        composeRule.setContent {
            JchuLegacyExpandableDescription(
                image = "",
                title = "Legacy title",
                description = description,
                colors = JchuLegacyExpandableDescriptionColors(
                    contentColor = Color.White,
                    gradientColor = Color.Black,
                    containerColor = Color.Gray
                )
            )
        }

        composeRule.onNodeWithText("Legacy title").assertExists()
        composeRule.onNodeWithText(description).performClick()
    }
}
