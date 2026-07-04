import SwiftUI

public enum JchuPurchaseItemType {
    case horizontal
    case vertical
}

public struct JchuPurchaseElementsScaffold<Item, ID: Hashable, Content: View, TopContent: View, HeaderContent: View>: View {
    @Environment(\.presentationMode) private var presentationMode
    @Environment(\.jchuTheme) private var theme

    private let title: LocalizedStringKey
    private let cells: Int
    private let items: [Item]?
    private let isLoading: Bool
    private let error: String?
    private let id: KeyPath<Item, ID>
    private let filtered: ([Item]) -> [Item]
    private let isColumnList: Bool
    private let config: JchuPurchaseScaffoldConfig
    private let topBarConfig: JchuScaffoldTopBarConfig
    private let onBack: (() -> Void)?
    private let topContent: (() -> TopContent)?
    private let headerContent: (() -> HeaderContent)?
    private let content: (JchuPurchaseItemType, Item) -> Content

    public init(
        _ title: LocalizedStringKey,
        cells: Int = 2,
        items: [Item]?,
        isLoading: Bool,
        error: String? = nil,
        id: KeyPath<Item, ID>,
        filtered: @escaping ([Item]) -> [Item] = { $0 },
        isColumnList: Bool = false,
        config: JchuPurchaseScaffoldConfig = JchuPurchaseScaffoldConfig(),
        topBarConfig: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil,
        topContent: (() -> TopContent)? = nil,
        headerContent: (() -> HeaderContent)? = nil,
        @ViewBuilder content: @escaping (JchuPurchaseItemType, Item) -> Content
    ) {
        self.title = title
        self.cells = cells
        self.items = items
        self.isLoading = isLoading
        self.error = error
        self.id = id
        self.filtered = filtered
        self.isColumnList = isColumnList
        self.config = config
        self.topBarConfig = topBarConfig
        self.onBack = onBack
        self.topContent = topContent
        self.headerContent = headerContent
        self.content = content
    }

    public var body: some View {
        JchuScaffold(
            backgroundColor: config.scaffoldColors.containerColor,
            topBar: {
                VStack(spacing: 0) {
                    JchuScaffoldTopBar(
                        title,
                        backgroundColor: config.scaffoldColors.containerColor,
                        foregroundColor: config.scaffoldColors.contentColor,
                        config: topBarConfig,
                        onBack: handleBack
                    )
                    topContent?()
                }
            },
            bottomBar: {
                if config.searchConfig.isActive && !isLoading && error == nil && items?.isEmpty == false {
                    JchuPurchaseSearchBottomBar(config: config)
                }
            }
        ) {
            JchuPurchaseStates(
                cells: cells,
                items: items,
                isLoading: isLoading,
                error: error,
                id: id,
                filtered: filtered,
                isColumnList: isColumnList,
                config: config,
                headerContent: headerContent,
                content: content
            )
        }
    }

    private func handleBack() {
        if let onBack {
            onBack()
        } else {
            presentationMode.wrappedValue.dismiss()
        }
    }
}

public extension JchuPurchaseElementsScaffold where TopContent == EmptyView, HeaderContent == EmptyView {
    init(
        _ title: LocalizedStringKey,
        cells: Int = 2,
        items: [Item]?,
        isLoading: Bool,
        error: String? = nil,
        id: KeyPath<Item, ID>,
        filtered: @escaping ([Item]) -> [Item] = { $0 },
        isColumnList: Bool = false,
        config: JchuPurchaseScaffoldConfig = JchuPurchaseScaffoldConfig(),
        topBarConfig: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil,
        @ViewBuilder content: @escaping (JchuPurchaseItemType, Item) -> Content
    ) {
        self.init(
            title,
            cells: cells,
            items: items,
            isLoading: isLoading,
            error: error,
            id: id,
            filtered: filtered,
            isColumnList: isColumnList,
            config: config,
            topBarConfig: topBarConfig,
            onBack: onBack,
            topContent: nil,
            headerContent: nil,
            content: content
        )
    }
}

