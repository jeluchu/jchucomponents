import JchuComponentsCore
import JchuComponentsExtensions
import JchuComponentsSwiftUI
import SwiftUI

enum JchuCatalogCategory: String, CaseIterable, Identifiable {
    case buttons = "Buttons"
    case cards = "Cards"
    case chips = "Chips"
    case inputs = "Inputs"
    case extensions = "Extensions"
    case images = "Images"
    case lists = "Lists"
    case loaders = "Loaders"
    case progress = "Progress"
    case dividers = "Dividers"
    case toolbars = "Toolbars"
    case scaffolds = "Scaffolds"
    case pay = "Pay"
    case theme = "Theme"
    case info = "Info"

    var id: String { rawValue }

    var systemImage: String {
        switch self {
        case .buttons:
            "button.programmable"
        case .cards:
            "rectangle.stack"
        case .chips:
            "tag"
        case .inputs:
            "text.cursor"
        case .extensions:
            "curlybraces"
        case .images:
            "photo"
        case .lists:
            "square.grid.2x2"
        case .loaders:
            "arrow.triangle.2.circlepath"
        case .progress:
            "chart.bar"
        case .dividers:
            "minus"
        case .toolbars:
            "menubar.rectangle"
        case .scaffolds:
            "iphone"
        case .pay:
            "creditcard"
        case .theme:
            "paintpalette"
        case .info:
            "info.circle"
        }
    }
}

enum JchuCatalogFixtureKind: String {
    case enabled
    case disabled
    case loading
    case error
    case longContent
}

struct JchuCatalogStateFixture<State> {
    let name: String
    let kind: JchuCatalogFixtureKind
    let state: State
}

struct JchuCatalogMenuOption: Identifiable, Hashable {
    let id: String
    let name: String
    let systemImage: String
}

struct JchuCatalogModuleSection: Identifiable {
    let id: String
    let title: String
    let subtitle: String
    let options: [JchuCatalogMenuOption]
}

struct JchuCatalogFloatingButtonFixture: Identifiable {
    let name: String
    let kind: JchuCatalogFixtureKind
    let size: CGFloat
    let isEnabled: Bool
    let contentDescription: String

    var id: String { name }
}

struct JchuCatalogChipFixture: Identifiable {
    let name: String
    let kind: JchuCatalogFixtureKind
    let label: String
    let contentDescription: String

    var id: String { name }
}

struct JchuCatalogLoaderFixture: Identifiable {
    let name: String
    let kind: JchuCatalogFixtureKind
    let contentDescription: String

    var id: String { name }
}

struct JchuCatalogInputFixture: Identifiable {
    let name: String
    let kind: JchuCatalogFixtureKind
    let label: String
    let contentDescription: String
    let maxLength: Int?

    var id: String { name }
}

struct JchuCatalogExtensionFixture: Identifiable {
    let group: String
    let name: String
    let value: String

    var id: String {
        "\(group)-\(name)"
    }
}

struct JchuCatalogSampleItem: Identifiable, Hashable {
    let id: String
    let title: String
    let subtitle: String
    let systemImage: String
}

enum JchuCatalogScenario: String, CaseIterable, Identifiable {
    case light = "Light"
    case dark = "Dark"
    case accessibility = "Accessibility"

    var id: String { rawValue }

    var theme: JchuTheme {
        switch self {
        case .light:
            return JchuTheme(
                colors: JchuColors(
                    background: Color(red: 0.66, green: 0.82, blue: 0.71),
                    surface: Color(red: 0.58, green: 0.78, blue: 0.67),
                    primary: Color(red: 0.66, green: 0.82, blue: 0.71),
                    content: Color(red: 0.20, green: 0.20, blue: 0.20),
                    contentSecondary: Color(red: 0.30, green: 0.49, blue: 0.39),
                    error: Color(red: 0.70, green: 0.15, blue: 0.12)
                )
            )
        case .dark:
            return JchuTheme(
                colors: JchuColors(
                    background: Color(red: 0.06, green: 0.08, blue: 0.07),
                    surface: Color(red: 0.11, green: 0.13, blue: 0.12),
                    primary: Color(red: 0.84, green: 0.95, blue: 0.87),
                    content: Color(red: 0.95, green: 0.97, blue: 0.94),
                    contentSecondary: Color(red: 0.56, green: 0.84, blue: 0.64),
                    error: Color(red: 1, green: 0.71, blue: 0.67)
                )
            )
        case .accessibility:
            return JchuTheme(
                colors: JchuColors(
                    background: .white,
                    surface: .white,
                    primary: .black,
                    content: .black,
                    contentSecondary: .black,
                    error: Color(red: 0.55, green: 0.11, blue: 0.09)
                ),
                spacing: JchuSpacing(dimen16: 20, dimen24: 30, dimen32: 40),
                shapes: JchuShapes(corner16: 12)
            )
        }
    }

