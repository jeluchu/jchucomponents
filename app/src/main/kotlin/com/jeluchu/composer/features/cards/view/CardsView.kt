package com.jeluchu.composer.features.cards.view

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
import com.jeluchu.composer.features.cards.view.navigation.CardsRoutes
import com.jeluchu.jchucomponents.navigation3.BackStackBack
import com.jeluchu.jchucomponents.navigation3.NavigateTo
import com.jeluchu.jchucomponents.navigation3.Screen
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius

@Composable
@Screen(graph = "Cards")
fun CardsView(
    @BackStackBack onBack: () -> Unit,
    @NavigateTo(route = CardsRoutes.BenefitsView::class) onOpenBenefits: () -> Unit,
    @NavigateTo(route = CardsRoutes.RequirementsCardsView::class) onOpenRequirements: () -> Unit,
    @NavigateTo(route = CardsRoutes.AssistantCardsView::class) onOpenAssistant: () -> Unit,
    @NavigateTo(route = CardsRoutes.CategoryCardsView::class) onOpenCategory: () -> Unit,
    @NavigateTo(route = CardsRoutes.CategoryIconCardsView::class) onOpenCategoryIcon: () -> Unit,
    @NavigateTo(route = CardsRoutes.DebutCardsView::class) onOpenDebut: () -> Unit,
    @NavigateTo(route = CardsRoutes.ExpandableCardsView::class) onOpenExpandable: () -> Unit,
    @NavigateTo(route = CardsRoutes.InfoCardsView::class) onOpenInfo: () -> Unit,
    @NavigateTo(route = CardsRoutes.TeCardsView::class) onOpenTe: () -> Unit
) {
    Toolbars { destination ->
        when (destination) {
            DestinationsIds.back -> onBack()
            DestinationsIds.benefitCards -> onOpenBenefits()
            DestinationsIds.requirementsCards -> onOpenRequirements()
            DestinationsIds.assistantCards -> onOpenAssistant()
            DestinationsIds.categoryCards -> onOpenCategory()
            DestinationsIds.categoryIconCards -> onOpenCategoryIcon()
            DestinationsIds.debutCards -> onOpenDebut()
            DestinationsIds.expandableCards -> onOpenExpandable()
            DestinationsIds.infoCards -> onOpenInfo()
            DestinationsIds.teCards -> onOpenTe()
        }
    }
}

@Composable
private fun Toolbars(onItemClick: (String) -> Unit) =
    ScaffoldStructure(
        title = Names.cards,
        colors =
            CenterToolbarColors(
                containerColor = secondary,
                contentColor = milky
            ),
        onNavIconClick = { onItemClick(DestinationsIds.back) }
    ) {
        MenuOptions.cards.forEach { option ->
            SimpleButton(
                modifier =
                    Modifier
                        .clip(10.cornerRadius())
                        .background(primary.copy(.7f)),
                label = option.name,
                color = Color.DarkGray
            ) { onItemClick(option.id) }
        }
    }
