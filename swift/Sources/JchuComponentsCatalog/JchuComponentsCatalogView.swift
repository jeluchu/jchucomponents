import JchuComponentsCore
import JchuComponentsExtensions
import JchuComponentsPay
import JchuComponentsSwiftUI
import SwiftUI
import UIKit

public struct JchuComponentsCatalogView: View {
    @State private var scenario: JchuCatalogScenario = .light

    public init() {}

    public var body: some View {
        CatalogRootView(scenario: $scenario)
            .jchuTheme(scenario.theme)
            .tint(scenario.theme.colors.contentSecondary)
            .preferredColorScheme(scenario.colorScheme)
            .dynamicTypeSize(scenario.dynamicTypeSize)
    }
}

private struct CatalogRootView: View {
    @Environment(\.jchuTheme) private var theme
    @Binding var scenario: JchuCatalogScenario
    @State private var selectedDestination: JchuCatalogDestination?

    var body: some View {
        NavigationStack {
            ScrollView {
                CatalogHomeHeader(scenario: $scenario)

                VStack(spacing: 0) {
                    ForEach(JchuCatalogFixtures.moduleSections) { section in
                        CatalogModuleHeader(section: section)

                        VStack(spacing: 18) {
                            ForEach(section.options) { option in
                                Button {
                                    selectedDestination = .route(option.id)
                                } label: {
                                    CatalogHomeCard(
                                        option: option,
                                        subtitle: capabilityDescription(for: option.id)
                                    )
                                }
                                .buttonStyle(.plain)
                            }
                        }
                        .padding(.horizontal, 16)
                        .padding(.top, 22)
                        .padding(.bottom, 28)
                    }
                }
            }
            .background(theme.colors.background.ignoresSafeArea())
            .foregroundStyle(theme.colors.content)
            .toolbar(.hidden, for: .navigationBar)
            .navigationDestination(item: $selectedDestination) { destination in
                CatalogDestinationView(destination: destination)
            }
        }
    }

    private func capabilityDescription(for route: String) -> String {
        switch route {
        case "buttons":
            "Progress buttons, loading states and floating actions."
        case "cards":
            "Benefit cards and reusable content surfaces."
        case "chips":
            "Default, tag, selected and removable chip states."
        case "inputs":
            "Search, counted text and growing multiline fields."
        case "preferences":
            "Action, switch and single-choice settings rows."
        case "text":
            "Expandable text with caller-owned disclosure state."
        case "lists":
            "Static grid layouts and repeated item composition."
        case "loaders":
            "Circular and compact loading indicators."
        case "progress":
            "Linear, circular and icon progress feedback."
        case "dividers":
            "Horizontal, inset and vertical separators."
        case "toolbars":
            "Top bars, centered titles and large toolbar variants."
        case "scaffolds":
            "Base, state, detail, settings, share and purchase screens."
        case "images":
            "Network images, placeholders, failures and prefetching."
        case "extensions":
            "Strings, numbers, dates, collections, concurrency and view helpers."
        case "pay":
            "Subscription types, billing info and RevenueCat integration flow."
        case "themeTokens":
            "Colors, spacing, shapes, motion and preset palettes."
        case "info":
            "Package version, covered modules and catalog parity notes."
        default:
            "Catalog examples and behavior checks."
        }
    }
}

private enum JchuCatalogDestination: Identifiable, Hashable {
    case route(String)

    var id: String {
        route
    }

    var route: String {
        switch self {
        case .route(let route):
            route
        }
    }
}

private struct CatalogDestinationView: View {
    let destination: JchuCatalogDestination
    @State private var selectedDestination: JchuCatalogDestination?

    var body: some View {
        routeView(destination.route)
            .navigationDestination(item: $selectedDestination) { destination in
                CatalogDestinationView(destination: destination)
            }
    }

    private func navigate(to route: String) {
        selectedDestination = .route(route)
    }

    @ViewBuilder
    private func menu(_ title: LocalizedStringKey, options: [JchuCatalogMenuOption]) -> some View {
        CatalogMenuScreen(title: title, options: options) { route in
            navigate(to: route)
        }
    }

    @ViewBuilder
    private func routeView(_ route: String) -> some View {
        switch route {
        case "buttons":
            menu("Buttons", options: JchuCatalogFixtures.buttonsMenu)
        case "progressButtons":
            ProgressButtonsCatalogScreen()
        case "floatingButtons":
            FloatingButtonsCatalogScreen()
        case "cards":
            menu("Cards", options: JchuCatalogFixtures.cardsMenu)
        case "benefitCards":
            BenefitsCatalogScreen()
        case "requirementsCards":
            RequirementsCardsCatalogScreen()
        case "chips":
            ChipsCatalogScreen()
        case "inputs":
            InputsCatalogScreen()
        case "preferences":
            PreferencesCatalogScreen()
        case "text":
            TextCatalogScreen()
        case "lists":
            menu("Lists", options: JchuCatalogFixtures.listsMenu)
        case "lazyStaticGrids":
            LazyStaticGridCatalogScreen()
        case "loaders":
            LoadersCatalogScreen()
        case "progress":
            menu("Progress", options: JchuCatalogFixtures.progressMenu)
        case "circularProgress":
            CircularProgressCatalogScreen()
        case "linearProgress":
            LinearProgressCatalogScreen()
        case "iconProgress":
            IconProgressCatalogScreen()
        case "dividers":
            DividersCatalogScreen()
        case "toolbars":
            menu("Toolbars", options: JchuCatalogFixtures.toolbarsMenu)
        case "simpleToolbars":
            ToolbarCatalogScreen(title: "Toolbars", alignment: .trailing)
        case "centerToolbars":
            ToolbarCatalogScreen(title: "CenterToolbars", alignment: .center)
        case "largeToolbars":
            LargeToolbarCatalogScreen()
        case "scaffolds":
            menu("Scaffolds", options: JchuCatalogFixtures.scaffoldsMenu)
        case "basicScaffolds":
            ScaffoldsCatalogScreen()
        case "detailsScaffold":
            DetailsScaffoldCatalogScreen()
        case "settingsScaffold":
            SettingsScaffoldCatalogScreen()
        case "shareScaffold":
            ShareScaffoldCatalogScreen()
        case "remoteContent":
            RemoteContentCatalogScreen()
        case "purchaseGrid":
            PurchaseGridCatalogScreen()
        case "purchaseTabs":
            PurchaseTabsCatalogScreen()
        case "purchaseTabBar":
            PurchaseTabBarCatalogScreen()
        case "purchaseStates":
            PurchaseStatesCatalogScreen()
        case "images":
            menu("Images", options: JchuCatalogFixtures.imagesMenu)
        case "networkImage":
            ImagesCatalogScreen()
        case "networkImageStates":
            NetworkImageStatesCatalogScreen()
        case "networkPrefetcher":
            NetworkPrefetcherCatalogScreen()
        case "extensions":
            menu("Extensions", options: JchuCatalogFixtures.extensionsMenu)
        case "foundationExtensions":
            ExtensionsCatalogScreen()
        case "dateUtilities":
            DateUtilitiesCatalogScreen()
        case "concurrencyExtensions":
            ConcurrencyCatalogScreen()
        case "jchuCompatible":
            JchuCompatibleCatalogScreen()
        case "viewExtensions":
            ViewExtensionsCatalogScreen()
        case "imageExtensions":
            UIImageExtensionsCatalogScreen()
        case "urlImageExtensions":
            URLImageExtensionsCatalogScreen()
        case "pay":
            PayCatalogScreen()
        case "themeTokens":
            ThemeTokensCatalogScreen()
        case "info":
            InfoCatalogScreen()
        default:
            CatalogPlaceholderScreen(title: route)
        }
    }
}

private struct CatalogHomeHeader: View {
    @Environment(\.jchuTheme) private var theme
    @Binding var scenario: JchuCatalogScenario

