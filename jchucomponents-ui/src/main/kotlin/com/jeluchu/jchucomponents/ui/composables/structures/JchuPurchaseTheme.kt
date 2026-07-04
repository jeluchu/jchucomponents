package com.jeluchu.jchucomponents.ui.composables.structures

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.jeluchu.jchucomponents.ui.composables.textfields.SearchBarDefaults

@Immutable
data class JchuScreenColorTheme(
    val primary: Color,
    val secondary: Color,
    val primaryVariant: Color = primary.copy(alpha = 0.2f),
    val secondaryVariant: Color = secondary.copy(alpha = 0.1f)
)

object JchuAppColorThemes {
    val ableSisters = JchuScreenColorTheme(primary = Color(0xFFBA4A78), secondary = Color(0xFFF6B3CC))
    val nooksCranny = JchuScreenColorTheme(primary = Color(0xFF8A6A43), secondary = Color(0xFFEAD9B8))
    val fossils = JchuScreenColorTheme(primary = Color(0xFF375E86), secondary = Color(0xFFA9D4F0))
    val reactions = JchuScreenColorTheme(primary = Color(0xFF7E5AA7), secondary = Color(0xFFD8C4F1))
    val nookPoints = JchuScreenColorTheme(primary = Color(0xFF386B4F), secondary = Color(0xFFC7E8D1))
    val gyroids = JchuScreenColorTheme(primary = Color(0xFF5D4633), secondary = Color(0xFFC9B59B))
    val amiibos = JchuScreenColorTheme(primary = Color(0xFF2E7C85), secondary = Color(0xFFA8E2E4))
    val critters = JchuScreenColorTheme(primary = Color(0xFF7E6C42), secondary = Color(0xFFF1D982))
    val music = JchuScreenColorTheme(primary = Color(0xFFC46C36), secondary = Color(0xFFF3C29A))
    val garden = JchuScreenColorTheme(primary = Color(0xFF527C4B), secondary = Color(0xFFD8ECCB))
    val villagers = critters
    val characters = JchuScreenColorTheme(primary = Color(0xFF795548), secondary = Color(0xFFD7B89B))
    val designs = JchuScreenColorTheme(primary = Color(0xFFBA4A78), secondary = Color(0xFFF8C6D8))
    val arts = JchuScreenColorTheme(primary = Color(0xFF7E6C42), secondary = Color(0xFFF2E8CB))
    val builds = JchuScreenColorTheme(primary = Color(0xFF8A6A43), secondary = Color(0xFFF2C879))
    val mysteryIslands = JchuScreenColorTheme(primary = Color(0xFF375E86), secondary = Color(0xFFC9E4F8))
    val artists = JchuScreenColorTheme(primary = Color(0xFF3F3A35), secondary = Color(0xFFF6E6C8))
    val nookMiles = JchuScreenColorTheme(primary = Color(0xFF375E86), secondary = Color(0xFFB7D7CA))
    val lego = JchuScreenColorTheme(primary = Color(0xFFB48924), secondary = Color(0xFFF6DD7D))
    val lifSupport = JchuScreenColorTheme(primary = Color(0xFF7E5AA7), secondary = Color(0xFFD6B7EC))
    val mail = JchuScreenColorTheme(primary = Color(0xFFAA3E45), secondary = Color(0xFFF0A9AA))
    val tvShows = JchuScreenColorTheme(primary = Color(0xFF6D4A8E), secondary = Color(0xFFD6B7EC))
    val house = JchuScreenColorTheme(primary = Color(0xFF795548), secondary = Color(0xFFF2C879))
    val museum = JchuScreenColorTheme(primary = Color(0xFF62686D), secondary = Color(0xFFD6D8D9))
    val turnips = JchuScreenColorTheme(primary = Color(0xFF7A4F72), secondary = Color(0xFFEECDE6))
    val recipes = JchuScreenColorTheme(primary = Color(0xFFC56534), secondary = Color(0xFFF5E5C5))
    val happyHomeParadise = JchuScreenColorTheme(primary = Color(0xFFB74248), secondary = Color(0xFFF2A4AA))
    val friends = music
    val locations = JchuScreenColorTheme(primary = Color(0xFF375E86), secondary = Color(0xFFA8E2E4))
    val avatarCreator = JchuScreenColorTheme(
        primary = Color(0xFFC9974D),
        secondary = Color(0xFFD7B89B),
        primaryVariant = Color(0xFFF6EDCF),
        secondaryVariant = Color(0xFF5D4633)
    )
    val dreamSuit = reactions
    val hotel = JchuScreenColorTheme(primary = Color(0xFF367D9F), secondary = Color(0xFFAEDCEB))
    val weather = amiibos
}

