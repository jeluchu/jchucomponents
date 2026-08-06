package com.jeluchu.jchucomponents.ui

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jeluchu.jchucomponents.ui.composables.text.JchuExpandableDescriptionConfig
import com.jeluchu.jchucomponents.ui.composables.text.JchuSimpleExpandableText
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class INookExpandableDescriptionFidelityTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun summaryOwnsStateAndReportsToggle() {
        val description = "Original expandable description ".repeat(8)
        var expanded: Boolean? = null

        composeRule.setContent {
            JchuSimpleExpandableText(
                description = description,
                config = JchuExpandableDescriptionConfig(enableHapticFeedback = false),
                onExpandedChange = { expanded = it }
            )
        }

        composeRule.onNodeWithText(description).performClick()
        assertEquals(true, expanded)
    }
}