    var body: some View {
        VStack(alignment: .leading, spacing: 22) {
            HStack(spacing: 16) {
                ZStack {
                    Image(systemName: "swift")
                        .font(.system(size: 28, weight: .bold))
                        .foregroundStyle(theme.colors.content)
                }
                .frame(width: 42, height: 42)

                Text("Jchucomponents")
                    .font(.system(size: 30, weight: .bold, design: .rounded))
                    .foregroundStyle(theme.colors.content)
                    .lineLimit(1)
                    .minimumScaleFactor(0.72)
            }

            Picker("Scenario", selection: $scenario) {
                ForEach(JchuCatalogFixtures.scenarios) { scenario in
                    Text(scenario.rawValue).tag(scenario)
                }
            }
            .pickerStyle(.segmented)
            .labelsHidden()
        }
        .padding(.horizontal, 18)
        .padding(.top, 22)
        .padding(.bottom, 18)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(theme.colors.primary)
    }
}

private struct CatalogModuleHeader: View {
    @Environment(\.jchuTheme) private var theme
    let section: JchuCatalogModuleSection

    var body: some View {
        Text(section.title)
            .font(.system(size: 30, weight: .semibold, design: .rounded))
            .foregroundStyle(.white.opacity(0.92))
            .lineLimit(1)
            .minimumScaleFactor(0.76)
            .padding(.horizontal, 18)
            .padding(.vertical, 20)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(theme.colors.contentSecondary)
    }
}

private struct CatalogHomeCard: View {
    @Environment(\.jchuTheme) private var theme
    let option: JchuCatalogMenuOption
    let subtitle: String

    var body: some View {
        VStack(alignment: .leading, spacing: 7) {
            Text(option.name)
                .font(.system(size: 27, weight: .medium, design: .rounded))
                .foregroundStyle(theme.colors.contentSecondary)
                .lineLimit(1)
                .minimumScaleFactor(0.72)

            Text(subtitle)
                .font(.system(size: 13, weight: .medium, design: .rounded))
                .foregroundStyle(theme.colors.contentSecondary.opacity(0.82))
                .lineLimit(1)
                .fixedSize(horizontal: false, vertical: true)
        }
        .padding(.horizontal, 18)
        .frame(maxWidth: .infinity, minHeight: 78, alignment: .leading)
        .background(theme.colors.surface, in: .rect(cornerRadius: 14))
        .contentShape(.rect(cornerRadius: 14))
    }
}

private struct CatalogMenuScreen: View {
    let title: LocalizedStringKey
    let options: [JchuCatalogMenuOption]
    let navigate: (String) -> Void

    var body: some View {
        JchuScrollableScaffold(title) {
            VStack(spacing: 12) {
                ForEach(options) { option in
                    Button {
                        navigate(option.id)
                    } label: {
                        CatalogMenuCard(option: option)
                    }
                    .buttonStyle(.plain)
                }
            }
        }
    }
}

private struct ProgressButtonsCatalogScreen: View {
    @State private var interactiveLoading = false

    var body: some View {
        JchuScrollableScaffold("ProgressButtons") {
            CatalogSection("States") {
                ForEach(JchuCatalogFixtures.progressButtonStateFixtures, id: \.name) { fixture in
                    VStack(alignment: .leading, spacing: 8) {
                        CatalogLabel(fixture.name)
                        JchuProgressButton(state: fixture.state) {}
                    }
                }
            }

            CatalogSection("Interactive") {
                JchuProgressButton(
                    state: JchuProgressButtonState(
                        title: interactiveLoading ? "Loading" : "Interactive",
                        isLoading: interactiveLoading,
                        isEnabled: true
                    )
                ) {
                    interactiveLoading.toggle()
                }
            }
        }
    }
}

private struct FloatingButtonsCatalogScreen: View {
    var body: some View {
        JchuScrollableScaffold("FloatingButtons") {
            CatalogSection("Sizes") {
                HStack(alignment: .bottom, spacing: 18) {
                    ForEach(JchuCatalogFixtures.floatingButtonFixtures) { fixture in
                        VStack(spacing: 8) {
                            Button {} label: {
                                Image(systemName: "plus")
                                    .font(.system(size: fixture.size * 0.35, weight: .bold))
                                    .frame(width: fixture.size, height: fixture.size)
                            }
                            .buttonStyle(.borderedProminent)
                            .clipShape(Circle())
                            .disabled(!fixture.isEnabled)
                            .accessibilityLabel(Text(fixture.contentDescription))

                            CatalogLabel(fixture.name)
                        }
                    }
                }
            }
        }
    }
}

private struct ChipsCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme
    @State private var selected = "Selected"
    @State private var removableVisible = true

    var body: some View {
        JchuScrollableScaffold("Chips") {
            CatalogSection("Default") {
                FlowLayout(spacing: 8) {
                    ForEach(JchuCatalogFixtures.chipFixtures) { fixture in
                        if fixture.name != "Removable" || removableVisible {
                            JchuChip(
                                LocalizedStringKey(fixture.label),
                                isSelected: selected == fixture.name
                            ) {
                                if fixture.name == "Removable" {
                                    removableVisible = false
                                } else {
                                    selected = fixture.name
                                }
                            }
                            .accessibilityLabel(Text(fixture.contentDescription))
                        }
                    }
                }
            }

            CatalogSection("Tags") {
                FlowLayout(spacing: 8) {
                    ForEach(JchuCatalogFixtures.tagChipFixtures) { fixture in
                        JchuChip(LocalizedStringKey(fixture.label)) {}
                    }
                }
            }

            CatalogSection("Amount counters") {
                FlowLayout(spacing: 8) {
                    JchuAmountCounter(
                        amount: "x3",
                        colors: JchuAmountCounterColors(
                            contentColor: theme.colors.content,
                            containerColor: theme.colors.surface
                        ),
                        systemImageName: "star.fill"
                    )
                }
            }

            CatalogSection("YouTube") {
                FlowLayout(spacing: 8) {
                    ForEach(JchuCatalogFixtures.youtubeChipFixtures) { fixture in
                        JchuChip(
                            LocalizedStringKey(fixture.label),
                            isSelected: fixture.name == "Selected"
                        ) {}
                    }
                }
            }
        }
    }
}