    var colorScheme: ColorScheme {
        self == .dark ? .dark : .light
    }

    var dynamicTypeSize: DynamicTypeSize {
        self == .accessibility ? .accessibility2 : .large
    }
}

@MainActor
enum JchuCatalogFixtures {
    static let longContent =
        "JchuComponents catalog fixture with enough content to validate wrapping and scaling."

    static let scenarios = JchuCatalogScenario.allCases

    static let uiMenu = [
        JchuCatalogMenuOption(id: "buttons", name: JchuCatalogCategory.buttons.rawValue, systemImage: JchuCatalogCategory.buttons.systemImage),
        JchuCatalogMenuOption(id: "cards", name: JchuCatalogCategory.cards.rawValue, systemImage: JchuCatalogCategory.cards.systemImage),
        JchuCatalogMenuOption(id: "chips", name: JchuCatalogCategory.chips.rawValue, systemImage: JchuCatalogCategory.chips.systemImage),
        JchuCatalogMenuOption(id: "inputs", name: JchuCatalogCategory.inputs.rawValue, systemImage: JchuCatalogCategory.inputs.systemImage),
        JchuCatalogMenuOption(id: "lists", name: JchuCatalogCategory.lists.rawValue, systemImage: JchuCatalogCategory.lists.systemImage),
        JchuCatalogMenuOption(id: "loaders", name: JchuCatalogCategory.loaders.rawValue, systemImage: JchuCatalogCategory.loaders.systemImage),
        JchuCatalogMenuOption(id: "progress", name: JchuCatalogCategory.progress.rawValue, systemImage: JchuCatalogCategory.progress.systemImage),
        JchuCatalogMenuOption(id: "dividers", name: JchuCatalogCategory.dividers.rawValue, systemImage: JchuCatalogCategory.dividers.systemImage),
        JchuCatalogMenuOption(id: "toolbars", name: JchuCatalogCategory.toolbars.rawValue, systemImage: JchuCatalogCategory.toolbars.systemImage),
        JchuCatalogMenuOption(id: "scaffolds", name: JchuCatalogCategory.scaffolds.rawValue, systemImage: JchuCatalogCategory.scaffolds.systemImage),
        JchuCatalogMenuOption(id: "images", name: JchuCatalogCategory.images.rawValue, systemImage: JchuCatalogCategory.images.systemImage),
        JchuCatalogMenuOption(id: "extensions", name: JchuCatalogCategory.extensions.rawValue, systemImage: JchuCatalogCategory.extensions.systemImage),
        JchuCatalogMenuOption(id: "pay", name: JchuCatalogCategory.pay.rawValue, systemImage: JchuCatalogCategory.pay.systemImage),
        JchuCatalogMenuOption(id: "themeTokens", name: JchuCatalogCategory.theme.rawValue, systemImage: JchuCatalogCategory.theme.systemImage),
        JchuCatalogMenuOption(id: "info", name: JchuCatalogCategory.info.rawValue, systemImage: JchuCatalogCategory.info.systemImage),
    ]