public extension JchuPurchaseElementsScaffold where HeaderContent == EmptyView {
    init(
        _ title: LocalizedStringKey,
        cells: Int = 2,
        items: [Item]?,
        isLoading: Bool,
        error: String? = nil,
        id: KeyPath<Item, ID>,
        filtered: @escaping ([Item]) -> [Item] = { $0 },
        isColumnList: Bool = false,
        config: JchuPurchaseScaffoldConfig = JchuPurchaseScaffoldConfig(),
        topBarConfig: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil,
        @ViewBuilder topContent: @escaping () -> TopContent,
        @ViewBuilder content: @escaping (JchuPurchaseItemType, Item) -> Content
    ) {
        self.init(
            title,
            cells: cells,
            items: items,
            isLoading: isLoading,
            error: error,
            id: id,
            filtered: filtered,
            isColumnList: isColumnList,
            config: config,
            topBarConfig: topBarConfig,
            onBack: onBack,
            topContent: topContent,
            headerContent: nil,
            content: content
        )
    }
}

public extension JchuPurchaseElementsScaffold where TopContent == EmptyView {
    init(
        _ title: LocalizedStringKey,
        cells: Int = 2,
        items: [Item]?,
        isLoading: Bool,
        error: String? = nil,
        id: KeyPath<Item, ID>,
        filtered: @escaping ([Item]) -> [Item] = { $0 },
        isColumnList: Bool = false,
        config: JchuPurchaseScaffoldConfig = JchuPurchaseScaffoldConfig(),
        topBarConfig: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil,
        @ViewBuilder headerContent: @escaping () -> HeaderContent,
        @ViewBuilder content: @escaping (JchuPurchaseItemType, Item) -> Content
    ) {
        self.init(
            title,
            cells: cells,
            items: items,
            isLoading: isLoading,
            error: error,
            id: id,
            filtered: filtered,
            isColumnList: isColumnList,
            config: config,
            topBarConfig: topBarConfig,
            onBack: onBack,
            topContent: nil,
            headerContent: headerContent,
            content: content
        )
    }
}

public struct JchuPurchaseStates<Item, ID: Hashable, Content: View, HeaderContent: View>: View {
    @Environment(\.jchuTheme) private var theme

    private let cells: Int
    private let items: [Item]?
    private let isLoading: Bool
    private let error: String?
    private let id: KeyPath<Item, ID>
    private let filtered: ([Item]) -> [Item]
    private let isColumnList: Bool
    private let config: JchuPurchaseScaffoldConfig
    private let headerContent: (() -> HeaderContent)?
    private let content: (JchuPurchaseItemType, Item) -> Content

    public init(
        cells: Int = 2,
        items: [Item]?,
        isLoading: Bool,
        error: String?,
        id: KeyPath<Item, ID>,
        filtered: @escaping ([Item]) -> [Item] = { $0 },
        isColumnList: Bool = false,
        config: JchuPurchaseScaffoldConfig = JchuPurchaseScaffoldConfig(),
        headerContent: (() -> HeaderContent)? = nil,
        @ViewBuilder content: @escaping (JchuPurchaseItemType, Item) -> Content
    ) {
        self.cells = cells
        self.items = items
        self.isLoading = isLoading
        self.error = error
        self.id = id
        self.filtered = filtered
        self.isColumnList = isColumnList
        self.config = config
        self.headerContent = headerContent
        self.content = content
    }

    public var body: some View {
        JchuRemoteScreenContent(
            data: items,
            isLoading: isLoading,
            error: error,
            loadingContent: {
                JchuLoadingIndicator(label: "Loading")
                    .foregroundStyle(config.scaffoldColors.contentColor)
            },
            successContent: { sourceItems in
                let visibleItems = filtered(sourceItems)

                if config.headerConfig.isCompletedByHiddenFavorites && config.searchConfig.query.wrappedValue.isEmpty {
                    config.hiddenFavoritesContent()
                } else if visibleItems.isEmpty {
                    config.emptyContent()
                } else if isColumnList {
                    columnContent(visibleItems)
                } else {
                    gridContent(visibleItems)
                }
            },
            failureContent: { error in
                config.errorContent(error)
            }
        )
    }

