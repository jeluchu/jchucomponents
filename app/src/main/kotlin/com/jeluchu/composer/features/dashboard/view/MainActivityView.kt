package com.jeluchu.composer.features.dashboard.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.commons.models.MenuOptions
import com.jeluchu.composer.core.ui.composables.ScaffoldListStructure
import com.jeluchu.composer.core.ui.composables.SimpleButton
import com.jeluchu.composer.core.ui.theme.darkness
import com.jeluchu.composer.core.ui.theme.milky
import com.jeluchu.composer.core.ui.theme.primary
import com.jeluchu.composer.core.ui.theme.secondary
import com.jeluchu.composer.core.utils.DestinationsIds
import com.jeluchu.composer.features.bottons.view.navigation.ButtonsRoutes
import com.jeluchu.composer.features.cards.view.navigation.CardsRoutes
import com.jeluchu.composer.features.chips.view.navigation.ChipsRoutes
import com.jeluchu.composer.features.dividers.view.navigation.DividersRoutes
import com.jeluchu.composer.features.extensions.view.navigation.ExtensionsRoutes
import com.jeluchu.composer.features.inputs.view.navigation.InputsRoutes
import com.jeluchu.composer.features.layouts.view.navigation.LayoutsRoutes
import com.jeluchu.composer.features.lists.view.navigation.ListsRoutes
import com.jeluchu.composer.features.loaders.view.navigation.LoadersRoutes
import com.jeluchu.composer.features.navigationcodegen.navigation.MobilityRoutes
import com.jeluchu.composer.features.navigationcodegen.navigation.NavigationCodegenRoutes
import com.jeluchu.composer.features.navigationcodegen.navigation.NookCatalogRoutes
import com.jeluchu.composer.features.previews.view.navigation.PreviewsRoutes
import com.jeluchu.composer.features.progress.view.navigation.ProgressRoutes
import com.jeluchu.composer.features.room.view.navigation.RoomRoutes
import com.jeluchu.composer.features.supabase.view.navigation.SupabaseRoutes
import com.jeluchu.composer.features.surfaces.view.navigation.SurfacesRoutes
import com.jeluchu.composer.features.text.view.navigation.TextRoutes
import com.jeluchu.composer.features.toolbars.view.navigation.ToolbarsRoutes
import com.jeluchu.jchucomponents.navigation3.NavigateTo
import com.jeluchu.jchucomponents.navigation3.Screen
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors

@Composable
@Screen(graph = "Catalog")
fun MainView(
    @NavigateTo(route = RoomRoutes.RoomView::class) onOpenRoom: () -> Unit,
    @NavigateTo(route = TextRoutes.TextView::class) onOpenText: () -> Unit,
    @NavigateTo(route = CardsRoutes.CardsView::class) onOpenCards: () -> Unit,
    @NavigateTo(route = ChipsRoutes.ChipsView::class) onOpenChips: () -> Unit,
    @NavigateTo(route = InputsRoutes.InputsView::class) onOpenInputs: () -> Unit,
    @NavigateTo(route = LayoutsRoutes.LayoutsView::class) onOpenLayouts: () -> Unit,
    @NavigateTo(route = ButtonsRoutes.ButtonsView::class) onOpenButtons: () -> Unit,
    @NavigateTo(route = LoadersRoutes.LoadersView::class) onOpenLoaders: () -> Unit,
    @NavigateTo(route = PreviewsRoutes.PreviewsView::class) onOpenPreviews: () -> Unit,
    @NavigateTo(route = SupabaseRoutes.SupabaseView::class) onOpenSupabase: () -> Unit,
    @NavigateTo(route = ProgressRoutes.ProgressView::class) onOpenProgress: () -> Unit,
    @NavigateTo(route = SurfacesRoutes.SurfacesView::class) onOpenSurfaces: () -> Unit,
    @NavigateTo(route = DividersRoutes.DividersView::class) onOpenDividers: () -> Unit,
    @NavigateTo(route = ToolbarsRoutes.ToolbarsView::class) onOpenToolbars: () -> Unit,
    @NavigateTo(route = ListsRoutes.LazyGridsView::class) onOpenLazyGrids: () -> Unit,
    @NavigateTo(route = MobilityRoutes.MobilityHomeRoute::class) onOpenMobility: () -> Unit,
    @NavigateTo(route = ExtensionsRoutes.ExtensionsView::class) onOpenExtensions: () -> Unit,
    @NavigateTo(route = NookCatalogRoutes.NookCatalogHomeRoute::class) onOpenNookCatalog: () -> Unit,
    @NavigateTo(route = NavigationCodegenRoutes.NavigationCodegenView::class) onOpenNavigationCodegen: () -> Unit
) = ScaffoldListStructure(
    title = "Jchucomponents",
    navIcon = com.jeluchu.jchucomponents.ui.R.drawable.ic_deco_jeluchu,
    colors =
        CenterToolbarColors(
            containerColor = primary,
            contentColor = darkness
        )
) {
    stickyHeader {
        Text(
            text = "UI Components",
            color = milky.copy(.8f),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(secondary)
                    .padding(JchuCatalogTheme.spacing.dimen15),
            fontWeight = FontWeight.Bold
        )
    }

    items(MenuOptions.ui) { option ->
        SimpleButton(
            modifier =
                Modifier
                    .padding(horizontal = JchuCatalogTheme.spacing.dimen15)
                    .clip(JchuCatalogTheme.shapes.corner10)
                    .background(JchuCatalogTheme.colors.surface),
            label = option.name,
            color = JchuCatalogTheme.colors.accent,
            onClick = {
                when (option.id) {
                    DestinationsIds.room -> onOpenRoom()
                    DestinationsIds.text -> onOpenText()
                    DestinationsIds.cards -> onOpenCards()
                    DestinationsIds.chips -> onOpenChips()
                    DestinationsIds.inputs -> onOpenInputs()
                    DestinationsIds.loaders -> onOpenLoaders()
                    DestinationsIds.layouts -> onOpenLayouts()
                    DestinationsIds.buttons -> onOpenButtons()
                    DestinationsIds.previews -> onOpenPreviews()
                    DestinationsIds.dividers -> onOpenDividers()
                    DestinationsIds.toolbars -> onOpenToolbars()
                    DestinationsIds.mobility -> onOpenMobility()
                    DestinationsIds.progress -> onOpenProgress()
                    DestinationsIds.surfaces -> onOpenSurfaces()
                    DestinationsIds.supabase -> onOpenSupabase()
                    DestinationsIds.lazyGrids -> onOpenLazyGrids()
                    DestinationsIds.extensions -> onOpenExtensions()
                    DestinationsIds.nookCatalog -> onOpenNookCatalog()
                    DestinationsIds.navigationCodegen -> onOpenNavigationCodegen()
                }
            }
        )
    }
}
