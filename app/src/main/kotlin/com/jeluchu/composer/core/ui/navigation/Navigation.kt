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
import com.jeluchu.jchucomponents.ui.extensions.navigation.back
import com.jeluchu.jchucomponents.ui.extensions.navigation.navigateSingleTop

@Composable
fun Navigation() {
    val backStack = rememberNavBackStack(CatalogRoute.Dashboard)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.back() },
        entryDecorators =
            listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
        entryProvider =
            entryProvider {
                entry<CatalogRoute.Dashboard> {
                    MainView { id ->
                        when (id) {
                            DestinationsIds.buttons -> backStack.navigateSingleTop(CatalogRoute.Buttons)
                            DestinationsIds.cards -> backStack.navigateSingleTop(CatalogRoute.Cards)
                            DestinationsIds.chips -> backStack.navigateSingleTop(CatalogRoute.Chips)
                            DestinationsIds.inputs -> backStack.navigateSingleTop(CatalogRoute.Inputs)
                            DestinationsIds.loaders -> backStack.navigateSingleTop(CatalogRoute.Loaders)
                            DestinationsIds.progress -> backStack.navigateSingleTop(CatalogRoute.Progress)
                            DestinationsIds.lazyGrids -> backStack.navigateSingleTop(CatalogRoute.LazyGrids)
                            DestinationsIds.dividers -> backStack.navigateSingleTop(CatalogRoute.Dividers)
                            DestinationsIds.toolbars -> backStack.navigateSingleTop(CatalogRoute.Toolbars)
                        }
                    }
                }
                entry<CatalogRoute.Buttons> {
                    ButtonsView { id ->
                        when (id) {
                            DestinationsIds.floatingButton ->
                                backStack.navigateSingleTop(CatalogRoute.FloatingButtons)
                            DestinationsIds.back -> backStack.back()
                        }
                    }
                }
                entry<CatalogRoute.FloatingButtons> {
                    FloatingButtonView { backStack.back() }
                }
                entry<CatalogRoute.Chips> {
                    ChipsView { backStack.back() }
                }
                entry<CatalogRoute.Inputs> {
                    InputsView { backStack.back() }
                }
                entry<CatalogRoute.Loaders> {
                    LoadersView { backStack.back() }
                }
                entry<CatalogRoute.Progress> {
                    ProgressView { id ->
                        when (id) {
                            DestinationsIds.circularProgress ->
                                backStack.navigateSingleTop(CatalogRoute.CircularProgress)
                            DestinationsIds.linearProgress ->
                                backStack.navigateSingleTop(CatalogRoute.LinearProgress)
                            DestinationsIds.iconProgress ->
                                backStack.navigateSingleTop(CatalogRoute.IconProgress)
                            DestinationsIds.back -> backStack.back()
                        }
                    }
                }
                entry<CatalogRoute.CircularProgress> {
                    CircularProgressbarView { backStack.back() }
                }
                entry<CatalogRoute.LinearProgress> {
                    LinearProgressbarView { backStack.back() }
                }
                entry<CatalogRoute.IconProgress> {
                    IconProgressbarView { backStack.back() }
                }
                entry<CatalogRoute.LazyGrids> {
                    LazyGridsView { id ->
                        when (id) {
                            DestinationsIds.lazyStaticGrids ->
                                backStack.navigateSingleTop(CatalogRoute.LazyStaticGrids)
                            DestinationsIds.back -> backStack.back()
                        }
                    }
                }
                entry<CatalogRoute.LazyStaticGrids> {
                    LazyStaticGridView { backStack.back() }
                }
                entry<CatalogRoute.Dividers> {
                    DividersView()
                }
                entry<CatalogRoute.Toolbars> {
                    ToolbarsView { id ->
                        when (id) {
                            DestinationsIds.simpleToolbars ->
                                backStack.navigateSingleTop(CatalogRoute.SimpleToolbars)
                            DestinationsIds.centerToolbars ->
                                backStack.navigateSingleTop(CatalogRoute.CenterToolbars)
                            DestinationsIds.largeToolbars ->
                                backStack.navigateSingleTop(CatalogRoute.LargeToolbars)
                            DestinationsIds.back -> backStack.back()
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
                                backStack.navigateSingleTop(CatalogRoute.BenefitCards)
                            DestinationsIds.back -> backStack.back()
                        }
                    }
                }
                entry<CatalogRoute.BenefitCards> {
                    BenefitsView { backStack.back() }
                }
            }
    )
}
