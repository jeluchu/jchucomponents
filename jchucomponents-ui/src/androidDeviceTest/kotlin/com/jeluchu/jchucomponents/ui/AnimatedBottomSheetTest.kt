package com.jeluchu.jchucomponents.ui

import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.jeluchu.jchucomponents.ui.composables.sheets.JchuAnimatedBottomSheet
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
class AnimatedBottomSheetTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun nullInitialValue_doesNotRequestDismissal() {
        var dismissRequests = 0

        composeRule.setContent {
            JchuAnimatedBottomSheet<String>(
                value = null,
                onDismissRequest = { dismissRequests++ }
            ) {
                Text(it)
            }
        }

        composeRule.waitForIdle()

        assertEquals(0, dismissRequests)
    }

    @Test
    fun nonNullValue_isProvidedToContent() {
        composeRule.setContent {
            JchuAnimatedBottomSheet(
                value = "Reusable sheet content",
                onDismissRequest = {}
            ) {
                Text(it)
            }
        }

        composeRule
            .onNodeWithText("Reusable sheet content")
            .assertExists()
    }
}
