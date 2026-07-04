import SwiftUI

public struct JchuScaffold<Content: View, TopBar: View, BottomBar: View>: View {
    private let backgroundColor: Color
    private let hidesNavigationBar: Bool
    private let showsTopBarOverlay: Bool
    private let content: Content
    private let topBar: TopBar?
    private let bottomBar: BottomBar?

    public init(
        backgroundColor: Color = Color(uiColor: .systemBackground),
        hidesNavigationBar: Bool = true,
        showsTopBarOverlay: Bool = true,
        @ViewBuilder topBar: () -> TopBar? = { nil },
        @ViewBuilder bottomBar: () -> BottomBar? = { nil },
        @ViewBuilder content: () -> Content
    ) {
        self.backgroundColor = backgroundColor
        self.hidesNavigationBar = hidesNavigationBar
        self.showsTopBarOverlay = showsTopBarOverlay
        self.topBar = topBar()
        self.bottomBar = bottomBar()
        self.content = content()
    }

    public var body: some View {
        ZStack {
            backgroundColor
                .ignoresSafeArea()

            VStack(spacing: 0) {
                topBar

                content
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
            }
            .overlay(alignment: .bottom) {
                bottomBar
            }
            .overlay(alignment: .top) {
                if showsTopBarOverlay, let topBar {
                    VStack(spacing: 0) {
                        topBar.opacity(0)

                        LinearGradient(
                            colors: [
                                backgroundColor,
                                backgroundColor.opacity(0)
                            ],
                            startPoint: .top,
                            endPoint: .bottom
                        )
                        .frame(height: 10)
                    }
                }
            }
            .toolbar(hidesNavigationBar ? .hidden : .visible, for: .navigationBar)
        }
        .ignoresSafeArea(edges: .bottom)
    }
}

public extension JchuScaffold where BottomBar == EmptyView {
    init(
        backgroundColor: Color = Color(uiColor: .systemBackground),
        hidesNavigationBar: Bool = true,
        showsTopBarOverlay: Bool = true,
        @ViewBuilder topBar: () -> TopBar?,
        @ViewBuilder content: () -> Content
    ) {
        self.backgroundColor = backgroundColor
        self.hidesNavigationBar = hidesNavigationBar
        self.showsTopBarOverlay = showsTopBarOverlay
        self.topBar = topBar()
        self.bottomBar = nil
        self.content = content()
    }
}

public extension JchuScaffold where TopBar == EmptyView {
    init(
        backgroundColor: Color = Color(uiColor: .systemBackground),
        hidesNavigationBar: Bool = true,
        showsTopBarOverlay: Bool = true,
        @ViewBuilder bottomBar: () -> BottomBar?,
        @ViewBuilder content: () -> Content
    ) {
        self.backgroundColor = backgroundColor
        self.hidesNavigationBar = hidesNavigationBar
        self.showsTopBarOverlay = showsTopBarOverlay
        self.topBar = nil
        self.bottomBar = bottomBar()
        self.content = content()
    }
}

public extension JchuScaffold where TopBar == EmptyView, BottomBar == EmptyView {
    init(
        backgroundColor: Color = Color(uiColor: .systemBackground),
        hidesNavigationBar: Bool = true,
        showsTopBarOverlay: Bool = true,
        @ViewBuilder content: () -> Content
    ) {
        self.backgroundColor = backgroundColor
        self.hidesNavigationBar = hidesNavigationBar
        self.showsTopBarOverlay = showsTopBarOverlay
        self.topBar = nil
        self.bottomBar = nil
        self.content = content()
    }
}

public struct JchuScaffoldTopBarConfig {
    public let titleFont: Font
    public let backSystemImage: String
    public let backIconFont: Font

    public init(
        titleFont: Font = .system(size: 18, weight: .medium),
        backSystemImage: String = "chevron.left",
        backIconFont: Font = .system(size: 20, weight: .semibold)
    ) {
        self.titleFont = titleFont
        self.backSystemImage = backSystemImage
        self.backIconFont = backIconFont
    }
}

public struct JchuScaffoldTopBar<Leading: View, Trailing: View>: View {
    @Environment(\.jchuTheme) private var theme

    private let title: LocalizedStringKey
    private let backgroundColor: Color?
    private let foregroundColor: Color?
    private let titleAlignment: HorizontalAlignment
    private let config: JchuScaffoldTopBarConfig
    private let leading: Leading
    private let trailing: Trailing

    public init(
        _ title: LocalizedStringKey,
        backgroundColor: Color? = nil,
        foregroundColor: Color? = nil,
        titleAlignment: HorizontalAlignment = .trailing,
        config: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        @ViewBuilder leading: () -> Leading,
        @ViewBuilder trailing: () -> Trailing
    ) {
        self.title = title
        self.backgroundColor = backgroundColor
        self.foregroundColor = foregroundColor
        self.titleAlignment = titleAlignment
        self.config = config
        self.leading = leading()
        self.trailing = trailing()
    }