    private func header() -> some View {
        VStack(spacing: theme.spacing.dimen10) {
            config.headerConfig.progressContent
            config.headerConfig.markAllContent
            headerContent?()
        }
    }

    private func columnContent(_ visibleItems: [Item]) -> some View {
        ScrollView(showsIndicators: false) {
            LazyVStack(spacing: theme.spacing.dimen10) {
                header()

                ForEach(visibleItems, id: id) { item in
                    content(.horizontal, item)
                }
            }
            .padding(.horizontal, theme.spacing.dimen16)
            .padding(.top, theme.spacing.dimen10)
            .padding(.bottom, theme.spacing.dimen32 + 70)
        }
    }

    private func gridContent(_ visibleItems: [Item]) -> some View {
        let columns = Array(
            repeating: GridItem(.flexible(), spacing: theme.spacing.dimen10),
            count: max(cells, 1)
        )

        return ScrollView(showsIndicators: false) {
            LazyVGrid(columns: columns, spacing: theme.spacing.dimen10) {
                Section {
                    ForEach(visibleItems, id: id) { item in
                        content(.vertical, item)
                    }
                } header: {
                    header()
                        .gridCellColumns(max(cells, 1))
                }
            }
            .padding(.horizontal, theme.spacing.dimen16)
            .padding(.top, theme.spacing.dimen10)
            .padding(.bottom, theme.spacing.dimen32 + 70)
        }
    }
}

public struct JchuPurchaseTabItemsScaffold<Content: View>: View {
    @Environment(\.presentationMode) private var presentationMode

    private let title: LocalizedStringKey
    private let tabs: [JchuScaffoldTabItem]
    @Binding private var selectedTabID: String
    private let isLoading: Bool
    private let error: String?
    private let isScrollable: Bool
    private let config: JchuPurchaseTabScaffoldConfig
    private let topBarConfig: JchuScaffoldTopBarConfig
    private let onBack: (() -> Void)?
    private let content: Content

    public init(
        _ title: LocalizedStringKey,
        tabs: [JchuScaffoldTabItem],
        selectedTabID: Binding<String>,
        isLoading: Bool,
        error: String? = nil,
        isScrollable: Bool = false,
        config: JchuPurchaseTabScaffoldConfig = JchuPurchaseTabScaffoldConfig(),
        topBarConfig: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil,
        @ViewBuilder content: () -> Content
    ) {
        self.title = title
        self.tabs = tabs
        self._selectedTabID = selectedTabID
        self.isLoading = isLoading
        self.error = error
        self.isScrollable = isScrollable
        self.config = config
        self.topBarConfig = topBarConfig
        self.onBack = onBack
        self.content = content()
    }

    public var body: some View {
        JchuScaffold(
            backgroundColor: config.scaffoldColors.containerColor,
            topBar: {
                JchuScaffoldTopBar(
                    title,
                    backgroundColor: config.scaffoldColors.containerColor,
                    foregroundColor: config.scaffoldColors.contentColor,
                    config: topBarConfig,
                    onBack: handleBack
                )
            },
            bottomBar: {
                VStack(spacing: 0) {
                    if !isLoading && error == nil && config.searchConfig.isActive {
                        JchuExpandableSearch(
                            query: config.searchConfig.query,
                            defaults: config.searchConfig.searchBarDefaults
                        )
                        .padding(.horizontal)
                        .padding(.bottom, 10)
                    }

                    JchuPurchaseTabBar(
                        tabs: tabs,
                        selectedID: $selectedTabID,
                        colors: config.tabColors,
                        isScrollable: isScrollable
                    )
                }
                .background(config.scaffoldColors.containerColor)
            }
        ) {
            content
        }
    }

