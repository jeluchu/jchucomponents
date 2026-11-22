package com.jeluchu.jchucomponents.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.jeluchu.jchucomponents.ui.composables.chips.JchuAmountCounter
import org.junit.Rule
import org.junit.Test

class AmountCounterTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun amountRemainsExposedAsText() {
        composeRule.setContent {
            JchuAmountCounter(amount = "x3", imageVector = Icons.Rounded.Star)
        }

        composeRule.onNodeWithText("x3").assertExists()
    }
}
