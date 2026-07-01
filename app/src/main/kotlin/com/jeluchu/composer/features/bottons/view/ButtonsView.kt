package com.jeluchu.composer.features.bottons.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.jeluchu.composer.core.commons.models.MenuOptions
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.composables.SimpleButton
import com.jeluchu.composer.core.ui.theme.milky
import com.jeluchu.composer.core.ui.theme.primary
import com.jeluchu.composer.core.ui.theme.secondary
import com.jeluchu.composer.core.utils.DestinationsIds
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.ui.accompanist.systemui.SystemStatusBarColors
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors
import com.jeluchu.jchucomponents.ui.composables.button.ProgressIndicatorButton
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.foundation.components.JchuProgressButtonState

@Composable
fun ButtonsView(onItemClick: (String) -> Unit) {
    SystemStatusBarColors(
        systemBarsColor = secondary,
        statusBarColor = secondary
    )

    Buttons(onItemClick)
}

@Composable
private fun Buttons(
    onItemClick: (String) -> Unit
) = ScaffoldStructure(
    title = Names.buttons,
    colors = CenterToolbarColors(
        containerColor = secondary,
        contentColor = milky
    ),
    onNavIconClick = { onItemClick(DestinationsIds.back) }
) {
    var interactiveLoading by remember { mutableStateOf(false) }

    Text("Progress buttons")
    listOf(
        JchuProgressButtonState("Normal"),
        JchuProgressButtonState("Loading", isLoading = true),
        JchuProgressButtonState("Disabled", isEnabled = false),
        JchuProgressButtonState("Interactive", isLoading = interactiveLoading),
    ).forEach { state ->
        ProgressIndicatorButton(
            modifier = Modifier.fillMaxWidth(),
            text = state.title,
            icon = Icons.Default.Check,
            isLoading = state.isLoading,
            enabled = state.isEnabled,
            onClick = {
                if (state.title == "Interactive") {
                    interactiveLoading = !interactiveLoading
                }
            }
        )
    }

    Text("Other buttons")
    MenuOptions.buttons.forEach { option ->
        SimpleButton(
            modifier = Modifier
                .clip(10.cornerRadius())
                .background(primary.copy(.7f)),
            label = option.name,
            color = Color.DarkGray
        ) { onItemClick(option.id) }
    }
}