    static let moduleSections = [
        JchuCatalogModuleSection(
            id: "swiftuiComponents",
            title: "UI Components",
            subtitle: "Reusable SwiftUI controls, inputs, feedback and layout basics.",
            options: [
                JchuCatalogMenuOption(id: "buttons", name: JchuCatalogCategory.buttons.rawValue, systemImage: JchuCatalogCategory.buttons.systemImage),
                JchuCatalogMenuOption(id: "cards", name: JchuCatalogCategory.cards.rawValue, systemImage: JchuCatalogCategory.cards.systemImage),
                JchuCatalogMenuOption(id: "chips", name: JchuCatalogCategory.chips.rawValue, systemImage: JchuCatalogCategory.chips.systemImage),
                JchuCatalogMenuOption(id: "inputs", name: JchuCatalogCategory.inputs.rawValue, systemImage: JchuCatalogCategory.inputs.systemImage),
                JchuCatalogMenuOption(id: "lists", name: JchuCatalogCategory.lists.rawValue, systemImage: JchuCatalogCategory.lists.systemImage),
                JchuCatalogMenuOption(id: "loaders", name: JchuCatalogCategory.loaders.rawValue, systemImage: JchuCatalogCategory.loaders.systemImage),
                JchuCatalogMenuOption(id: "progress", name: JchuCatalogCategory.progress.rawValue, systemImage: JchuCatalogCategory.progress.systemImage),
                JchuCatalogMenuOption(id: "dividers", name: JchuCatalogCategory.dividers.rawValue, systemImage: JchuCatalogCategory.dividers.systemImage),
                JchuCatalogMenuOption(id: "toolbars", name: JchuCatalogCategory.toolbars.rawValue, systemImage: JchuCatalogCategory.toolbars.systemImage),
            ]
        ),
        JchuCatalogModuleSection(
            id: "swiftuiScreens",
            title: "SwiftUI Screens",
            subtitle: "Scaffolds, remote states, purchase layouts, images and app theme tokens.",
            options: [
                JchuCatalogMenuOption(id: "scaffolds", name: JchuCatalogCategory.scaffolds.rawValue, systemImage: JchuCatalogCategory.scaffolds.systemImage),
                JchuCatalogMenuOption(id: "images", name: JchuCatalogCategory.images.rawValue, systemImage: JchuCatalogCategory.images.systemImage),
                JchuCatalogMenuOption(id: "themeTokens", name: JchuCatalogCategory.theme.rawValue, systemImage: JchuCatalogCategory.theme.systemImage),
            ]
        ),
        JchuCatalogModuleSection(
            id: "extensions",
            title: "Extensions",
            subtitle: "Foundation, collections, dates, concurrency, URL, UIImage and View helpers.",
            options: [
                JchuCatalogMenuOption(id: "extensions", name: JchuCatalogCategory.extensions.rawValue, systemImage: JchuCatalogCategory.extensions.systemImage),
            ]
        ),
        JchuCatalogModuleSection(
            id: "pay",
            title: "Pay",
            subtitle: "Subscription models, billing empty states and RevenueCat flow surface.",
            options: [
                JchuCatalogMenuOption(id: "pay", name: JchuCatalogCategory.pay.rawValue, systemImage: JchuCatalogCategory.pay.systemImage),
            ]
        ),
        JchuCatalogModuleSection(
            id: "core",
            title: "Core",
            subtitle: "Package metadata and shared binary information exposed to Swift.",
            options: [
                JchuCatalogMenuOption(id: "info", name: JchuCatalogCategory.info.rawValue, systemImage: JchuCatalogCategory.info.systemImage),
            ]
        ),
    ]

    static let buttonsMenu = [
        JchuCatalogMenuOption(id: "progressButtons", name: "ProgressButtons", systemImage: "rectangle.and.hand.point.up.left"),
        JchuCatalogMenuOption(id: "floatingButtons", name: "FloatingButtons", systemImage: "plus.circle.fill"),
    ]

    static let progressMenu = [
        JchuCatalogMenuOption(id: "circularProgress", name: "CircularProgress", systemImage: "circle.dotted"),
        JchuCatalogMenuOption(id: "linearProgress", name: "LinearProgress", systemImage: "chart.bar.xaxis"),
        JchuCatalogMenuOption(id: "iconProgress", name: "IconProgress", systemImage: "icloud.and.arrow.down"),
    ]

    static let listsMenu = [
        JchuCatalogMenuOption(id: "lazyStaticGrids", name: "LazyStaticGrids", systemImage: "square.grid.3x3")
    ]

    static let toolbarsMenu = [
        JchuCatalogMenuOption(id: "simpleToolbars", name: "Toolbars", systemImage: "rectangle.topthird.inset.filled"),
        JchuCatalogMenuOption(id: "centerToolbars", name: "CenterToolbars", systemImage: "align.horizontal.center"),
        JchuCatalogMenuOption(id: "largeToolbars", name: "LargeToolbars", systemImage: "textformat.size.larger"),
    ]

    static let cardsMenu = [
        JchuCatalogMenuOption(id: "benefitCards", name: "BenefitCards", systemImage: "sparkles")
    ]

