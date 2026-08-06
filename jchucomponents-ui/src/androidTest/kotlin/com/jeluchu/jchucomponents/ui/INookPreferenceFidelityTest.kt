package com.jeluchu.jchucomponents.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jeluchu.jchucomponents.ui.composables.preferences.JchuPlainPreference
import com.jeluchu.jchucomponents.ui.composables.preferences.JchuSwitchPreference
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class INookPreferenceFidelityTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun plainPreferenceReportsRowClick() {
        var clicks = 0

        composeRule.setContent {
            JchuPlainPreference(
                title = "Catalog updates",
                navigationContentDescription = "Go back",
                onClick = { clicks++ }
            )
        }

        composeRule.onNodeWithText("Catalog updates").performClick()
        assertEquals(1, clicks)
    }

    @Test
    fun switchPreferenceRowReportsInverseValue() {
        var requestedValue: Boolean? = null

        composeRule.setContent {
            JchuSwitchPreference(
                title = "Enable previews",
                value = true,
                darkTheme = false,
                imageVector = Icons.Outlined.Notifications,
                onValueChange = { requestedValue = it }
            )
        }

        composeRule.onNodeWithText("Enable previews").performClick()
        assertEquals(false, requestedValue)
    }
}
