package com.jeluchu.composer.features.toolbars.view

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
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
import com.jeluchu.composer.features.toolbars.view.navigation.ToolbarsRoutes
import com.jeluchu.jchucomponents.navigation3.BackStackBack
import com.jeluchu.jchucomponents.navigation3.NavigateTo
import com.jeluchu.jchucomponents.navigation3.Screen
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors
import com.jeluchu.jchucomponents.ui.theme.JchuTheme

@Composable
@Screen(graph = "Toolbars")
fun ToolbarsView(
    @BackStackBack onBack: () -> Unit,
    @NavigateTo(route = ToolbarsRoutes.SimpleToolbarsView::class)
    onOpenSimple: () -> Unit,
    @NavigateTo(route = ToolbarsRoutes.CenterToolbarsView::class)
    onOpenCenter: () -> Unit,
    @NavigateTo(route = ToolbarsRoutes.LargeToolbarsView::class)
    onOpenLarge: () -> Unit
) {
    Toolbars { destination ->
        when (destination) {
            DestinationsIds.back -> onBack()
            DestinationsIds.simpleToolbars -> onOpenSimple()
            DestinationsIds.centerToolbars -> onOpenCenter()
            DestinationsIds.largeToolbars -> onOpenLarge()
        }
    }
}

@Composable
private fun Toolbars(onItemClick: (String) -> Unit) =
    ScaffoldStructure(
        title = Names.toolbars,
        colors =
            CenterToolbarColors(
                containerColor = secondary,
                contentColor = milky
            ),
        onNavIconClick = { onItemClick(DestinationsIds.back) }
    ) {
        MenuOptions.toolbars.forEach { option ->
            SimpleButton(
                modifier =
                    Modifier
                        .clip(JchuTheme.shapes.corner10)
                        .background(primary.copy(.7f)),
                label = option.name,
                color = Color.DarkGray
            ) { onItemClick(option.id) }
        }
    }