    static let scaffoldsMenu = [
        JchuCatalogMenuOption(id: "basicScaffolds", name: "Base & state", systemImage: "iphone"),
        JchuCatalogMenuOption(id: "detailsScaffold", name: "DetailsScaffold", systemImage: "doc.text.magnifyingglass"),
        JchuCatalogMenuOption(id: "settingsScaffold", name: "SettingsScaffold", systemImage: "gearshape"),
        JchuCatalogMenuOption(id: "shareScaffold", name: "ShareScaffold", systemImage: "square.and.arrow.up"),
        JchuCatalogMenuOption(id: "remoteContent", name: "RemoteScreenContent", systemImage: "antenna.radiowaves.left.and.right"),
        JchuCatalogMenuOption(id: "purchaseGrid", name: "PurchaseElementsScaffold", systemImage: "square.grid.2x2"),
        JchuCatalogMenuOption(id: "purchaseTabs", name: "PurchaseTabItemsScaffold", systemImage: "rectangle.bottomthird.inset.filled"),
        JchuCatalogMenuOption(id: "purchaseTabBar", name: "PurchaseTabBar", systemImage: "rectangle.3.group"),
        JchuCatalogMenuOption(id: "purchaseStates", name: "PurchaseStates", systemImage: "checklist"),
    ]

    static let imagesMenu = [
        JchuCatalogMenuOption(id: "networkImage", name: "NetworkImage", systemImage: "photo"),
        JchuCatalogMenuOption(id: "networkImageStates", name: "Placeholders & errors", systemImage: "photo.badge.exclamationmark"),
        JchuCatalogMenuOption(id: "networkPrefetcher", name: "NetworkImagePrefetcher", systemImage: "tray.and.arrow.down"),
    ]

    static let extensionsMenu = [
        JchuCatalogMenuOption(id: "foundationExtensions", name: "Foundation & collections", systemImage: "curlybraces"),
        JchuCatalogMenuOption(id: "dateUtilities", name: "Date utilities", systemImage: "calendar"),
        JchuCatalogMenuOption(id: "concurrencyExtensions", name: "Concurrency", systemImage: "bolt.horizontal"),
        JchuCatalogMenuOption(id: "jchuCompatible", name: "JchuCompatible", systemImage: "puzzlepiece.extension"),
        JchuCatalogMenuOption(id: "viewExtensions", name: "View extensions", systemImage: "rectangle.on.rectangle"),
        JchuCatalogMenuOption(id: "imageExtensions", name: "UIImage extensions", systemImage: "photo.on.rectangle"),
        JchuCatalogMenuOption(id: "urlImageExtensions", name: "URL image extensions", systemImage: "link"),
    ]

    static let progressButtonStateFixtures = [
        JchuCatalogStateFixture(
            name: "Enabled",
            kind: .enabled,
            state: JchuProgressButtonState(title: "Normal", isLoading: false, isEnabled: true)
        ),
        JchuCatalogStateFixture(
            name: "Loading",
            kind: .loading,
            state: JchuProgressButtonState(title: "Loading", isLoading: true, isEnabled: true)
        ),
        JchuCatalogStateFixture(
            name: "Disabled",
            kind: .disabled,
            state: JchuProgressButtonState(title: "Disabled", isLoading: false, isEnabled: false)
        ),
        JchuCatalogStateFixture(
            name: "Error",
            kind: .error,
            state: JchuProgressButtonState(title: "Error", isLoading: false, isEnabled: true)
        ),
        JchuCatalogStateFixture(
            name: "Long content",
            kind: .longContent,
            state: JchuProgressButtonState(title: longContent, isLoading: false, isEnabled: true)
        ),
    ]

    static let progressButtonStates = progressButtonStateFixtures.map(\.state)

    static let floatingButtonFixtures = [
        JchuCatalogFloatingButtonFixture(name: "Large", kind: .enabled, size: 64, isEnabled: true, contentDescription: "Large floating button"),
        JchuCatalogFloatingButtonFixture(name: "Medium", kind: .enabled, size: 52, isEnabled: true, contentDescription: "Medium floating button"),
        JchuCatalogFloatingButtonFixture(name: "Small", kind: .enabled, size: 42, isEnabled: true, contentDescription: "Small floating button"),
        JchuCatalogFloatingButtonFixture(name: "Disabled", kind: .disabled, size: 64, isEnabled: false, contentDescription: "Disabled floating button"),
    ]

