package com.jeluchu.composer.features.bottons.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.milky
import com.jeluchu.composer.core.ui.theme.secondary
import com.jeluchu.composer.core.utils.DestinationsIds
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.ui.accompanist.systemui.SystemStatusBarColors
import com.jeluchu.jchucomponents.ui.composables.button.FloatingButtonSize
import com.jeluchu.jchucomponents.ui.composables.button.JchuFloatingButton
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors

@Composable
fun FloatingButtonView(onItemClick: (String) -> Unit) {
    SystemStatusBarColors(
        systemBarsColor = secondary,
        statusBarColor = secondary
    )

    FloatingButton(onItemClick)
}

@Composable
private fun FloatingButton(
    onItemClick: (String) -> Unit
) = ScaffoldStructure(
    title = Names.floatingButtons,
    colors = CenterToolbarColors(
        containerColor = secondary,
        contentColor = milky
    ),
    onNavIconClick = { onItemClick(DestinationsIds.back) }
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        JchuFloatingButton(
            size = FloatingButtonSize.Small,
            contentDescription = "Small floating button",
        )
        JchuFloatingButton(
            size = FloatingButtonSize.Medium,
            contentDescription = "Medium floating button",
        )
        JchuFloatingButton(
            enabled = false,
            size = FloatingButtonSize.Large,
            contentDescription = "Disabled floating button",
        )
    }
}