    public var body: some View {
        VStack(spacing: 0) {
            if titleAlignment == .center {
                ZStack {
                    HStack {
                        leading
                            .frame(width: 20, height: 20, alignment: .leading)

                        Spacer(minLength: 0)

                        trailing
                    }

                    titleView
                        .padding(.horizontal, theme.spacing.dimen32)
                }
                .padding(.horizontal, theme.spacing.dimen16)
            } else {
                HStack(spacing: theme.spacing.dimen12) {
                    leading
                        .frame(width: 20, height: 20, alignment: .leading)

                    Spacer(minLength: 0)

                    titleView
                        .padding(.trailing, theme.spacing.dimen16)

                    trailing
                }
                .padding(.horizontal, theme.spacing.dimen16)
            }
        }
        .padding(.vertical, theme.spacing.dimen12)
        .frame(maxWidth: .infinity)
        .background(backgroundColor ?? theme.colors.background)
    }

    private var titleView: some View {
        Text(title)
            .font(config.titleFont)
            .foregroundStyle(foregroundColor ?? theme.colors.content)
            .multilineTextAlignment(textAlignment)
            .lineLimit(2)
    }

    private var textAlignment: TextAlignment {
        switch titleAlignment {
        case .leading:
            .leading
        case .center:
            .center
        default:
            .trailing
        }
    }
}

public extension JchuScaffoldTopBar where Leading == JchuScaffoldBackButton, Trailing == EmptyView {
    init(
        _ title: LocalizedStringKey,
        backgroundColor: Color? = nil,
        foregroundColor: Color? = nil,
        titleAlignment: HorizontalAlignment = .trailing,
        config: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil
    ) {
        self.init(
            title,
            backgroundColor: backgroundColor,
            foregroundColor: foregroundColor,
            titleAlignment: titleAlignment,
            config: config,
            leading: {
                JchuScaffoldBackButton(
                    tint: foregroundColor,
                    systemImage: config.backSystemImage,
                    iconFont: config.backIconFont,
                    action: onBack
                )
            },
            trailing: { EmptyView() }
        )
    }
}

public extension JchuScaffoldTopBar where Leading == EmptyView, Trailing == EmptyView {
    init(
        _ title: LocalizedStringKey,
        backgroundColor: Color? = nil,
        foregroundColor: Color? = nil,
        titleAlignment: HorizontalAlignment = .trailing,
        config: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig()
    ) {
        self.init(
            title,
            backgroundColor: backgroundColor,
            foregroundColor: foregroundColor,
            titleAlignment: titleAlignment,
            config: config,
            leading: { EmptyView() },
            trailing: { EmptyView() }
        )
    }
}

public extension JchuScaffoldTopBar where Trailing == EmptyView {
    init(
        _ title: LocalizedStringKey,
        backgroundColor: Color? = nil,
        foregroundColor: Color? = nil,
        titleAlignment: HorizontalAlignment = .trailing,
        config: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        @ViewBuilder leading: () -> Leading
    ) {
        self.init(
            title,
            backgroundColor: backgroundColor,
            foregroundColor: foregroundColor,
            titleAlignment: titleAlignment,
            config: config,
            leading: leading,
            trailing: { EmptyView() }
        )
    }
}

public struct JchuScaffoldBackButton: View {
    @Environment(\.presentationMode) private var presentationMode
    @Environment(\.jchuTheme) private var theme
    private let tint: Color?
    private let systemImage: String
    private let iconFont: Font
    private let action: (() -> Void)?

    public init(
        tint: Color? = nil,
        systemImage: String = "chevron.left",
        iconFont: Font = .system(size: 20, weight: .semibold),
        action: (() -> Void)? = nil
    ) {
        self.tint = tint
        self.systemImage = systemImage
        self.iconFont = iconFont
        self.action = action
    }

    public var body: some View {
        Button(action: handleBack) {
            Image(systemName: systemImage)
                .font(iconFont)
                .frame(width: 20, height: 20)
                .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
        .foregroundStyle(tint ?? theme.colors.content)
        .accessibilityLabel(Text("Back"))
    }

    private func handleBack() {
        if let action {
            action()
        } else {
            presentationMode.wrappedValue.dismiss()
        }
    }
}

public struct JchuScaffoldBottomBarItem: Identifiable {
    public let id: String
    public let systemImage: String
    public let label: LocalizedStringKey
    public let action: () -> Void

    public init(
        id: String,
        systemImage: String,
        label: LocalizedStringKey,
        action: @escaping () -> Void
    ) {
        self.id = id
        self.systemImage = systemImage
        self.label = label
        self.action = action
    }
}

public struct JchuScaffoldBottomBar: View {
    @Environment(\.jchuTheme) private var theme

    private let items: [JchuScaffoldBottomBarItem]
    @Binding private var selectedID: String

    public init(
        items: [JchuScaffoldBottomBarItem],
        selectedID: Binding<String>
    ) {
        self.items = items
        self._selectedID = selectedID
    }

