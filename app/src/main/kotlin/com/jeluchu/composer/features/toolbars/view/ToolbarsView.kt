package com.jeluchu.composer.features.toolbars.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jeluchu.composer.core.commons.models.MenuOptions
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.composables.SimpleButton
import com.jeluchu.composer.core.ui.theme.primary
import com.jeluchu.composer.core.ui.theme.secondary
import com.jeluchu.composer.core.utils.DestinationsIds
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.ktx.compose.toImageVector
import com.jeluchu.jchucomponents.ui.accompanist.systemui.SystemStatusBarColors
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.ui.foundation.icon.IconLink

@Composable
fun ToolbarsView(onItemClick: (String) -> Unit) {
    SystemStatusBarColors(
        systemBarsColor = secondary,
        statusBarColor = secondary
    )

    Toolbars(onItemClick)
}

@Composable
private fun Toolbars(
    onItemClick: (String) -> Unit
) = ScaffoldStructure(
    title = Names.toolbars,
    navigationIcon = {
        IconLink(
            imageVector = com.jeluchu.jchucomponents.ui.R.drawable.ic_arrow_left.toImageVector(),
            contentDescription = "ToolbarBack"
        ) { onItemClick(DestinationsIds.back) }
    }
) {
    MenuOptions.toolbars.forEach { option ->
        SimpleButton(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .clip(10.cornerRadius())
                .background(primary.copy(.7f)),
            label = option.name,
            color = Color.DarkGray
        ) { onItemClick(option.id) }
    }
}