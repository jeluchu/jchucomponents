package com.jeluchu.jchucomponents.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.composables.column.JchuScrollableColumn
import org.junit.Rule
import org.junit.Test

class ScrollableColumnTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun contentCanScrollToLastItem() {
        composeRule.setContent {
            Box(modifier = Modifier.height(100.dp)) {
                JchuScrollableColumn {
                    repeat(20) { index ->
                        Text("Item ${index + 1}")
                    }
                }
            }
        }

        composeRule
            .onNodeWithText("Item 20")
            .performScrollTo()
            .assertIsDisplayed()
    }
}