private struct InputsCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme
    @State private var searchQuery = ""
    @State private var componentName = ""
    @State private var notes = ""
    @State private var longContent = ""
    @State private var selectedColor = "green"

    var body: some View {
        JchuScrollableScaffold("Inputs") {
            CatalogSection("Search") {
                JchuExpandableSearch(
                    query: $searchQuery,
                    defaults: SearchBarDefaults(
                        label: LocalizedStringKey(JchuCatalogFixtures.inputFixtures[0].label),
                        containerColor: theme.colors.surface,
                        contentColor: theme.colors.content
                    )
                )

                if !searchQuery.isEmpty {
                    Text("Query: \(searchQuery)")
                        .font(theme.typography.body)
                        .foregroundStyle(theme.colors.content)
                }
            }

            CatalogSection("Counted") {
                JchuGrowingTextField(
                    value: $componentName,
                    defaults: GrowingTextFieldDefaults(
                        label: "Component name",
                        placeholder: "Write a component",
                        systemImage: "text.cursor",
                        minLines: 1,
                        maxLines: 2,
                        maxCharacters: 40,
                        containerColor: theme.colors.surface,
                        contentColor: theme.colors.content
                    )
                )
            }

            CatalogSection("Growing text field") {
                JchuGrowingTextField(
                    value: $notes,
                    defaults: GrowingTextFieldDefaults(
                        label: "Component notes",
                        placeholder: "Write implementation notes",
                        systemImage: "note.text",
                        maxCharacters: 120,
                        containerColor: theme.colors.surface,
                        contentColor: theme.colors.content
                    )
                )
            }

            CatalogSection("Long content") {
                JchuGrowingTextField(
                    value: $longContent,
                    defaults: GrowingTextFieldDefaults(
                        label: LocalizedStringKey(JchuCatalogFixtures.longContent),
                        placeholder: "Long label resilience",
                        minLines: 2,
                        maxLines: 4,
                        containerColor: theme.colors.surface,
                        contentColor: theme.colors.content
                    )
                )
            }

            CatalogSection("Color picker") {
                JchuColorPicker(
                    options: [
                        JchuColorOption(value: "green", color: .green, accessibilityLabel: "Green"),
                        JchuColorOption(value: "blue", color: .blue, accessibilityLabel: "Blue"),
                        JchuColorOption(value: "orange", color: .orange, accessibilityLabel: "Orange"),
                        JchuColorOption(
                            value: "disabled",
                            color: .gray,
                            accessibilityLabel: "Unavailable",
                            isEnabled: false
                        )
                    ],
                    selection: $selectedColor
                )
            }

            CatalogSection("iNook color settings fidelity") {
                JchuINookColorPicker(
                    selection: $selectedColor,
                    options: [
                        JchuINookColorOption(colorAssetName: "green", color: .green),
                        JchuINookColorOption(colorAssetName: "blue", color: .blue),
                        JchuINookColorOption(colorAssetName: "orange", color: .orange),
                        JchuINookColorOption(colorAssetName: "premium", color: .purple, isPremium: true)
                    ],
                    isSubscribed: false,
                    isDarkTheme: false,
                    title: "Phone color",
                    description: "Choose the background color used by the phone."
                )
            }
        }
    }
}

private struct PreferencesCatalogScreen: View {
    @State private var previewsEnabled = true
    @State private var frequency = "Daily"

    var body: some View {
        JchuScrollableScaffold("Preferences") {
            CatalogSection("Action") {
                JchuPreferenceItem(
                    "Catalog updates",
                    description: "Open reusable preference settings",
                    systemImage: "bell"
                ) {}
            }

            CatalogSection("Switch") {
                JchuPreferenceSwitch(
                    "Enable previews",
                    description: "Caller-owned switch state",
                    systemImage: "sparkles",
                    isOn: $previewsEnabled
                )
                JchuPreferenceSwitch(
                    "Disabled preference",
                    isOn: .constant(false),
                    isEnabled: false
                )
            }

            CatalogSection("Single choice") {
                ForEach(["Daily", "Weekly"], id: \.self) { option in
                    JchuPreferenceChoice(
                        LocalizedStringKey(option),
                        isSelected: frequency == option
                    ) {
                        frequency = option
                    }
                }
            }

            CatalogSection("iNook preference fidelity") {
                JchuPreferenceToggle(
                    systemImageName: "bell",
                    title: "Enable notifications",
                    isActive: $previewsEnabled
                )
                JchuSettingsToggle(
                    title: "Enable previews",
                    description: "Original settings toggle layout",
                    isActive: $previewsEnabled
                )
            }
        }
    }
}

private struct TextCatalogScreen: View {
    @State private var isExpanded = false

    var body: some View {
        JchuScrollableScaffold("Text") {
            CatalogSection("Expandable text") {
                JchuExpandableText(
                    String(
                        repeating: "The caller owns expansion state and the component only renders text. ",
                        count: 6
                    ),
                    isExpanded: $isExpanded
                )
            }

            CatalogSection("iNook expandable-description fidelity") {
                JchuSimpleExpandableText(
                    description: String(
                        repeating: "This variant keeps the original internal state, gradient and disclosure behavior. ",
                        count: 5
                    ),
                    config: JchuExpandableDescriptionConfig(enableHapticFeedback: false)
                )
            }
        }
    }
}

private struct LoadersCatalogScreen: View {
    var body: some View {
        JchuScrollableScaffold("Loaders") {
            CatalogSection("States") {
                ForEach(JchuCatalogFixtures.loaderFixtures) { fixture in
                    VStack(spacing: 10) {
                        CatalogLabel(fixture.name)
                        JchuLoadingIndicator(label: LocalizedStringKey(fixture.name == "Compact" ? "" : "Loading"))
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, fixture.name == "Compact" ? 8 : 20)
                            .accessibilityLabel(Text(fixture.contentDescription))
                    }
                }
            }
        }
    }
}

private struct LinearProgressCatalogScreen: View {
    var body: some View {
        JchuScrollableScaffold("LinearProgress") {
            CatalogSection("States") {
                ForEach(JchuCatalogFixtures.progressStateFixtures, id: \.name) { fixture in
                    VStack(alignment: .leading, spacing: 8) {
                        CatalogLabel(fixture.name)
                        JchuLinearProgress(state: fixture.state)
                    }
                }
            }
        }
    }
}

private struct CircularProgressCatalogScreen: View {
    var body: some View {
        JchuScrollableScaffold("CircularProgress") {
            CatalogSection("States") {
                LazyVGrid(columns: [GridItem(.adaptive(minimum: 120), spacing: 16)], spacing: 18) {
                    ForEach(JchuCatalogFixtures.progressStateFixtures, id: \.name) { fixture in
                        VStack(spacing: 8) {
                            JchuCircularProgress(state: fixture.state)
                            CatalogLabel(fixture.name)
                        }
                    }
                }
            }
        }
    }
}

private struct IconProgressCatalogScreen: View {
    var body: some View {
        JchuScrollableScaffold("IconProgress") {
            CatalogSection("States") {
                ForEach(JchuCatalogFixtures.progressStateFixtures, id: \.name) { fixture in
                    JchuIconProgress(state: fixture.state, systemImage: "icloud.and.arrow.down")
                }
            }
        }
    }
}

private struct BenefitsCatalogScreen: View {
    var body: some View {
        JchuScrollableScaffold("BenefitCards") {
            CatalogSection("Benefits") {
                BenefitCard(title: "Accessible states", subtitle: "Enabled, disabled, loading, error and long-content fixtures.")
                BenefitCard(title: "Reusable SwiftUI", subtitle: "Catalog entries use the same public components exposed by the package.")
                BenefitCard(title: "Theme ready", subtitle: "Light, dark and accessibility scenarios can be reviewed from the dashboard.")
            }
        }
    }
}

private struct RequirementsCardsCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme

    var body: some View {
        let colors = JchuINookRequirementsCardColors(
            strokeColor: theme.colors.content.opacity(0.4),
            contentColor: theme.colors.content,
            containerColor: theme.colors.content.opacity(0.05),
            amountInfoColors: JchuINookAmountInfoColors(
                contentColor: theme.colors.content,
                containerColor: theme.colors.content.opacity(0.1),
                iconColor: theme.colors.content
            ),
            amountColors: JchuINookRequirementsAmountCounterColors(
                contentColor: theme.colors.content,
                containerColor: theme.colors.content.opacity(0.1),
                iconColor: theme.colors.content
            )
        )

        JchuScrollableScaffold("RequirementsCards") {
            CatalogSection("Text and marquee") {
                JchuINookRequirementsCardText(
                    title: "Lighting type",
                    requirement: "Fluorescent lighting requirement",
                    colors: colors
                )
            }
            CatalogSection("Amount information") {
                JchuINookRequirementsCardInfo(
                    title: "Appearance",
                    amount: "10%",
                    colors: colors,
                    dialogDefaults: JchuINookAmountInfoDialogDefaults(
                        title: "Appearance",
                        message: "Original alert interaction"
                    )
                )
            }
        }
    }
}