    static let chipFixtures = [
        JchuCatalogChipFixture(name: "Default", kind: .enabled, label: "Default", contentDescription: "Default chip"),
        JchuCatalogChipFixture(name: "Clickable", kind: .enabled, label: "Clickable", contentDescription: "Clickable chip"),
        JchuCatalogChipFixture(name: "Selected", kind: .enabled, label: "Selected", contentDescription: "Selected chip"),
        JchuCatalogChipFixture(name: "Removable", kind: .enabled, label: "Remove me", contentDescription: "Remove chip"),
        JchuCatalogChipFixture(name: "Long content", kind: .longContent, label: longContent, contentDescription: "Long content chip"),
    ]

    static let tagChipFixtures = [
        JchuCatalogChipFixture(name: "Tag", kind: .enabled, label: "iOS", contentDescription: "Tag chip"),
        JchuCatalogChipFixture(name: "Tag with container", kind: .enabled, label: "SwiftUI", contentDescription: "Tag chip with container"),
    ]

    static let youtubeChipFixtures = [
        JchuCatalogChipFixture(name: "Selected", kind: .enabled, label: "Selected", contentDescription: "Selected YouTube chip"),
        JchuCatalogChipFixture(name: "Unselected", kind: .disabled, label: "Unselected", contentDescription: "Unselected YouTube chip"),
    ]

    static let loaderFixtures = [
        JchuCatalogLoaderFixture(name: "Circular", kind: .loading, contentDescription: "Circular loading indicator"),
        JchuCatalogLoaderFixture(name: "Compact", kind: .loading, contentDescription: "Compact loading indicator"),
        JchuCatalogLoaderFixture(name: "Full width", kind: .loading, contentDescription: "Full width loading indicator"),
    ]

    static let inputFixtures = [
        JchuCatalogInputFixture(name: "Search", kind: .enabled, label: "Search components", contentDescription: "Search the component catalog", maxLength: nil),
        JchuCatalogInputFixture(name: "Counted", kind: .enabled, label: "Component name", contentDescription: "Component name with character limit", maxLength: 40),
        JchuCatalogInputFixture(name: "Expandable search", kind: .enabled, label: "Search the catalog", contentDescription: "Expandable search field", maxLength: nil),
        JchuCatalogInputFixture(name: "Growing text field", kind: .enabled, label: "Component notes", contentDescription: "Multiline field with character limit", maxLength: 120),
        JchuCatalogInputFixture(name: "Long content", kind: .longContent, label: longContent, contentDescription: "Long input label fixture", maxLength: nil),
    ]

    static let imageURL = URL(string: "https://picsum.photos/id/1025/600/400")

    static let sampleItems = [
        JchuCatalogSampleItem(id: "buttons", title: "Buttons", subtitle: "Progress and floating states", systemImage: "button.programmable"),
        JchuCatalogSampleItem(id: "inputs", title: "Inputs", subtitle: "Search and growing text", systemImage: "text.cursor"),
        JchuCatalogSampleItem(id: "progress", title: "Progress", subtitle: "Linear, circular and icon", systemImage: "chart.bar"),
        JchuCatalogSampleItem(id: "images", title: "Images", subtitle: "Network rendering states", systemImage: "photo"),
    ]

