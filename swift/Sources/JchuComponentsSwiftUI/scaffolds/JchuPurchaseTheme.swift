import SwiftUI

/// A four-role color palette used to derive screen-specific configurations.
public struct JchuScreenColorTheme: Sendable {
    public var primary: Color
    public var secondary: Color
    public var primaryVariant: Color
    public var secondaryVariant: Color

    public init(
        primary: Color,
        secondary: Color,
        primaryVariant: Color? = nil,
        secondaryVariant: Color? = nil
    ) {
        self.primary = primary
        self.secondary = secondary
        self.primaryVariant = primaryVariant ?? primary.opacity(0.2)
        self.secondaryVariant = secondaryVariant ?? secondary.opacity(0.1)
    }
}

/// Built-in application color presets.
///
/// These presets are conveniences; reusable components accept custom
/// ``JchuScreenColorTheme`` values and do not require a preset.
public enum JchuAppColorThemes {
    public static let ableSisters = JchuScreenColorTheme(primary: Color(red: 0.73, green: 0.29, blue: 0.47), secondary: Color(red: 0.96, green: 0.70, blue: 0.80))
    public static let nooksCranny = JchuScreenColorTheme(primary: Color(red: 0.54, green: 0.42, blue: 0.26), secondary: Color(red: 0.92, green: 0.85, blue: 0.72))
    public static let fossils = JchuScreenColorTheme(primary: Color(red: 0.22, green: 0.37, blue: 0.53), secondary: Color(red: 0.66, green: 0.83, blue: 0.94))
    public static let reactions = JchuScreenColorTheme(primary: Color(red: 0.49, green: 0.35, blue: 0.65), secondary: Color(red: 0.85, green: 0.77, blue: 0.95))
    public static let nookPoints = JchuScreenColorTheme(primary: Color(red: 0.22, green: 0.42, blue: 0.31), secondary: Color(red: 0.78, green: 0.91, blue: 0.82))
    public static let gyroids = JchuScreenColorTheme(primary: Color(red: 0.36, green: 0.27, blue: 0.20), secondary: Color(red: 0.79, green: 0.71, blue: 0.61))
    public static let amiibos = JchuScreenColorTheme(primary: Color(red: 0.18, green: 0.49, blue: 0.52), secondary: Color(red: 0.66, green: 0.89, blue: 0.89))
    public static let critters = JchuScreenColorTheme(primary: Color(red: 0.49, green: 0.42, blue: 0.26), secondary: Color(red: 0.95, green: 0.85, blue: 0.51))
    public static let music = JchuScreenColorTheme(primary: Color(red: 0.77, green: 0.42, blue: 0.21), secondary: Color(red: 0.95, green: 0.76, blue: 0.60))
    public static let garden = JchuScreenColorTheme(primary: Color(red: 0.32, green: 0.49, blue: 0.29), secondary: Color(red: 0.85, green: 0.93, blue: 0.80))
    public static let villagers = critters
    public static let characters = JchuScreenColorTheme(primary: Color(red: 0.47, green: 0.33, blue: 0.28), secondary: Color(red: 0.84, green: 0.72, blue: 0.61))
    public static let designs = JchuScreenColorTheme(primary: Color(red: 0.73, green: 0.29, blue: 0.47), secondary: Color(red: 0.97, green: 0.78, blue: 0.85))
    public static let arts = JchuScreenColorTheme(primary: Color(red: 0.49, green: 0.42, blue: 0.26), secondary: Color(red: 0.95, green: 0.91, blue: 0.80))
    public static let builds = JchuScreenColorTheme(primary: Color(red: 0.54, green: 0.42, blue: 0.26), secondary: Color(red: 0.95, green: 0.78, blue: 0.47))
    public static let mysteryIslands = JchuScreenColorTheme(primary: Color(red: 0.22, green: 0.37, blue: 0.53), secondary: Color(red: 0.79, green: 0.89, blue: 0.97))
    public static let artists = JchuScreenColorTheme(primary: Color(red: 0.25, green: 0.23, blue: 0.21), secondary: Color(red: 0.96, green: 0.90, blue: 0.78))
    public static let nookMiles = JchuScreenColorTheme(primary: Color(red: 0.22, green: 0.37, blue: 0.53), secondary: Color(red: 0.72, green: 0.84, blue: 0.79))
    public static let lego = JchuScreenColorTheme(primary: Color(red: 0.71, green: 0.54, blue: 0.14), secondary: Color(red: 0.96, green: 0.87, blue: 0.49))
    public static let lifSupport = reactions
    public static let mail = JchuScreenColorTheme(primary: Color(red: 0.67, green: 0.24, blue: 0.27), secondary: Color(red: 0.94, green: 0.66, blue: 0.67))
    public static let tvShows = JchuScreenColorTheme(primary: Color(red: 0.43, green: 0.29, blue: 0.56), secondary: Color(red: 0.84, green: 0.72, blue: 0.93))
    public static let house = JchuScreenColorTheme(primary: Color(red: 0.47, green: 0.33, blue: 0.28), secondary: Color(red: 0.95, green: 0.78, blue: 0.47))
    public static let museum = JchuScreenColorTheme(primary: Color(red: 0.38, green: 0.41, blue: 0.43), secondary: Color(red: 0.84, green: 0.85, blue: 0.85))
    public static let turnips = JchuScreenColorTheme(primary: Color(red: 0.48, green: 0.31, blue: 0.45), secondary: Color(red: 0.93, green: 0.80, blue: 0.90))
    public static let recipes = JchuScreenColorTheme(primary: Color(red: 0.77, green: 0.40, blue: 0.20), secondary: Color(red: 0.96, green: 0.90, blue: 0.77))
    public static let happyHomeParadise = JchuScreenColorTheme(primary: Color(red: 0.72, green: 0.26, blue: 0.28), secondary: Color(red: 0.95, green: 0.64, blue: 0.67))
    public static let friends = music
    public static let locations = JchuScreenColorTheme(primary: Color(red: 0.22, green: 0.37, blue: 0.53), secondary: Color(red: 0.66, green: 0.89, blue: 0.89))
    public static let avatarCreator = JchuScreenColorTheme(primary: Color(red: 0.79, green: 0.59, blue: 0.30), secondary: Color(red: 0.84, green: 0.72, blue: 0.61))
    public static let dreamSuit = reactions
    public static let hotel = JchuScreenColorTheme(primary: Color(red: 0.21, green: 0.49, blue: 0.62), secondary: Color(red: 0.68, green: 0.86, blue: 0.92))
    public static let weather = amiibos
}