    private func handleBack() {
        if let onBack {
            onBack()
        } else {
            presentationMode.wrappedValue.dismiss()
        }
    }
}

public struct JchuPurchaseTabBar: View {
    @Environment(\.jchuTheme) private var theme
    @Namespace private var selectionNamespace

    private let tabs: [JchuScaffoldTabItem]
    @Binding private var selectedID: String
    private let colors: JchuPurchaseTabColors
    private let isScrollable: Bool
    private let selectionAnimation = Animation.spring(response: 0.3, dampingFraction: 0.75)

    public init(
        tabs: [JchuScaffoldTabItem],
        selectedID: Binding<String>,
        colors: JchuPurchaseTabColors = JchuPurchaseTabColors(),
        isScrollable: Bool = false
    ) {
        self.tabs = tabs
        self._selectedID = selectedID
        self.colors = colors
        self.isScrollable = isScrollable
    }

    public var body: some View {
        VStack(spacing: 0) {
            if isScrollable {
                GeometryReader { geometry in
                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: theme.spacing.dimen08) {
                            tabButtons
                        }
                        .frame(minWidth: geometry.size.width)
                        .padding(theme.spacing.dimen10)
                    }
                    .background(
                        RoundedRectangle(cornerRadius: theme.shapes.corner16)
                            .fill(colors.contentColor)
                    )
                }
                .frame(height: 60)
            } else {
                HStack(spacing: 0) {
                    tabButtons
                }
                .padding(theme.spacing.dimen10)
                .background(
                    RoundedRectangle(cornerRadius: theme.shapes.corner16)
                        .fill(colors.contentColor)
                )
            }
        }
        .padding()
        .frame(maxWidth: .infinity)
        .padding(.bottom, theme.spacing.dimen24)
        .background(
            UnevenRoundedRectangle(topLeadingRadius: 20, topTrailingRadius: 20)
                .fill(colors.containerColor)
                .ignoresSafeArea(edges: .bottom)
        )
        .animation(selectionAnimation, value: selectedID)
    }

    private var tabButtons: some View {
        ForEach(tabs) { tab in
            Button {
                withAnimation(selectionAnimation) {
                    selectedID = tab.id
                }
            } label: {
                HStack(spacing: theme.spacing.dimen08) {
                    Image(systemName: tab.systemImage)
                        .font(.headline)

                    if selectedID == tab.id || tab.systemImage.isEmpty {
                        Text(tab.title)
                            .font(theme.typography.label)
                            .lineLimit(1)
                            .transition(.opacity.combined(with: .move(edge: .trailing)))
                    }
                }
                .frame(maxWidth: isScrollable ? nil : .infinity)
                .frame(height: 50)
                .padding(.horizontal, theme.spacing.dimen10)
                .foregroundStyle(selectedID == tab.id ? colors.selectedContentColor : colors.unselectedContentColor)
                .background(
                    ZStack {
                        if selectedID == tab.id {
                            RoundedRectangle(cornerRadius: theme.shapes.corner12)
                                .fill(colors.selectedContainerColor)
                                .matchedGeometryEffect(id: "selectedTab", in: selectionNamespace)
                        }
                    }
                )
            }
            .buttonStyle(.plain)
        }
    }
}

private struct JchuPurchaseSearchBottomBar: View {
    @Environment(\.jchuTheme) private var theme

    let config: JchuPurchaseScaffoldConfig

    var body: some View {
        ZStack(alignment: .bottom) {
            LinearGradient(
                colors: [
                    config.scaffoldColors.containerColor.opacity(0),
                    config.scaffoldColors.containerColor
                ],
                startPoint: .top,
                endPoint: .bottom
            )
            .frame(height: 120)
            .ignoresSafeArea(edges: .bottom)

            JchuExpandableSearch(
                query: config.searchConfig.query,
                defaults: config.searchConfig.searchBarDefaults
            )
            .padding(.horizontal, theme.spacing.dimen16)
            .padding(.bottom, theme.spacing.dimen24)
        }
    }
}