private struct LazyStaticGridCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme
    private let columns = [GridItem(.adaptive(minimum: 96), spacing: 12)]

    var body: some View {
        JchuScrollableScaffold("LazyStaticGrids") {
            CatalogSection("Static grid") {
                LazyVGrid(columns: columns, spacing: 12) {
                    ForEach(JchuCatalogFixtures.uiMenu.prefix(9)) { option in
                        VStack(spacing: 8) {
                            Image(systemName: option.systemImage)
                                .font(.title3)
                            Text(option.name)
                                .font(.caption)
                                .lineLimit(2)
                                .multilineTextAlignment(.center)
                        }
                        .foregroundStyle(theme.colors.content)
                        .frame(maxWidth: .infinity, minHeight: 88)
                        .padding(10)
                        .background(theme.colors.surface, in: .rect(cornerRadius: 8))
                    }
                }
            }
        }
    }
}

private struct DividersCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme

    var body: some View {
        JchuScrollableScaffold("Dividers") {
            CatalogSection("Default") {
                Text("Top content")
                Divider()
                Text("Bottom content")
            }

            CatalogSection("Inset") {
                VStack(alignment: .leading, spacing: 12) {
                    Text("Leading content")
                    Divider().padding(.leading, 36)
                    Text("Indented divider")
                }
            }

            CatalogSection("Vertical") {
                HStack {
                    Text("Left")
                    Divider().frame(height: 44)
                    Text("Right")
                }
            }
        }
        .foregroundStyle(theme.colors.content)
    }
}

private struct ToolbarCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme
    let title: LocalizedStringKey
    let alignment: HorizontalAlignment

    var body: some View {
        JchuScrollableScaffold(title) {
            CatalogSection("Preview") {
                JchuScaffoldTopBar(
                    title,
                    backgroundColor: theme.colors.surface,
                    foregroundColor: theme.colors.content,
                    titleAlignment: alignment,
                    leading: {
                        JchuScaffoldBackButton()
                    },
                    trailing: {
                        Image(systemName: "ellipsis")
                    }
                )
                .clipShape(.rect(cornerRadius: 8))
            }
        }
    }
}

private struct LargeToolbarCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme

    var body: some View {
        JchuScrollableScaffold("LargeToolbars") {
            CatalogSection("Preview") {
                VStack(alignment: .leading, spacing: 8) {
                    Text("LargeToolbars")
                        .font(.largeTitle.bold())
                    Text(JchuCatalogFixtures.longContent)
                        .font(.body)
                        .foregroundStyle(theme.colors.contentSecondary)
                }
                .foregroundStyle(theme.colors.content)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding()
                .background(theme.colors.surface, in: .rect(cornerRadius: 8))
            }
        }
    }
}

private struct ScaffoldsCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme
    @State private var selectedID = "home"
    @State private var loading = false
    @State private var empty = false

    var body: some View {
        JchuScrollableScaffold("Scaffolds") {
            CatalogSection("Top bar") {
                JchuScaffoldTopBar("Details", titleAlignment: .center)
                    .background(theme.colors.surface, in: .rect(cornerRadius: 8))
            }

            CatalogSection("Bottom bar") {
                JchuScaffoldBottomBar(
                    items: [
                        JchuScaffoldBottomBarItem(id: "home", systemImage: "house", label: "Home") {},
                        JchuScaffoldBottomBarItem(id: "search", systemImage: "magnifyingglass", label: "Search") {},
                        JchuScaffoldBottomBarItem(id: "settings", systemImage: "gearshape", label: "Settings") {},
                    ],
                    selectedID: $selectedID
                )
                .clipShape(.rect(cornerRadius: 8))
            }

            CatalogSection("States") {
                Toggle("Loading", isOn: $loading)
                Toggle("Empty", isOn: $empty)
                JchuStatePreview(isLoading: loading, isEmpty: empty)
            }
        }
        .foregroundStyle(theme.colors.content)
    }
}

private struct DetailsScaffoldCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme

    private var config: JchuDetailsScaffoldConfig {
        JchuDetailsScaffoldConfig(
            scaffoldColors: JchuPurchaseScaffoldColors(
                contentColor: theme.colors.content,
                containerColor: theme.colors.background
            )
        )
    }

    var body: some View {
        JchuDetailsScaffold(
            "DetailsScaffold",
            details: JchuCatalogFixtures.sampleItems.first,
            config: config
        ) { item in
            VStack(alignment: .leading, spacing: 12) {
                Image(systemName: item.systemImage)
                    .font(.largeTitle)
                    .foregroundStyle(theme.colors.contentSecondary)

                Text(item.title)
                    .font(theme.typography.title)

                Text(item.subtitle)
                    .font(theme.typography.body)
                    .foregroundStyle(theme.colors.contentSecondary)

                BenefitCard(title: "State aware", subtitle: "The same scaffold can render loading, empty and content states.")
            }
        }
    }
}

private struct SettingsScaffoldCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme
    @State private var notifications = true
    @State private var analytics = false

    var body: some View {
        JchuSettingsScaffold(
            "SettingsScaffold",
            backgroundColor: theme.colors.background,
            contentColor: theme.colors.content,
            surfaceColor: theme.colors.surface
        ) {
            VStack(alignment: .leading, spacing: 16) {
                Toggle("Notifications", isOn: $notifications)
                Toggle("Anonymous analytics", isOn: $analytics)

                Divider()

                Label("Version \(JchuComponentsInfo.version)", systemImage: "info.circle")
                    .font(theme.typography.body)
            }
            .foregroundStyle(theme.colors.content)
        }
    }
}

private struct ShareScaffoldCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme

    private var config: JchuShareScaffoldConfig {
        JchuShareScaffoldConfig(
            shareBarColors: JchuShareBarColors(
                containerColor: theme.colors.surface,
                shareContentColor: theme.colors.background,
                shareContainerColor: theme.colors.contentSecondary,
                downloadContentColor: theme.colors.contentSecondary,
                downloadContainerColor: theme.colors.contentSecondary
            ),
            scaffoldColors: JchuPurchaseScaffoldColors(
                contentColor: theme.colors.content,
                containerColor: theme.colors.background
            )
        )
    }

    var body: some View {
        JchuShareScaffold(
            "ShareScaffold",
            config: config,
            onShare: {},
            onDownload: {}
        ) {
            VStack(alignment: .leading, spacing: 12) {
                Text("Share-ready content")
                    .font(theme.typography.title)
                Text("Bottom actions are provided by JchuShareBottomBar and themed from the catalog palette.")
                    .font(theme.typography.body)
                    .foregroundStyle(theme.colors.contentSecondary)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
            .padding(16)
        }
    }
}

private struct RemoteContentCatalogScreen: View {
    @State private var mode = "success"

    var body: some View {
        JchuScrollableScaffold("RemoteScreenContent") {
            CatalogSection("State") {
                Picker("Mode", selection: $mode) {
                    Text("Success").tag("success")
                    Text("Loading").tag("loading")
                    Text("Error").tag("error")
                }
                .pickerStyle(.segmented)

                JchuRemoteScreenContent(
                    data: mode == "success" ? "Catalog payload" : nil,
                    isLoading: mode == "loading",
                    error: mode == "error" ? "Network unavailable" : nil,
                    loadingContent: {
                        JchuLoadingIndicator(label: "Loading")
                            .frame(maxWidth: .infinity, minHeight: 180)
                    },
                    successContent: { value in
                        BenefitCard(title: "Success", subtitle: value)
                            .frame(minHeight: 180)
                    },
                    failureContent: { error in
                        JchuDefaultEmptyState(
                            LocalizedStringKey(error ?? "Unable to load"),
                            systemImage: "exclamationmark.triangle"
                        )
                            .frame(maxWidth: .infinity, minHeight: 180)
                    }
                )
            }
        }
    }
}