/// Foreground and background colors for purchase scaffolds.
public struct JchuPurchaseScaffoldColors {
    public var contentColor: Color
    public var containerColor: Color

    public init(
        contentColor: Color = .primary,
        containerColor: Color = Color(uiColor: .systemBackground)
    ) {
        self.contentColor = contentColor
        self.containerColor = containerColor
    }
}

/// Search behavior and appearance for purchase scaffolds.
public struct JchuPurchaseSearchConfig {
    public var isActive: Bool
    public var label: LocalizedStringKey
    public var contentColor: Color
    public var containerColor: Color
    public var query: Binding<String>

    public init(
        isActive: Bool = true,
        label: LocalizedStringKey = "Search",
        contentColor: Color = .primary,
        containerColor: Color = Color(uiColor: .secondarySystemBackground),
        query: Binding<String> = .constant("")
    ) {
        self.isActive = isActive
        self.label = label
        self.contentColor = contentColor
        self.containerColor = containerColor
        self.query = query
    }

    public var searchBarDefaults: SearchBarDefaults {
        SearchBarDefaults(
            label: label,
            containerColor: containerColor,
            contentColor: contentColor
        )
    }
}

/// Optional content displayed above a purchase collection.
public struct JchuPurchaseHeaderConfig {
    public var isCompletedByHiddenFavorites: Bool
    public var progressContent: AnyView?
    public var markAllContent: AnyView?

    public init(
        isCompletedByHiddenFavorites: Bool = false,
        progressContent: AnyView? = nil,
        markAllContent: AnyView? = nil
    ) {
        self.isCompletedByHiddenFavorites = isCompletedByHiddenFavorites
        self.progressContent = progressContent
        self.markAllContent = markAllContent
    }
}

/// State views, search behavior and colors for a purchase-elements scaffold.
public struct JchuPurchaseScaffoldConfig {
    public var headerConfig: JchuPurchaseHeaderConfig
    public var searchConfig: JchuPurchaseSearchConfig
    public var emptyContent: () -> AnyView
    public var errorContent: (String?) -> AnyView
    public var hiddenFavoritesContent: () -> AnyView
    public var scaffoldColors: JchuPurchaseScaffoldColors

