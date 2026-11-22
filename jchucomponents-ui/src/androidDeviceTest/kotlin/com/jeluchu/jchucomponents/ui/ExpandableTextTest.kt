package com.jeluchu.jchucomponents.ui

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jeluchu.jchucomponents.ui.composables.text.JchuExpandableText
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ExpandableTextTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun expandActionReportsRequestedState() {
        var requestedExpanded = false

        composeRule.setContent {
            JchuExpandableText(
                text = "Reusable text ".repeat(20),
                expanded = false,
                onExpandedChange = { requestedExpanded = it },
                expandLabel = "Read more",
                collapseLabel = "Read less"
            )
        }

        composeRule.onNodeWithText("Read less").assertDoesNotExist()
        composeRule.onNodeWithText("Read more").assertExists().performClick()

        assertTrue(requestedExpanded)
    }
}
