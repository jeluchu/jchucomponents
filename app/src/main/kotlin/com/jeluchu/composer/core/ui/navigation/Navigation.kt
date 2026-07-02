package com.jeluchu.composer.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.jeluchu.composer.core.utils.DestinationsIds
import com.jeluchu.composer.features.bottons.view.ButtonsView
import com.jeluchu.composer.features.bottons.view.FloatingButtonView
import com.jeluchu.composer.features.cards.view.BenefitsView
import com.jeluchu.composer.features.cards.view.CardsView
import com.jeluchu.composer.features.chips.view.ChipsView
import com.jeluchu.composer.features.dashboard.view.MainView
import com.jeluchu.composer.features.dividers.view.DividersView
import com.jeluchu.composer.features.inputs.view.InputsView
import com.jeluchu.composer.features.lists.view.LazyGridsView
import com.jeluchu.composer.features.lists.view.LazyStaticGridView
import com.jeluchu.composer.features.loaders.view.LoadersView
import com.jeluchu.composer.features.progress.view.CircularProgressbarView
import com.jeluchu.composer.features.progress.view.IconProgressbarView
import com.jeluchu.composer.features.progress.view.LinearProgressbarView
import com.jeluchu.composer.features.progress.view.ProgressView
import com.jeluchu.composer.features.toolbars.view.ToolbarsView
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarActionsPreview
import com.jeluchu.jchucomponents.ui.composables.toolbars.ToolbarActionsPreview

@Composable
fun Navigation() {
    val backStack = rememberNavBackStack(CatalogRoute.Dashboard)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.goBack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<CatalogRoute.Dashboard> {
                MainView { id ->
                    when (id) {
                        DestinationsIds.buttons -> backStack.navigateTo(CatalogRoute.Buttons)
                        DestinationsIds.cards -> backStack.navigateTo(CatalogRoute.Cards)
                        DestinationsIds.chips -> backStack.navigateTo(CatalogRoute.Chips)
                        DestinationsIds.inputs -> backStack.navigateTo(CatalogRoute.Inputs)
                        DestinationsIds.loaders -> backStack.navigateTo(CatalogRoute.Loaders)
                        DestinationsIds.progress -> backStack.navigateTo(CatalogRoute.Progress)
                        DestinationsIds.lazyGrids -> backStack.navigateTo(CatalogRoute.LazyGrids)
                        DestinationsIds.dividers -> backStack.navigateTo(CatalogRoute.Dividers)
                        DestinationsIds.toolbars -> backStack.navigateTo(CatalogRoute.Toolbars)
                    }
                }
            }
            entry<CatalogRoute.Buttons> {
                ButtonsView { id ->
                    when (id) {
                        DestinationsIds.floatingButton ->
                            backStack.navigateTo(CatalogRoute.FloatingButtons)
                        DestinationsIds.back -> backStack.goBack()
                    }
                }
            }
            entry<CatalogRoute.FloatingButtons> {
                FloatingButtonView { backStack.goBack() }
            }
            entry<CatalogRoute.Chips> {
                ChipsView { backStack.goBack() }
            }
            entry<CatalogRoute.Inputs> {
                InputsView { backStack.goBack() }
            }
            entry<CatalogRoute.Loaders> {
                LoadersView { backStack.goBack() }
            }
            entry<CatalogRoute.Progress> {
                ProgressView { id ->
                    when (id) {
                        DestinationsIds.circularProgress ->
                            backStack.navigateTo(CatalogRoute.CircularProgress)
                        DestinationsIds.linearProgress ->
                            backStack.navigateTo(CatalogRoute.LinearProgress)
                        DestinationsIds.iconProgress ->
                            backStack.navigateTo(CatalogRoute.IconProgress)
                        DestinationsIds.back -> backStack.goBack()
                    }
                }
            }
            entry<CatalogRoute.CircularProgress> {
                CircularProgressbarView { backStack.goBack() }
            }
            entry<CatalogRoute.LinearProgress> {
                LinearProgressbarView { backStack.goBack() }
            }
            entry<CatalogRoute.IconProgress> {
                IconProgressbarView { backStack.goBack() }
            }
            entry<CatalogRoute.LazyGrids> {
                LazyGridsView { id ->
                    when (id) {
                        DestinationsIds.lazyStaticGrids ->
                            backStack.navigateTo(CatalogRoute.LazyStaticGrids)
                        DestinationsIds.back -> backStack.goBack()
                    }
                }
            }
            entry<CatalogRoute.LazyStaticGrids> {
                LazyStaticGridView { backStack.goBack() }
            }
            entry<CatalogRoute.Dividers> {
                DividersView()
            }
            entry<CatalogRoute.Toolbars> {
                ToolbarsView { id ->
                    when (id) {
                        DestinationsIds.simpleToolbars ->
                            backStack.navigateTo(CatalogRoute.SimpleToolbars)
                        DestinationsIds.centerToolbars ->
                            backStack.navigateTo(CatalogRoute.CenterToolbars)
                        DestinationsIds.largeToolbars ->
                            backStack.navigateTo(CatalogRoute.LargeToolbars)
                        DestinationsIds.back -> backStack.goBack()
                    }
                }
            }
            entry<CatalogRoute.SimpleToolbars> {
                ToolbarActionsPreview()
            }
            entry<CatalogRoute.CenterToolbars> {
                CenterToolbarActionsPreview()
            }
            entry<CatalogRoute.LargeToolbars> {
                // Soon
            }
            entry<CatalogRoute.Cards> {
                CardsView { id ->
                    when (id) {
                        DestinationsIds.benefitCards ->
                            backStack.navigateTo(CatalogRoute.BenefitCards)
                        DestinationsIds.back -> backStack.goBack()
                    }
                }
            }
            entry<CatalogRoute.BenefitCards> {
                BenefitsView { backStack.goBack() }
            }
        }
    )
}