private struct PurchaseGridCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme
    @State private var query = ""

    private var config: JchuPurchaseScaffoldConfig {
        JchuPurchaseScaffoldConfig(
            searchConfig: JchuPurchaseSearchConfig(
                label: "Search components",
                contentColor: theme.colors.content,
                containerColor: theme.colors.surface,
                query: $query
            ),
            scaffoldColors: JchuPurchaseScaffoldColors(
                contentColor: theme.colors.content,
                containerColor: theme.colors.background
            )
        )
    }

    var body: some View {
        JchuPurchaseElementsScaffold(
            "PurchaseElementsScaffold",
            cells: 2,
            items: JchuCatalogFixtures.sampleItems,
            isLoading: false,
            id: \.id,
            filtered: { items in
                query.isEmpty ? items : items.filter { $0.title.localizedCaseInsensitiveContains(query) }
            },
            config: config
        ) { _, item in
            SampleItemCard(item: item)
        }
    }
}

private struct PurchaseTabsCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme
    @State private var selectedTabID = "components"
    @State private var query = ""

    private let tabs = [
        JchuScaffoldTabItem(id: "components", title: "Components", systemImage: "square.grid.2x2"),
        JchuScaffoldTabItem(id: "states", title: "States", systemImage: "switch.2"),
        JchuScaffoldTabItem(id: "theme", title: "Theme", systemImage: "paintpalette"),
    ]

    private var config: JchuPurchaseTabScaffoldConfig {
        JchuPurchaseTabScaffoldConfig(
            tabColors: JchuPurchaseTabColors(
                selectedContentColor: theme.colors.content,
                unselectedContentColor: theme.colors.contentSecondary,
                containerColor: theme.colors.surface,
                selectedContainerColor: theme.colors.primary.opacity(0.55),
                contentColor: theme.colors.background.opacity(0.3)
            ),
            searchConfig: JchuPurchaseSearchConfig(
                label: "Search tab content",
                contentColor: theme.colors.content,
                containerColor: theme.colors.surface,
                query: $query
            ),
            scaffoldColors: JchuPurchaseScaffoldColors(
                contentColor: theme.colors.content,
                containerColor: theme.colors.background
            )
        )
    }

    var body: some View {
        JchuPurchaseTabItemsScaffold(
            "PurchaseTabItemsScaffold",
            tabs: tabs,
            selectedTabID: $selectedTabID,
            isLoading: false,
            config: config
        ) {
            VStack(alignment: .leading, spacing: 12) {
                Text(selectedTabID.capitalizingFirstLetter())
                    .font(theme.typography.title)
                Text("Query: \(query.nilIfBlank ?? "empty")")
                    .font(theme.typography.body)
                    .foregroundStyle(theme.colors.contentSecondary)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
            .padding(16)
            .foregroundStyle(theme.colors.content)
        }
    }
}

private struct PurchaseTabBarCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme
    @State private var selectedTabID = "one"

    private let tabs = [
        JchuScaffoldTabItem(id: "one", title: "One", systemImage: "1.circle"),
        JchuScaffoldTabItem(id: "two", title: "Two", systemImage: "2.circle"),
        JchuScaffoldTabItem(id: "three", title: "Three", systemImage: "3.circle"),
    ]

    var body: some View {
        JchuScrollableScaffold("PurchaseTabBar") {
            CatalogSection("Standalone") {
                JchuPurchaseTabBar(
                    tabs: tabs,
                    selectedID: $selectedTabID,
                    colors: JchuPurchaseTabColors(
                        selectedContentColor: theme.colors.content,
                        unselectedContentColor: theme.colors.contentSecondary,
                        containerColor: theme.colors.surface,
                        selectedContainerColor: theme.colors.primary.opacity(0.55),
                        contentColor: theme.colors.background.opacity(0.25)
                    )
                )
                .clipShape(.rect(cornerRadius: 8))

                CatalogValueRow(name: "Selected", value: selectedTabID)
            }
        }
    }
}

private struct PurchaseStatesCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme
    @State private var mode = "content"

    private var config: JchuPurchaseScaffoldConfig {
        JchuPurchaseScaffoldConfig(
            searchConfig: JchuPurchaseSearchConfig(isActive: false),
            scaffoldColors: JchuPurchaseScaffoldColors(
                contentColor: theme.colors.content,
                containerColor: theme.colors.background
            )
        )
    }

    var body: some View {
        JchuScrollableScaffold("PurchaseStates") {
            Picker("Mode", selection: $mode) {
                Text("Content").tag("content")
                Text("Loading").tag("loading")
                Text("Empty").tag("empty")
                Text("Error").tag("error")
            }
            .pickerStyle(.segmented)

            JchuPurchaseStates<JchuCatalogSampleItem, String, SampleItemCard, EmptyView>(
                cells: 2,
                items: mode == "empty" ? [] : JchuCatalogFixtures.sampleItems,
                isLoading: mode == "loading",
                error: mode == "error" ? "Unable to load" : nil,
                id: \.id,
                config: config,
                headerContent: nil
            ) { _, item in
                SampleItemCard(item: item)
            }
            .frame(minHeight: 260)
            .background(theme.colors.surface.opacity(0.25), in: .rect(cornerRadius: 8))
        }
    }
}

private struct ImagesCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme

    var body: some View {
        JchuScrollableScaffold("Images") {
            CatalogSection("Network image") {
                JchuNetworkImage(
                    url: JchuCatalogFixtures.imageURL,
                    cornerRadius: theme.shapes.corner16
                )
                .frame(height: 220)
            }
        }
    }
}

private struct NetworkImageStatesCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme

    var body: some View {
        JchuScrollableScaffold("Image states") {
            CatalogSection("Placeholder") {
                JchuNetworkImagePlaceholder()
                    .frame(height: 160)
                    .frame(maxWidth: .infinity)
                    .background(theme.colors.surface, in: .rect(cornerRadius: 8))
            }

            CatalogSection("Failure") {
                JchuNetworkImageErrorView()
                    .frame(height: 160)
                    .frame(maxWidth: .infinity)
                    .background(theme.colors.surface, in: .rect(cornerRadius: 8))
            }

            CatalogSection("Configurations") {
                VStack(alignment: .leading, spacing: 8) {
                    CatalogValueRow(name: "poster", value: "\(Int(JchuNetworkImageConfiguration.poster(size: CGSize(width: 180, height: 260)).cornerRadius)) pt corner")
                    CatalogValueRow(name: "galleryThumbnail", value: "\(Int(JchuNetworkImageConfiguration.galleryThumbnail(size: CGSize(width: 120, height: 120)).cornerRadius)) pt corner")
                    CatalogValueRow(name: "fullScreen", value: "\(Int(JchuNetworkImageConfiguration.fullScreen().cornerRadius)) pt corner")
                }
            }
        }
    }
}

private struct NetworkPrefetcherCatalogScreen: View {
    @StateObject private var prefetcher = JchuNetworkImagePrefetcher()

    var body: some View {
        JchuScrollableScaffold("NetworkImagePrefetcher") {
            CatalogSection("API") {
                CatalogValueRow(name: "prefetch(urls:configuration:)", value: "Starts a Kingfisher prefetch request for URL values.")
                CatalogValueRow(name: "prefetch(urlStrings:configuration:)", value: "Compacts URL strings before prefetching.")
                CatalogValueRow(name: "stop()", value: "Cancels and clears the current prefetcher.")
            }

            CatalogSection("Safe catalog action") {
                JchuProgressButton("Stop prefetcher") {
                    prefetcher.stop()
                }
                CatalogLabel("The catalog intentionally avoids starting network prefetch work.")
            }
        }
    }
}

private struct ExtensionsCatalogScreen: View {
    var body: some View {
        JchuScrollableScaffold("Extensions") {
            ForEach(extensionGroups, id: \.self) { group in
                CatalogSection(LocalizedStringKey(group)) {
                    VStack(alignment: .leading, spacing: 10) {
                        ForEach(JchuCatalogFixtures.extensionFixtures.filter { $0.group == group }) { fixture in
                            CatalogValueRow(name: fixture.name, value: fixture.value)
                            Divider()
                        }
                    }
                }
            }
        }
    }

