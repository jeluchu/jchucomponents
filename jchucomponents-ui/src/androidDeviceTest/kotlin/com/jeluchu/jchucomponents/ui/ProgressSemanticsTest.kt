package com.jeluchu.jchucomponents.ui

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jeluchu.jchucomponents.ui.composables.button.JchuProgressButton
import com.jeluchu.jchucomponents.ui.composables.progress.LinearProgressbar
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ProgressSemanticsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun linearProgress_exposesBoundedRange() {
        composeRule.setContent {
            LinearProgressbar(
                icon = ImageVector.vectorResource(R.drawable.ic_btn_qrcode),
                number = 15f,
                maxNumber = 10f,
                animationDuration = 0
            )
        }

        composeRule
            .onNode(hasProgressRange(current = 10f, max = 10f))
            .assertExists()
    }

    @Test
    fun linearProgress_disabledExposesDisabledState() {
        composeRule.setContent {
            LinearProgressbar(
                icon = ImageVector.vectorResource(R.drawable.ic_btn_qrcode),
                number = 5f,
                maxNumber = 10f,
                enabled = false,
                animationDuration = 0
            )
        }

        composeRule
            .onNode(hasProgressRange(current = 5f, max = 10f))
            .assertIsNotEnabled()
    }

    @Test
    fun progressButton_loadingPreventsClicks() {
        var clicks = 0
        composeRule.setContent {
            JchuProgressButton(
                text = "Continue",
                icon = ImageVector.vectorResource(R.drawable.ic_btn_qrcode),
                isLoading = true,
                onClick = { clicks++ }
            )
        }

        composeRule
            .onNodeWithText("Continue")
            .assertIsNotEnabled()
            .performClick()

        assertEquals(0, clicks)
    }

    private fun hasProgressRange(
        current: Float,
        max: Float
    ): SemanticsMatcher =
        SemanticsMatcher.expectValue(
            SemanticsProperties.ProgressBarRangeInfo,
            ProgressBarRangeInfo(current = current, range = 0f..max)
        )
}
