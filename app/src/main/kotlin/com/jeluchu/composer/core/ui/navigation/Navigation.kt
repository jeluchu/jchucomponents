package com.jeluchu.composer.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.jeluchu.composer.features.bottons.view.navigation.buttonsEntries
import com.jeluchu.composer.features.cards.view.navigation.cardsEntries
import com.jeluchu.composer.features.chips.view.navigation.chipsEntries
import com.jeluchu.composer.features.dashboard.view.navigation.CatalogRoutes
import com.jeluchu.composer.features.dashboard.view.navigation.catalogEntries
import com.jeluchu.composer.features.dividers.view.navigation.dividersEntries
import com.jeluchu.composer.features.extensions.view.navigation.extensionsEntries
import com.jeluchu.composer.features.inputs.view.navigation.inputsEntries
import com.jeluchu.composer.features.layouts.view.navigation.layoutsEntries
import com.jeluchu.composer.features.lists.view.navigation.listsEntries
import com.jeluchu.composer.features.loaders.view.navigation.loadersEntries
import com.jeluchu.composer.features.navigationcodegen.navigation.MobilityRoutes
import com.jeluchu.composer.features.navigationcodegen.navigation.NavigationCodegenRoutes
import com.jeluchu.composer.features.navigationcodegen.navigation.NookCatalogRoutes
import com.jeluchu.composer.features.navigationcodegen.navigation.mobilityEntries
import com.jeluchu.composer.features.navigationcodegen.navigation.navigationCodegenEntries
import com.jeluchu.composer.features.navigationcodegen.navigation.nookCatalogEntries
import com.jeluchu.composer.features.previews.view.navigation.previewsEntries
import com.jeluchu.composer.features.progress.view.navigation.progressEntries
import com.jeluchu.composer.features.room.view.navigation.roomEntries
import com.jeluchu.composer.features.supabase.view.navigation.supabaseEntries
import com.jeluchu.composer.features.surfaces.view.navigation.surfacesEntries
import com.jeluchu.composer.features.text.view.navigation.textEntries
import com.jeluchu.composer.features.toolbars.view.navigation.toolbarsEntries
import com.jeluchu.jchucomponents.navigation3.extensions.back
import com.jeluchu.jchucomponents.navigation3.extensions.navigateTo

@Composable
fun Navigation() {
    val backStack = rememberNavBackStack(CatalogRoutes.MainView)
    val uriHandler = LocalUriHandler.current

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
                catalogEntries(backStack)
                buttonsEntries(backStack)
                cardsEntries(backStack)
                chipsEntries(backStack)
                dividersEntries(backStack)
                extensionsEntries(backStack)
                inputsEntries(backStack)
                layoutsEntries(backStack)
                listsEntries(backStack)
                loadersEntries(backStack)
                previewsEntries(backStack)
                progressEntries(backStack)
                roomEntries(backStack)
                supabaseEntries(backStack)
                surfacesEntries(backStack)
                textEntries(backStack)
                toolbarsEntries(backStack)
                navigationCodegenEntries(backStack)
                mobilityEntries(
                    backStack = backStack,
                    onOpenUrl = { url -> uriHandler.openUri(url) }
                )
                nookCatalogEntries(backStack)
            }
    )
}