    private var extensionGroups: [String] {
        Array(Set(JchuCatalogFixtures.extensionFixtures.map(\.group))).sorted()
    }
}

private struct ViewExtensionsCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme
    @State private var highlighted = true
    @State private var text = ""

    var body: some View {
        JchuScrollableScaffold("View extensions") {
            CatalogSection("Conditional transform") {
                Toggle("Highlighted", isOn: $highlighted)

                Text("View.if(_:transform:)")
                    .font(theme.typography.body)
                    .padding(12)
                    .if(highlighted) { view in
                        view.roundWithStrokeBackground(
                            corner: 8,
                            container: theme.colors.primary.opacity(0.28),
                            stroke: theme.colors.contentSecondary,
                            lineWidth: 1
                        )
                    } else: { view in
                        view.roundBackground(corner: 8, color: theme.colors.surface)
                    }
            }

            CatalogSection("Placeholder") {
                TextField("", text: $text)
                    .padding(12)
                    .placeholder("Write something", when: text.isEmpty)
                    .roundStrokeBackground(corner: 8, color: theme.colors.contentSecondary.opacity(0.6))
            }

            CatalogSection("Rounded corners") {
                HStack {
                    Text("Top corners")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(theme.colors.primary.opacity(0.4))
                        .cornerRadius(18, corners: [.topLeft, .topRight])

                    Text("Bottom")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(theme.colors.surface)
                        .cornerRadius(18, corners: [.bottomLeft, .bottomRight])
                }
            }

            CatalogSection("Snapshot") {
                CatalogValueRow(
                    name: "Text.snapshot(scale:)",
                    value: Text("Snapshot").padding(8).snapshot(scale: 1)?.size.debugDescription ?? "nil"
                )
            }
        }
    }
}

private struct UIImageExtensionsCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme

    private var sourceImage: UIImage {
        let renderer = UIGraphicsImageRenderer(size: CGSize(width: 640, height: 360))
        return renderer.image { context in
            UIColor(red: 0.47, green: 0.73, blue: 0.60, alpha: 1).setFill()
            context.fill(CGRect(x: 0, y: 0, width: 640, height: 360))
            UIColor(red: 0.66, green: 0.82, blue: 0.71, alpha: 1).setFill()
            context.cgContext.fillEllipse(in: CGRect(x: 220, y: 80, width: 200, height: 200))
        }
    }

    var body: some View {
        let fitted = sourceImage.resizedToFit(maxDimension: 180)
        let targetFitted = sourceImage.resizedToFit(in: CGSize(width: 160, height: 90))
        let optimized = sourceImage.optimizedJPEG(maxDimension: 240, maximumByteCount: 80 * 1_024)

        JchuScrollableScaffold("UIImage extensions") {
            CatalogSection("Resize") {
                HStack(spacing: 12) {
                    Image(uiImage: sourceImage)
                        .resizable()
                        .scaledToFit()
                        .frame(height: 80)

                    Image(uiImage: fitted)
                        .resizable()
                        .scaledToFit()
                        .frame(height: 80)
                }
                .padding(10)
                .background(theme.colors.surface, in: .rect(cornerRadius: 8))
            }

            CatalogSection("Results") {
                CatalogValueRow(name: "Original", value: "\(Int(sourceImage.size.width)) x \(Int(sourceImage.size.height))")
                CatalogValueRow(name: "resizedToFit(maxDimension:)", value: "\(Int(fitted.size.width)) x \(Int(fitted.size.height))")
                CatalogValueRow(name: "resizedToFit(in:)", value: "\(Int(targetFitted.size.width)) x \(Int(targetFitted.size.height))")
                CatalogValueRow(name: "optimizedJPEG(...)", value: optimized.map { "\($0.count) bytes" } ?? "nil")
            }
        }
    }
}

private struct URLImageExtensionsCatalogScreen: View {
    var body: some View {
        JchuScrollableScaffold("URL image extensions") {
            CatalogSection("Async helpers") {
                CatalogValueRow(name: "URL.imageSize(session:)", value: "Downloads image data and returns decoded UIImage size.")
                CatalogValueRow(name: "URL.isPortraitImage(session:)", value: "Uses imageSize(session:) and checks height > width.")
            }

            CatalogSection("Sample URL") {
                CatalogValueRow(
                    name: JchuCatalogFixtures.imageURL?.absoluteString ?? "nil",
                    value: "Used by the NetworkImage catalog screen."
                )
            }
        }
    }
}

private struct DateUtilitiesCatalogScreen: View {
    private let locale = Locale(identifier: "en_US_POSIX")
    private let timeZone = TimeZone(secondsFromGMT: 0)!
    private let sampleDate = Date(timeIntervalSince1970: 1_720_126_920)
    private var calendar: Calendar {
        var calendar = Calendar(identifier: .gregorian)
        calendar.locale = locale
        calendar.timeZone = timeZone
        return calendar
    }

    var body: some View {
        JchuScrollableScaffold("Date utilities") {
            CatalogSection("Formats") {
                CatalogValueRow(name: "jchuDateFormatVerbose", value: jchuDateFormatVerbose)
                CatalogValueRow(name: "format()", value: sampleDate.format(locale: locale, timeZone: timeZone))
                CatalogValueRow(name: "formatOnlyTime()", value: sampleDate.formatOnlyTime(locale: locale, timeZone: timeZone))
                CatalogValueRow(name: "formatToServerDateTimeDefaults()", value: sampleDate.formatToServerDateTimeDefaults(locale: locale, timeZone: timeZone))
                CatalogValueRow(name: "formatToTruncatedDateTime()", value: sampleDate.formatToTruncatedDateTime(locale: locale, timeZone: timeZone))
                CatalogValueRow(name: "toAccessibilityDateMMMMYYYY()", value: sampleDate.toAccessibilityDateMMMMYYYY(locale: locale, timeZone: timeZone))
            }

            CatalogSection("Calendar math") {
                CatalogValueRow(name: "addMonths(1)", value: sampleDate.addMonths(1, calendar: calendar).format(locale: locale, timeZone: timeZone))
                CatalogValueRow(name: "minusDays(2)", value: sampleDate.minusDays(2, calendar: calendar).format(locale: locale, timeZone: timeZone))
                CatalogValueRow(name: "dayEnd()", value: sampleDate.dayEnd(calendar: calendar).formatWithTime(locale: locale, timeZone: timeZone))
                CatalogValueRow(name: "diffInDays(to:)", value: String(sampleDate.diffInDays(to: sampleDate.plusDays(5, calendar: calendar), calendar: calendar)))
            }

            CatalogSection("Global helpers") {
                let nowValue = Date(timeIntervalSince1970: 1_720_126_920)
                let lastFetch = nowValue.addMinutes(-31, calendar: calendar).timeIntervalSince1970 * 1000
                CatalogValueRow(name: "getDateTime(currentMillis:)", value: getDateTime(currentMillis: "1720126920000", locale: locale, timeZone: timeZone) ?? "nil")
                CatalogValueRow(name: "isNextDay(lastFetchTime:)", value: String(isNextDay(lastFetchTime: sampleDate.minusDays(1, calendar: calendar).timeIntervalSince1970 * 1000, now: nowValue, calendar: calendar)))
                CatalogValueRow(name: "isFetchThirtyMinutes(lastFetchTime:)", value: String(isFetchThirtyMinutes(lastFetchTime: lastFetch, now: nowValue)))
                CatalogValueRow(name: "currentYearString()", value: currentYearString(calendar: calendar))
            }
        }
    }
}

private struct ConcurrencyCatalogScreen: View {
    @State private var events: [String] = ["Idle"]