    static let extensionFixtures = [
        JchuCatalogExtensionFixture(
            group: "String",
            name: "String.grouped(every:)",
            value: "123456789".grouped(every: 3)
        ),
        JchuCatalogExtensionFixture(
            group: "String",
            name: "String.onlyDigits",
            value: "A1 B2-C3".onlyDigits
        ),
        JchuCatalogExtensionFixture(
            group: "String",
            name: "String.formatInGroups(groupSize:separator:)",
            value: "ABCDEFGHIJKL".formatInGroups(groupSize: 4, separator: "-")
        ),
        JchuCatalogExtensionFixture(
            group: "String",
            name: "String.truncated(to:)",
            value: "JchuComponentsSwiftUI".truncated(to: 8)
        ),
        JchuCatalogExtensionFixture(
            group: "String",
            name: "String.removingDiacritics",
            value: "áéíóú ñ".removingDiacritics
        ),
        JchuCatalogExtensionFixture(
            group: "String",
            name: "String.isValidEmail",
            value: String("hello@jchu.dev".isValidEmail)
        ),
        JchuCatalogExtensionFixture(
            group: "Bool",
            name: "Bool?.orFalse()",
            value: String(Optional<Bool>.none.orFalse())
        ),
        JchuCatalogExtensionFixture(
            group: "Numeric",
            name: "Int.thousandsFormatted",
            value: 1234567.thousandsFormatted
        ),
        JchuCatalogExtensionFixture(
            group: "Numeric",
            name: "Int.millisecondsToTimer",
            value: 125000.millisecondsToTimer
        ),
        JchuCatalogExtensionFixture(
            group: "Numeric",
            name: "Int.roundedUpToNearestTen",
            value: String(126.roundedUpToNearestTen)
        ),
        JchuCatalogExtensionFixture(
            group: "Numeric",
            name: "Int64.bytesToMegabytes",
            value: Int64(5_242_880).bytesToMegabytes
        ),
        JchuCatalogExtensionFixture(
            group: "Collection",
            name: "Array.concatenateLowercase()",
            value: ["Jchu", "Components", "iOS"].concatenateLowercase()
        ),
        JchuCatalogExtensionFixture(
            group: "Collection",
            name: "Sequence.chunked(into:)",
            value: [1, 2, 3, 4, 5].chunked(into: 2).map { $0.map(String.init).joined(separator: ",") }.joined(separator: " | ")
        ),
        JchuCatalogExtensionFixture(
            group: "Date",
            name: "Date.formatWithTime()",
            value: sampleDate.formatWithTime(locale: catalogLocale, timeZone: catalogTimeZone)
        ),
        JchuCatalogExtensionFixture(
            group: "Date",
            name: "Date.firstDayOfTheMonth()",
            value: sampleDate.firstDayOfTheMonth(calendar: catalogCalendar)
                .format(locale: catalogLocale, timeZone: catalogTimeZone)
        ),
        JchuCatalogExtensionFixture(
            group: "Date",
            name: "Date.plusDays(3)",
            value: sampleDate.plusDays(3, calendar: catalogCalendar)
                .format(locale: catalogLocale, timeZone: catalogTimeZone)
        ),
        JchuCatalogExtensionFixture(
            group: "Date",
            name: "Int.durationText",
            value: 3665.durationText
        ),
        JchuCatalogExtensionFixture(
            group: "Data",
            name: "Data.detectedImageMIMEType",
            value: samplePNGHeader.detectedImageMIMEType ?? "nil"
        ),
        JchuCatalogExtensionFixture(
            group: "Data",
            name: "Data.imageFileName(prefix:date:)",
            value: samplePNGHeader.imageFileName(prefix: "catalog", date: sampleDate) ?? "nil"
        ),
        JchuCatalogExtensionFixture(
            group: "Codable",
            name: "Encodable.toJson()",
            value: SampleCodable(name: "Catalog", count: 3).toJson() ?? "nil"
        ),
        JchuCatalogExtensionFixture(
            group: "Codable",
            name: "String.fromJson(_:)",
            value: "{\"name\":\"Catalog\",\"count\":3}".fromJson(SampleCodable.self)?.name ?? "nil"
        ),
    ]

    private static let catalogLocale = Locale(identifier: "en_US_POSIX")

    private static let catalogTimeZone = TimeZone(secondsFromGMT: 0)!

    private static var catalogCalendar: Calendar {
        var calendar = Calendar(identifier: .gregorian)
        calendar.locale = catalogLocale
        calendar.timeZone = catalogTimeZone
        return calendar
    }

    private static let sampleDate = Date(timeIntervalSince1970: 1_720_126_920)

    private static let samplePNGHeader = Data([0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A])

    private struct SampleCodable: Codable {
        let name: String
        let count: Int
    }

    static let progressStateFixtures = [
        JchuCatalogStateFixture(
            name: "Enabled",
            kind: .enabled,
            state: progressState(title: "Linear", value: 40)
        ),
        JchuCatalogStateFixture(
            name: "Disabled",
            kind: .disabled,
            state: progressState(title: "Disabled", value: 40, isEnabled: false)
        ),
        JchuCatalogStateFixture(
            name: "Loading",
            kind: .loading,
            state: progressState(title: "Indeterminate", value: 0, isIndeterminate: true)
        ),
        JchuCatalogStateFixture(
            name: "Error",
            kind: .error,
            state: progressState(title: "Error", value: 0, isEnabled: false)
        ),
        JchuCatalogStateFixture(
            name: "Long content",
            kind: .longContent,
            state: progressState(title: longContent, value: 65)
        ),
    ]

    static func progressState(
        title: String,
        value: Double,
        isEnabled: Bool = true,
        isIndeterminate: Bool = false
    ) -> JchuProgressState {
        JchuProgressState(
            title: title,
            value: value,
            maxValue: 100,
            isEnabled: isEnabled,
            isIndeterminate: isIndeterminate
        )
    }
}