@Immutable
class JchuPurchaseScaffoldColors(
    val contentColor: Color = Color.Black,
    val containerColor: Color = Color.White
)

@Immutable
class JchuPurchaseSearchConfig(
    val isActive: Boolean = true,
    val label: String = "Search",
    val contentColor: Color = Color.Black,
    val containerColor: Color = Color.White,
    val query: MutableState<String> = mutableStateOf("")
) {
    fun toSearchBarDefaults() = SearchBarDefaults(
        label = label,
        contentColor = contentColor,
        containerColor = containerColor
    )
}

@Immutable
class JchuPurchaseHeaderConfig(
    val isAllFavourites: Boolean = false,
    val isCompletedByHiddenFavorites: Boolean = false,
    val markAllContent: (@Composable () -> Unit)? = null,
    val progressContent: (@Composable () -> Unit)? = null
)

@Immutable
class JchuPurchaseScaffoldConfig(
    val headerConfig: JchuPurchaseHeaderConfig = JchuPurchaseHeaderConfig(),
    val searchConfig: JchuPurchaseSearchConfig = JchuPurchaseSearchConfig(),
    val emptyContent: @Composable () -> Unit = { JchuDefaultEmptyContent() },
    val errorContent: @Composable (String?) -> Unit = { JchuDefaultEmptyContent("Unable to load content") },
    val hiddenFavoritesContent: @Composable () -> Unit = { JchuDefaultEmptyContent("All achieved") },
    val scaffoldColors: JchuPurchaseScaffoldColors = JchuPurchaseScaffoldColors()
)

@Immutable
class JchuPurchaseTabScaffoldConfig(
    val searchConfig: JchuPurchaseSearchConfig = JchuPurchaseSearchConfig(),
    val tabColors: JchuPurchaseTabColors = JchuPurchaseTabColors(),
    val scaffoldColors: JchuPurchaseScaffoldColors = JchuPurchaseScaffoldColors()
)

@Immutable
class JchuPurchaseTabColors(
    val selectedContentColor: Color = Color.Black,
    val unselectedContentColor: Color = Color.Black.copy(alpha = 0.6f),
    val containerColor: Color = Color.White,
    val selectedContainerColor: Color = Color.Black.copy(alpha = 0.1f)
)

fun JchuScreenColorTheme.toPurchaseScaffoldConfig(
    query: MutableState<String> = mutableStateOf(""),
    labelSearch: String = "Search",
    isSearchActive: Boolean = true,
    headerConfig: JchuPurchaseHeaderConfig = JchuPurchaseHeaderConfig()
) = JchuPurchaseScaffoldConfig(
    headerConfig = headerConfig,
    searchConfig = JchuPurchaseSearchConfig(
        isActive = isSearchActive,
        label = labelSearch,
        query = query,
        contentColor = secondary,
        containerColor = primary.copy(alpha = 0.18f)
    ),
    scaffoldColors = JchuPurchaseScaffoldColors(
        contentColor = primary,
        containerColor = secondary
    )
)

fun JchuScreenColorTheme.toPurchaseTabScaffoldConfig(
    query: MutableState<String> = mutableStateOf(""),
    labelSearch: String = "Search",
    isSearchActive: Boolean = true
) = JchuPurchaseTabScaffoldConfig(
    searchConfig = JchuPurchaseSearchConfig(
        isActive = isSearchActive,
        label = labelSearch,
        query = query,
        contentColor = secondary,
        containerColor = primary.copy(alpha = 0.18f)
    ),
    scaffoldColors = JchuPurchaseScaffoldColors(
        contentColor = primary,
        containerColor = secondary
    ),
    tabColors = JchuPurchaseTabColors(
        selectedContentColor = primary,
        unselectedContentColor = primary.copy(alpha = 0.58f),
        containerColor = primary.copy(alpha = 0.16f),
        selectedContainerColor = secondary.copy(alpha = 0.55f)
    )
)

fun JchuScreenColorTheme.toDetailsScaffoldConfig() = JchuDetailsScaffoldConfig(
    colors = JchuScaffoldColors(
        contentColor = primary,
        containerColor = secondary
    )
)

fun JchuScreenColorTheme.toShareScaffoldConfig() = JchuShareScaffoldConfig(
    scaffoldColors = JchuPurchaseScaffoldColors(
        contentColor = primary,
        containerColor = secondary
    ),
    shareBarColors = JchuShareBarColors(
        containerColor = primary.copy(alpha = 0.16f),
        shareContentColor = secondary,
        shareContainerColor = primary.copy(alpha = 0.82f),
        downloadContentColor = primary,
        downloadContainerColor = primary.copy(alpha = 0.82f)
    )
)