    var body: some View {
        JchuScrollableScaffold("Concurrency") {
            CatalogSection("AsyncSequence.observe") {
                JchuProgressButton("Run observe demo") {
                    Task {
                        let observedEvents = await Self.observeEvents()
                        setEvents(observedEvents)
                    }
                }

                ForEach(events, id: \.self) { event in
                    CatalogValueRow(name: "Event", value: event)
                }
            }

            CatalogSection("Task.perform") {
                CatalogValueRow(name: "Task.perform", value: "Wraps async throwing work and routes success/failure callbacks.")
            }
        }
    }

    @MainActor
    private func setEvents(_ values: [String]) {
        events = values
    }

    nonisolated private static func observeEvents() async -> [String] {
        var captured: [String] = []
        let stream = AsyncStream<Int> { continuation in
            [1, 2, 3].forEach { continuation.yield($0) }
            continuation.finish()
        }

        await stream.observe(
            onStart: {
                captured.append("Started")
            },
            onEach: { value in
                captured.append("Value \(value)")
            },
            onComplete: {
                captured.append("Completed")
            }
        )
        return captured
    }
}

private struct JchuCompatibleCatalogScreen: View {
    var body: some View {
        JchuScrollableScaffold("JchuCompatible") {
            CatalogSection("Collection wrapper") {
                let values = ["A", "B", "C"]
                CatalogValueRow(name: "[].jchu.isNotEmpty", value: String(values.jchu.isNotEmpty))
                CatalogValueRow(name: "[].jchu[safe: 1]", value: values.jchu[safe: 1] ?? "nil")
                CatalogValueRow(name: "[].jchu[safe: 8]", value: values.jchu[safe: 8] ?? "nil")
            }

            CatalogSection("Mutation helper") {
                CatalogValueRow(name: "addAllIfNotExist", value: addAllIfNotExistSample())
            }
        }
    }

    private func addAllIfNotExistSample() -> String {
        var values = ["A", "B"]
        values.addAllIfNotExist(["B", "C", "D"])
        return values.joined(separator: ", ")
    }
}

private struct ThemeTokensCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme

    var body: some View {
        JchuScrollableScaffold("Theme") {
            CatalogSection("Colors") {
                ColorTokenRow(name: "background", color: theme.colors.background)
                ColorTokenRow(name: "surface", color: theme.colors.surface)
                ColorTokenRow(name: "primary", color: theme.colors.primary)
                ColorTokenRow(name: "content", color: theme.colors.content)
                ColorTokenRow(name: "contentSecondary", color: theme.colors.contentSecondary)
                ColorTokenRow(name: "error", color: theme.colors.error)
            }

            CatalogSection("Spacing") {
                FlowLayout(spacing: 8) {
                    TokenPill("04 = \(Int(theme.spacing.dimen04))")
                    TokenPill("08 = \(Int(theme.spacing.dimen08))")
                    TokenPill("12 = \(Int(theme.spacing.dimen12))")
                    TokenPill("16 = \(Int(theme.spacing.dimen16))")
                    TokenPill("24 = \(Int(theme.spacing.dimen24))")
                    TokenPill("32 = \(Int(theme.spacing.dimen32))")
                    TokenPill("48 = \(Int(theme.spacing.dimen48))")
                }
            }

            CatalogSection("Shapes & motion") {
                CatalogValueRow(name: "corner08", value: "\(Int(theme.shapes.corner08))")
                CatalogValueRow(name: "corner16", value: "\(Int(theme.shapes.corner16))")
                CatalogValueRow(name: "corner999", value: "\(Int(theme.shapes.corner999))")
                CatalogValueRow(name: "motion.durationShort", value: "\(theme.motion.durationShort)")
                CatalogValueRow(name: "motion.durationMedium", value: "\(theme.motion.durationMedium)")
            }

            CatalogSection("Preset color themes") {
                ColorThemePreview(name: "garden", colorTheme: JchuAppColorThemes.garden)
                ColorThemePreview(name: "nookPoints", colorTheme: JchuAppColorThemes.nookPoints)
                ColorThemePreview(name: "amiibos", colorTheme: JchuAppColorThemes.amiibos)
                ColorThemePreview(name: "music", colorTheme: JchuAppColorThemes.music)
            }
        }
    }
}

private struct PayCatalogScreen: View {
    @Environment(\.jchuTheme) private var theme

    var body: some View {
        JchuScrollableScaffold("Pay") {
            CatalogSection("Models") {
                CatalogValueRow(name: "JchuSubscriptionType", value: "none, monthly, yearly, promo")
                CatalogValueRow(name: "JchuSubscriptionState", value: "none, active, inactiveUntilRenewal")
                CatalogValueRow(name: "JchuSubscriptionInfo.empty", value: subscriptionSummary(JchuSubscriptionInfo.empty))
                CatalogValueRow(name: "JchuBillingInfo.empty", value: "packages: \(JchuBillingInfo.empty.packages.count), products: \(JchuBillingInfo.empty.products.count)")
            }

            CatalogSection("RevenueCat flow") {
                VStack(alignment: .leading, spacing: 10) {
                    PayStep(number: "1", title: "configure(apiKey:isDebug:subscriptionName:)")
                    PayStep(number: "2", title: "getProducts()")
                    PayStep(number: "3", title: "purchase(type:) / restorePurchases()")
                    PayStep(number: "4", title: "isSubscriptionActive()")
                }
            }

            CatalogSection("Safety") {
                Text("This catalog does not call RevenueCat. It documents the public API and empty states without requiring credentials.")
                    .font(theme.typography.body)
                    .foregroundStyle(theme.colors.contentSecondary)
                    .padding(12)
                    .background(theme.colors.surface, in: .rect(cornerRadius: 8))
            }
        }
    }

    private func subscriptionSummary(_ info: JchuSubscriptionInfo) -> String {
        "renewal: \(info.renewalType), state: \(info.state), promotional: \(info.promotional)"
    }
}

private struct InfoCatalogScreen: View {
    private let coveredModules = [
        "JchuComponentsSwiftUI",
        "JchuComponentsExtensions",
        "JchuComponentsPay",
        "JchuComponentsCore",
    ]

    var body: some View {
        JchuScrollableScaffold("Info") {
            CatalogSection("Package") {
                CatalogValueRow(name: "Version", value: JchuComponentsInfo.version)
                CatalogValueRow(name: "Platform", value: "Swift Package / iOS catalog")
            }

            CatalogSection("Catalog coverage") {
                ForEach(coveredModules, id: \.self) { module in
                    CatalogValueRow(name: module, value: "Visible")
                }
            }

            CatalogSection("Android parity") {
                CatalogValueRow(name: "Base categories", value: "Buttons, Cards, Chips, Inputs, Lists, Loaders, Progress, Toolbars")
                CatalogValueRow(name: "iOS extras", value: "Images, Scaffolds, Extensions, Pay, Theme, Info")
            }
        }
    }
}

private struct CatalogPlaceholderScreen: View {
    let title: String

    var body: some View {
        JchuScrollableScaffold(LocalizedStringKey(title)) {
            JchuDefaultEmptyState("Soon", systemImage: "clock")
        }
    }
}

private struct CatalogMenuRow: View {
    let option: JchuCatalogMenuOption

    var body: some View {
        Label(option.name, systemImage: option.systemImage)
    }
}

private struct CatalogMenuCard: View {
    @Environment(\.jchuTheme) private var theme
    let option: JchuCatalogMenuOption

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: option.systemImage)
                .font(.headline)
                .frame(width: 32, height: 32)
                .foregroundStyle(theme.colors.primary)

            Text(option.name)
                .font(theme.typography.body)
                .foregroundStyle(theme.colors.content)

            Spacer()

            Image(systemName: "chevron.right")
                .font(.caption.weight(.semibold))
                .foregroundStyle(theme.colors.contentSecondary)
        }
        .padding(14)
        .frame(maxWidth: .infinity)
        .background(theme.colors.surface, in: .rect(cornerRadius: 8))
    }
}

