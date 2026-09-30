package com.jeluchu.composer.features.lists.view

import androidx.compose.runtime.Composable
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.milky
import com.jeluchu.composer.core.ui.theme.secondary
import com.jeluchu.composer.core.utils.DestinationsIds
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.navigation3.BackStackBack
import com.jeluchu.jchucomponents.navigation3.Screen
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors
import com.jeluchu.jchucomponents.ui.foundation.lists.LazyStaticGridPreview

@Composable
@Screen(graph = "Lists")
fun LazyStaticGridView(@BackStackBack onBack: () -> Unit) {
    LazyStaticGrid { onBack() }
}

@Composable
private fun LazyStaticGrid(onItemClick: (String) -> Unit) =
    ScaffoldStructure(
        title = Names.lazyStaticGrids,
        colors =
            CenterToolbarColors(
                containerColor = secondary,
                contentColor = milky
            ),
        onNavIconClick = { onItemClick(DestinationsIds.back) }
    ) { LazyStaticGridPreview() }
