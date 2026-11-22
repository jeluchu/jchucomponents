package com.jeluchu.jchucomponents.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jeluchu.jchucomponents.ui.composables.cards.JchuINookAmountInfoColors
import com.jeluchu.jchucomponents.ui.composables.cards.JchuINookRequirementsCard
import com.jeluchu.jchucomponents.ui.composables.cards.JchuINookRequirementsCardColors
import org.junit.Rule
import org.junit.Test

class INookRequirementsCardFidelityTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun textAndVectorVariantsKeepTheirIndependentContracts() {
        composeRule.setContent {
            Row {
                JchuINookRequirementsCard(
                    title = "Customization kit",
                    amount = "x2",
                    imageVector = Icons.Default.Info
                )
                JchuINookRequirementsCard(
                    title = "Lighting type",
                    requirement = "Fluorescent"
                )
            }
        }

        composeRule.onNodeWithText("Customization kit").assertExists()
        composeRule.onNodeWithText("x2").assertExists()
        composeRule.onNodeWithText("Lighting type").assertExists()
        composeRule.onNodeWithText("Fluorescent").assertExists()
    }

    @Test
    fun amountInfoRetainsItsInternalDialogInteraction() {
        composeRule.setContent {
            JchuINookRequirementsCard(
                title = "Appearance",
                amount = "10%",
                colors = JchuINookRequirementsCardColors(
                    contentColor = Color.Black,
                    amountInfoColors = JchuINookAmountInfoColors(
                        contentColor = Color.Black,
                        containerColor = Color.LightGray
                    )
                )
            )
        }

        composeRule.onNodeWithContentDescription("10%").performClick()
        composeRule.onNodeWithText("Title").assertExists()
        composeRule.onNodeWithText("Description").assertExists()
        composeRule.onNodeWithText("Close").performClick()
        composeRule.onNodeWithText("Title").assertDoesNotExist()
    }
}