private struct CatalogSection<Content: View>: View {
    @Environment(\.jchuTheme) private var theme
    let title: LocalizedStringKey
    let content: Content

    init(_ title: LocalizedStringKey, @ViewBuilder content: () -> Content) {
        self.title = title
        self.content = content()
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(title)
                .font(theme.typography.section)
                .foregroundStyle(theme.colors.content)

            content
                .foregroundStyle(theme.colors.content)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.bottom, 12)
    }
}

private struct CatalogLabel: View {
    @Environment(\.jchuTheme) private var theme
    let title: String

    init(_ title: String) {
        self.title = title
    }

    var body: some View {
        Text(title)
            .font(theme.typography.label)
            .foregroundStyle(theme.colors.contentSecondary)
    }
}

private struct BenefitCard: View {
    @Environment(\.jchuTheme) private var theme
    let title: String
    let subtitle: String

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(title)
                .font(theme.typography.section)
                .foregroundStyle(theme.colors.content)
            Text(subtitle)
                .font(theme.typography.body)
                .foregroundStyle(theme.colors.contentSecondary)
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(theme.colors.surface, in: .rect(cornerRadius: 8))
    }
}

private struct SampleItemCard: View {
    @Environment(\.jchuTheme) private var theme
    let item: JchuCatalogSampleItem

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Image(systemName: item.systemImage)
                .font(.title2)
                .foregroundStyle(theme.colors.contentSecondary)

            Text(item.title)
                .font(theme.typography.section)
                .lineLimit(2)

            Text(item.subtitle)
                .font(theme.typography.label)
                .foregroundStyle(theme.colors.contentSecondary)
                .lineLimit(3)
        }
        .foregroundStyle(theme.colors.content)
        .frame(maxWidth: .infinity, minHeight: 128, alignment: .topLeading)
        .padding(12)
        .background(theme.colors.surface, in: .rect(cornerRadius: 8))
    }
}

private struct CatalogValueRow: View {
    @Environment(\.jchuTheme) private var theme
    let name: String
    let value: String

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            CatalogLabel(name)
            Text(value)
                .font(theme.typography.body)
                .foregroundStyle(theme.colors.content)
                .textSelection(.enabled)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

private struct PayStep: View {
    @Environment(\.jchuTheme) private var theme
    let number: String
    let title: String

    var body: some View {
        HStack(spacing: 10) {
            Text(number)
                .font(theme.typography.label)
                .foregroundStyle(theme.colors.background)
                .frame(width: 28, height: 28)
                .background(theme.colors.contentSecondary, in: Circle())

            Text(title)
                .font(theme.typography.body)
                .foregroundStyle(theme.colors.content)

            Spacer(minLength: 0)
        }
        .padding(12)
        .background(theme.colors.surface, in: .rect(cornerRadius: 8))
    }
}

private struct ColorTokenRow: View {
    @Environment(\.jchuTheme) private var theme
    let name: String
    let color: Color

    var body: some View {
        HStack(spacing: 12) {
            RoundedRectangle(cornerRadius: 8)
                .fill(color)
                .frame(width: 44, height: 32)
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(theme.colors.content.opacity(0.18), lineWidth: 1)
                )

            Text(name)
                .font(theme.typography.body)
                .foregroundStyle(theme.colors.content)

            Spacer()
        }
        .padding(10)
        .background(theme.colors.surface, in: .rect(cornerRadius: 8))
    }
}

private struct TokenPill: View {
    @Environment(\.jchuTheme) private var theme
    let text: String

    init(_ text: String) {
        self.text = text
    }

    var body: some View {
        Text(text)
            .font(theme.typography.label)
            .foregroundStyle(theme.colors.content)
            .padding(.horizontal, 12)
            .padding(.vertical, 8)
            .background(theme.colors.surface, in: Capsule())
    }
}

private struct ColorThemePreview: View {
    @Environment(\.jchuTheme) private var theme
    let name: String
    let colorTheme: JchuScreenColorTheme

    var body: some View {
        HStack(spacing: 10) {
            Circle()
                .fill(colorTheme.primary)
                .frame(width: 28, height: 28)
            Circle()
                .fill(colorTheme.secondary)
                .frame(width: 28, height: 28)

            Text(name)
                .font(theme.typography.body)

            Spacer()
        }
        .foregroundStyle(theme.colors.content)
        .padding(10)
        .background(theme.colors.surface, in: .rect(cornerRadius: 8))
    }
}

private struct JchuStatePreview: View {
    @Environment(\.jchuTheme) private var theme
    let isLoading: Bool
    let isEmpty: Bool

    var body: some View {
        ZStack {
            VStack(alignment: .leading, spacing: 8) {
                Text("Loaded content")
                    .font(.headline)
                Text("This mirrors the state scaffold behavior without leaving the catalog flow.")
                    .font(.body)
            }
            .foregroundStyle(theme.colors.content)
            .opacity(isLoading || isEmpty ? 0 : 1)

            if isLoading {
                JchuLoadingIndicator(label: "Loading")
            } else if isEmpty {
                JchuDefaultEmptyState()
            }
        }
        .frame(maxWidth: .infinity, minHeight: 160)
        .padding()
        .background(theme.colors.surface, in: .rect(cornerRadius: 8))
    }
}

private struct FlowLayout: Layout {
    var spacing: CGFloat

    init(spacing: CGFloat = 8) {
        self.spacing = spacing
    }

    func sizeThatFits(
        proposal: ProposedViewSize,
        subviews: Subviews,
        cache: inout Void
    ) -> CGSize {
        let rows = rows(for: subviews, maxWidth: proposal.width ?? 0)
        return CGSize(
            width: proposal.width ?? rows.map(\.width).max() ?? 0,
            height: rows.map(\.height).reduce(0, +) + CGFloat(max(rows.count - 1, 0)) * spacing
        )
    }

    func placeSubviews(
        in bounds: CGRect,
        proposal: ProposedViewSize,
        subviews: Subviews,
        cache: inout Void
    ) {
        var y = bounds.minY

        for row in rows(for: subviews, maxWidth: bounds.width) {
            var x = bounds.minX

            for item in row.items {
                item.subview.place(
                    at: CGPoint(x: x, y: y),
                    proposal: ProposedViewSize(item.size)
                )
                x += item.size.width + spacing
            }

            y += row.height + spacing
        }
    }

    private func rows(for subviews: Subviews, maxWidth: CGFloat) -> [Row] {
        var rows: [Row] = []
        var current = Row()

        for subview in subviews {
            let size = subview.sizeThatFits(.unspecified)
            let proposedWidth = current.width == 0 ? size.width : current.width + spacing + size.width

            if proposedWidth > maxWidth, !current.items.isEmpty {
                rows.append(current)
                current = Row()
            }

            current.items.append(RowItem(subview: subview, size: size))
            current.width = current.width == 0 ? size.width : current.width + spacing + size.width
            current.height = max(current.height, size.height)
        }

        if !current.items.isEmpty {
            rows.append(current)
        }

        return rows
    }

    private struct Row {
        var items: [RowItem] = []
        var width: CGFloat = 0
        var height: CGFloat = 0
    }

    private struct RowItem {
        let subview: LayoutSubview
        let size: CGSize
    }
}

#Preview("Catalog - Light") {
    JchuComponentsCatalogView()
        .preferredColorScheme(.light)
}

#Preview("Catalog - Dark") {
    JchuComponentsCatalogView()
        .preferredColorScheme(.dark)
}

#Preview("Catalog - Accessibility") {
    JchuComponentsCatalogView()
        .dynamicTypeSize(.accessibility2)
}