    public var body: some View {
        HStack {
            ForEach(items) { item in
                Button {
                    selectedID = item.id
                    item.action()
                } label: {
                    VStack(spacing: theme.spacing.dimen04) {
                        Image(systemName: item.systemImage)
                            .font(.headline)

                        Text(item.label)
                            .font(theme.typography.label)
                            .lineLimit(1)
                    }
                    .frame(maxWidth: .infinity)
                    .foregroundStyle(selectedID == item.id ? theme.colors.primary : theme.colors.contentSecondary)
                }
                .buttonStyle(.plain)
            }
        }
        .padding(.horizontal, theme.spacing.dimen12)
        .padding(.top, theme.spacing.dimen10)
        .padding(.bottom, theme.spacing.dimen24)
        .frame(maxWidth: .infinity)
        .background(theme.colors.surface)
    }
}

public struct JchuScrollableScaffold<Content: View>: View {
    @Environment(\.jchuTheme) private var theme

    private let title: LocalizedStringKey
    private let backgroundColor: Color?
    private let showsIndicators: Bool
    private let topBarConfig: JchuScaffoldTopBarConfig
    private let onBack: (() -> Void)?
    private let content: Content

    public init(
        _ title: LocalizedStringKey,
        backgroundColor: Color? = nil,
        showsIndicators: Bool = false,
        topBarConfig: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil,
        @ViewBuilder content: () -> Content
    ) {
        self.title = title
        self.backgroundColor = backgroundColor
        self.showsIndicators = showsIndicators
        self.topBarConfig = topBarConfig
        self.onBack = onBack
        self.content = content()
    }

    public var body: some View {
        JchuScaffold(
            backgroundColor: backgroundColor ?? theme.colors.background,
            topBar: {
                JchuScaffoldTopBar(
                    title,
                    config: topBarConfig,
                    onBack: onBack
                )
            }
        ) {
            ScrollView(showsIndicators: showsIndicators) {
                content
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(theme.spacing.dimen16)
            }
        }
    }
}

public struct JchuStateScaffold<Content: View, EmptyContent: View, LoadingContent: View>: View {
    @Environment(\.jchuTheme) private var theme

    private let title: LocalizedStringKey
    private let isLoading: Bool
    private let isEmpty: Bool
    private let topBarConfig: JchuScaffoldTopBarConfig
    private let onBack: (() -> Void)?
    private let content: Content
    private let emptyContent: EmptyContent
    private let loadingContent: LoadingContent

    public init(
        _ title: LocalizedStringKey,
        isLoading: Bool,
        isEmpty: Bool,
        topBarConfig: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil,
        @ViewBuilder content: () -> Content,
        @ViewBuilder emptyContent: () -> EmptyContent,
        @ViewBuilder loadingContent: () -> LoadingContent
    ) {
        self.title = title
        self.isLoading = isLoading
        self.isEmpty = isEmpty
        self.topBarConfig = topBarConfig
        self.onBack = onBack
        self.content = content()
        self.emptyContent = emptyContent()
        self.loadingContent = loadingContent()
    }

    public var body: some View {
        JchuScrollableScaffold(
            title,
            topBarConfig: topBarConfig,
            onBack: onBack
        ) {
            ZStack {
                content
                    .opacity(isLoading || isEmpty ? 0 : 1)

                if isLoading {
                    loadingContent
                } else if isEmpty {
                    emptyContent
                }
            }
            .frame(maxWidth: .infinity, minHeight: 240)
        }
    }
}

public extension JchuStateScaffold where EmptyContent == JchuDefaultEmptyState, LoadingContent == JchuLoadingIndicator {
    init(
        _ title: LocalizedStringKey,
        isLoading: Bool,
        isEmpty: Bool,
        topBarConfig: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil,
        @ViewBuilder content: () -> Content
    ) {
        self.init(
            title,
            isLoading: isLoading,
            isEmpty: isEmpty,
            topBarConfig: topBarConfig,
            onBack: onBack,
            content: content,
            emptyContent: {
                JchuDefaultEmptyState()
            },
            loadingContent: {
                JchuLoadingIndicator(label: "Loading")
            }
        )
    }
}

public struct JchuDefaultEmptyState: View {
    @Environment(\.jchuTheme) private var theme

    private let title: LocalizedStringKey
    private let systemImage: String

    public init(
        _ title: LocalizedStringKey = "No items",
        systemImage: String = "tray"
    ) {
        self.title = title
        self.systemImage = systemImage
    }

    public var body: some View {
        VStack(spacing: theme.spacing.dimen12) {
            Image(systemName: systemImage)
                .font(.title2)

            Text(title)
                .font(theme.typography.body)
        }
        .foregroundStyle(theme.colors.contentSecondary)
        .frame(maxWidth: .infinity)
        .padding(theme.spacing.dimen24)
    }
}

#Preview("Scrollable scaffold") {
    JchuScrollableScaffold("Scaffold") {
        VStack(alignment: .leading, spacing: 12) {
            Text("Migrated from inook-kmp")
            Text("Reusable SwiftUI scaffold")
        }
    }
    .jchuTheme(.standard)
}
