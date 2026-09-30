package com.jeluchu.composer.features.cards.view

import androidx.compose.runtime.Composable
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.navigation3.BackStackBack
import com.jeluchu.jchucomponents.navigation3.Screen
import com.jeluchu.jchucomponents.ui.composables.cards.AssistantCardPreview
import com.jeluchu.jchucomponents.ui.composables.cards.CategoryCardPreview
import com.jeluchu.jchucomponents.ui.composables.cards.CategoryIconPreview
import com.jeluchu.jchucomponents.ui.composables.cards.DebutCardPreview
import com.jeluchu.jchucomponents.ui.composables.cards.ExpandableCardPreview
import com.jeluchu.jchucomponents.ui.composables.cards.InfoCardPreviewLight
import com.jeluchu.jchucomponents.ui.composables.cards.TePreview

@Composable
@Screen(graph = "Cards")
fun AssistantCardsView(@BackStackBack onBack: () -> Unit) {
    CardPreviewScaffold(Names.assistantCards, onBack) { AssistantCardPreview() }
}

@Composable
@Screen(graph = "Cards")
fun CategoryCardsView(@BackStackBack onBack: () -> Unit) {
    CardPreviewScaffold(Names.categoryCards, onBack) { CategoryCardPreview() }
}

@Composable
@Screen(graph = "Cards")
fun CategoryIconCardsView(@BackStackBack onBack: () -> Unit) {
    CardPreviewScaffold(Names.categoryIconCards, onBack) { CategoryIconPreview() }
}

@Composable
@Screen(graph = "Cards")
fun DebutCardsView(@BackStackBack onBack: () -> Unit) {
    CardPreviewScaffold(Names.debutCards, onBack) { DebutCardPreview() }
}

@Composable
@Screen(graph = "Cards")
fun ExpandableCardsView(@BackStackBack onBack: () -> Unit) {
    CardPreviewScaffold(Names.expandableCards, onBack) { ExpandableCardPreview() }
}

@Composable
@Screen(graph = "Cards")
fun InfoCardsView(@BackStackBack onBack: () -> Unit) {
    CardPreviewScaffold(Names.infoCards, onBack) { InfoCardPreviewLight() }
}

@Composable
@Screen(graph = "Cards")
fun TeCardsView(@BackStackBack onBack: () -> Unit) {
    CardPreviewScaffold(Names.teCards, onBack) { TePreview() }
}

@Composable
private fun CardPreviewScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    ScaffoldStructure(title = title, onNavIconClick = onBack, content = content)
}
