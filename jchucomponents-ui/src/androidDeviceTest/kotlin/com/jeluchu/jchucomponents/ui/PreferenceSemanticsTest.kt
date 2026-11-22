package com.jeluchu.jchucomponents.ui

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jeluchu.jchucomponents.ui.composables.preferences.JchuPreferenceChoice
import com.jeluchu.jchucomponents.ui.composables.preferences.JchuPreferenceSwitch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PreferenceSemanticsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun switchRowReportsNextCheckedValue() {
        var reportedValue = false

        composeRule.setContent {
            JchuPreferenceSwitch(
                title = "Notifications",
                checked = false,
                onCheckedChange = { reportedValue = it }
            )
        }

        composeRule.onNodeWithText("Notifications").performClick()

        assertTrue(reportedValue)
    }

    @Test
    fun choiceUsesSingleSelectableRow() {
        var clicks = 0

        composeRule.setContent {
            JchuPreferenceChoice(
                title = "Daily",
                selected = true,
                onClick = { clicks++ }
            )
        }

        composeRule
            .onNodeWithText("Daily")
            .assertIsSelected()
            .performClick()

        assertEquals(1, clicks)
    }
}
