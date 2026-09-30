package com.jeluchu.composer.features.toolbars.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.navigation3.BackStackBack
import com.jeluchu.jchucomponents.navigation3.Screen
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarActionsPreview
import com.jeluchu.jchucomponents.ui.composables.toolbars.ToolbarActionsPreview

@Composable
@Screen(graph = "Toolbars")
fun SimpleToolbarsView(@BackStackBack onBack: () -> Unit) {
    ToolbarPreviewScaffold("Simple toolbars", onBack) { ToolbarActionsPreview() }
}

@Composable
@Screen(graph = "Toolbars")
fun CenterToolbarsView(@BackStackBack onBack: () -> Unit) {
    ToolbarPreviewScaffold(Names.centerToolbars, onBack) { CenterToolbarActionsPreview() }
}

@Composable
@Screen(graph = "Toolbars")
fun LargeToolbarsView(@BackStackBack onBack: () -> Unit) {
    ScaffoldStructure(title = Names.largeToolbars, onNavIconClick = onBack) {
        Text("Próximamente")
    }
}

@Composable
private fun ToolbarPreviewScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    ScaffoldStructure(title = title, onNavIconClick = onBack, content = content)
}
