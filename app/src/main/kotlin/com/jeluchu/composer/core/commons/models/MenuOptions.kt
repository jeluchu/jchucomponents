package com.jeluchu.composer.core.commons.models

import com.jeluchu.composer.core.catalog.CatalogCategory
import com.jeluchu.composer.core.utils.DestinationsIds
import com.jeluchu.composer.core.utils.Names

data class MenuOptions(
    val id: String,
    val name: String
) {
    companion object {
        val ui = listOf(
            MenuOptions(
                id = DestinationsIds.buttons,
                name = CatalogCategory.BUTTONS.title
            ),
            MenuOptions(
                id = DestinationsIds.cards,
                name = CatalogCategory.CARDS.title
            ),
            MenuOptions(
                id = DestinationsIds.chips,
                name = CatalogCategory.CHIPS.title
            ),
            MenuOptions(
                id = DestinationsIds.lazyGrids,
                name = CatalogCategory.LISTS.title
            ),
            MenuOptions(
                id = DestinationsIds.loaders,
                name = CatalogCategory.LOADERS.title
            ),
            MenuOptions(
                id = DestinationsIds.progress,
                name = CatalogCategory.PROGRESS.title
            ),
            MenuOptions(
                id = DestinationsIds.dividers,
                name = Names.dividers
            ),
            MenuOptions(
                id = DestinationsIds.toolbars,
                name = CatalogCategory.TOOLBARS.title
            ),
        )

        val buttons = listOf(
            MenuOptions(
                id = DestinationsIds.floatingButton,
                name = Names.floatingButtons
            ),
        )

        val progress = listOf(
            MenuOptions(
                id = DestinationsIds.circularProgress,
                name = Names.circularProgress
            ),
            MenuOptions(
                id = DestinationsIds.linearProgress,
                name = Names.linearProgress
            ),
            MenuOptions(
                id = DestinationsIds.iconProgress,
                name = Names.iconProgress
            )
        )

        val lazyGrids = listOf(
            MenuOptions(
                id = DestinationsIds.lazyStaticGrids,
                name = Names.lazyStaticGrids
            ),
        )

        val toolbars = listOf(
            MenuOptions(
                id = DestinationsIds.simpleToolbars,
                name = Names.toolbars
            ),
            MenuOptions(
                id = DestinationsIds.centerToolbars,
                name = Names.centerToolbars
            ),
            MenuOptions(
                id = DestinationsIds.largeToolbars,
                name = Names.largeToolbars
            ),
        )

        val cards = listOf(
            MenuOptions(
                id = DestinationsIds.benefitCards,
                name = Names.benefitCards
            ),
        )
    }
}
