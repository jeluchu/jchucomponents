package com.jeluchu.composer.core.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface CatalogRoute : NavKey {
    @Serializable
    data object Dashboard : CatalogRoute

    @Serializable
    data object Buttons : CatalogRoute

    @Serializable
    data object FloatingButtons : CatalogRoute

    @Serializable
    data object Chips : CatalogRoute

    @Serializable
    data object Inputs : CatalogRoute

    @Serializable
    data object Loaders : CatalogRoute

    @Serializable
    data object Progress : CatalogRoute

    @Serializable
    data object CircularProgress : CatalogRoute

    @Serializable
    data object LinearProgress : CatalogRoute

    @Serializable
    data object IconProgress : CatalogRoute

    @Serializable
    data object LazyGrids : CatalogRoute

    @Serializable
    data object LazyStaticGrids : CatalogRoute

    @Serializable
    data object Dividers : CatalogRoute

    @Serializable
    data object Toolbars : CatalogRoute

    @Serializable
    data object SimpleToolbars : CatalogRoute

    @Serializable
    data object CenterToolbars : CatalogRoute

    @Serializable
    data object LargeToolbars : CatalogRoute

    @Serializable
    data object Cards : CatalogRoute

    @Serializable
    data object BenefitCards : CatalogRoute
}