    @MainActor
    public init(
        headerConfig: JchuPurchaseHeaderConfig = JchuPurchaseHeaderConfig(),
        searchConfig: JchuPurchaseSearchConfig = JchuPurchaseSearchConfig(),
        emptyContent: @escaping () -> AnyView = { AnyView(JchuDefaultEmptyState()) },
        errorContent: @escaping (String?) -> AnyView = { _ in
            AnyView(JchuDefaultEmptyState("Unable to load content", systemImage: "exclamationmark.triangle"))
        },
        hiddenFavoritesContent: @escaping () -> AnyView = { AnyView(JchuDefaultEmptyState("All achieved")) },
        scaffoldColors: JchuPurchaseScaffoldColors = JchuPurchaseScaffoldColors()
    ) {
        self.headerConfig = headerConfig
        self.searchConfig = searchConfig
        self.emptyContent = emptyContent
        self.errorContent = errorContent
        self.hiddenFavoritesContent = hiddenFavoritesContent
        self.scaffoldColors = scaffoldColors
    }
}

/// Semantic colors for ``JchuPurchaseTabBar``.
public struct JchuPurchaseTabColors {
    public var selectedContentColor: Color
    public var unselectedContentColor: Color
    public var containerColor: Color
    public var selectedContainerColor: Color
    public var contentColor: Color

    public init(
        selectedContentColor: Color = .primary,
        unselectedContentColor: Color = .secondary,
        containerColor: Color = Color(uiColor: .secondarySystemBackground),
        selectedContainerColor: Color = Color.accentColor.opacity(0.15),
        contentColor: Color = .clear
    ) {
        self.selectedContentColor = selectedContentColor
        self.unselectedContentColor = unselectedContentColor
        self.containerColor = containerColor
        self.selectedContainerColor = selectedContainerColor
        self.contentColor = contentColor
    }
}

/// Search, tab and scaffold configuration for tabbed purchase screens.
public struct JchuPurchaseTabScaffoldConfig {
    public var tabColors: JchuPurchaseTabColors
    public var searchConfig: JchuPurchaseSearchConfig
    public var scaffoldColors: JchuPurchaseScaffoldColors

    public init(
        tabColors: JchuPurchaseTabColors = JchuPurchaseTabColors(),
        searchConfig: JchuPurchaseSearchConfig = JchuPurchaseSearchConfig(),
        scaffoldColors: JchuPurchaseScaffoldColors = JchuPurchaseScaffoldColors()
    ) {
        self.tabColors = tabColors
        self.searchConfig = searchConfig
        self.scaffoldColors = scaffoldColors
    }
}

/// Colors used by ``JchuDetailsScaffold``.
public struct JchuDetailsScaffoldConfig {
    public var scaffoldColors: JchuPurchaseScaffoldColors

    public init(
        scaffoldColors: JchuPurchaseScaffoldColors = JchuPurchaseScaffoldColors()
    ) {
        self.scaffoldColors = scaffoldColors
    }
}

@MainActor
public extension JchuScreenColorTheme {
    func toPurchaseScaffoldConfig(
        query: Binding<String> = .constant(""),
        labelSearch: LocalizedStringKey = "Search",
        isSearchActive: Bool = true,
        headerConfig: JchuPurchaseHeaderConfig = JchuPurchaseHeaderConfig()
    ) -> JchuPurchaseScaffoldConfig {
        JchuPurchaseScaffoldConfig(
            headerConfig: headerConfig,
            searchConfig: JchuPurchaseSearchConfig(
                isActive: isSearchActive,
                label: labelSearch,
                contentColor: secondary,
                containerColor: primary.opacity(0.18),
                query: query
            ),
            scaffoldColors: JchuPurchaseScaffoldColors(
                contentColor: primary,
                containerColor: secondary
            )
        )
    }

    func toPurchaseTabScaffoldConfig(
        query: Binding<String> = .constant(""),
        labelSearch: LocalizedStringKey = "Search",
        isSearchActive: Bool = true
    ) -> JchuPurchaseTabScaffoldConfig {
        JchuPurchaseTabScaffoldConfig(
            tabColors: JchuPurchaseTabColors(
                selectedContentColor: primary,
                unselectedContentColor: primary.opacity(0.58),
                containerColor: primary.opacity(0.16),
                selectedContainerColor: secondary.opacity(0.55),
                contentColor: secondary.opacity(0.3)
            ),
            searchConfig: JchuPurchaseSearchConfig(
                isActive: isSearchActive,
                label: labelSearch,
                contentColor: secondary,
                containerColor: primary.opacity(0.18),
                query: query
            ),
            scaffoldColors: JchuPurchaseScaffoldColors(
                contentColor: primary,
                containerColor: secondary
            )
        )
    }

    func toDetailsScaffoldConfig() -> JchuDetailsScaffoldConfig {
        JchuDetailsScaffoldConfig(
            scaffoldColors: JchuPurchaseScaffoldColors(
                contentColor: primary,
                containerColor: secondary
            )
        )
    }
}
