import SwiftUI

public struct JchuDetailsScaffold<Details, Content: View, BottomBar: View, EmptyContent: View, LoadingContent: View>: View {
    @Environment(\.presentationMode) private var presentationMode
    @Environment(\.jchuTheme) private var theme

    private let title: LocalizedStringKey
    private let details: Details?
    private let isLoading: Bool
    private let error: String?
    private let config: JchuDetailsScaffoldConfig
    private let topBarConfig: JchuScaffoldTopBarConfig
    private let onBack: (() -> Void)?
    private let bottomBar: BottomBar
    private let content: (Details) -> Content
    private let emptyContent: EmptyContent
    private let loadingContent: LoadingContent

    public init(
        _ title: LocalizedStringKey,
        details: Details?,
        isLoading: Bool = false,
        error: String? = nil,
        config: JchuDetailsScaffoldConfig = JchuDetailsScaffoldConfig(),
        topBarConfig: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil,
        @ViewBuilder bottomBar: () -> BottomBar,
        @ViewBuilder content: @escaping (Details) -> Content,
        @ViewBuilder emptyContent: () -> EmptyContent,
        @ViewBuilder loadingContent: () -> LoadingContent
    ) {
        self.title = title
        self.details = details
        self.isLoading = isLoading
        self.error = error
        self.config = config
        self.topBarConfig = topBarConfig
        self.onBack = onBack
        self.bottomBar = bottomBar()
        self.content = content
        self.emptyContent = emptyContent()
        self.loadingContent = loadingContent()
    }

    public var body: some View {
        JchuScaffold(
            backgroundColor: config.scaffoldColors.containerColor,
            topBar: {
                JchuScaffoldTopBar(
                    title,
                    backgroundColor: config.scaffoldColors.containerColor,
                    foregroundColor: config.scaffoldColors.contentColor,
                    titleAlignment: .center,
                    config: topBarConfig,
                    onBack: handleBack
                )
            },
            bottomBar: {
                bottomBar
            }
        ) {
            ZStack {
                if isLoading {
                    loadingContent
                } else if error?.isEmpty == false || details == nil {
                    emptyContent
                } else if let details {
                    ScrollView(showsIndicators: false) {
                        VStack(alignment: .leading, spacing: theme.spacing.dimen10) {
                            content(details)
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(.horizontal, theme.spacing.dimen16)
                        .padding(.bottom, theme.spacing.dimen32)
                    }
                }
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
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

public extension JchuDetailsScaffold where BottomBar == EmptyView {
    init(
        _ title: LocalizedStringKey,
        details: Details?,
        isLoading: Bool = false,
        error: String? = nil,
        config: JchuDetailsScaffoldConfig = JchuDetailsScaffoldConfig(),
        topBarConfig: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil,
        @ViewBuilder content: @escaping (Details) -> Content,
        @ViewBuilder emptyContent: () -> EmptyContent,
        @ViewBuilder loadingContent: () -> LoadingContent
    ) {
        self.init(
            title,
            details: details,
            isLoading: isLoading,
            error: error,
            config: config,
            topBarConfig: topBarConfig,
            onBack: onBack,
            bottomBar: { EmptyView() },
            content: content,
            emptyContent: emptyContent,
            loadingContent: loadingContent
        )
    }
}

public extension JchuDetailsScaffold where BottomBar == EmptyView, EmptyContent == AnyView, LoadingContent == AnyView {
    init(
        _ title: LocalizedStringKey,
        details: Details?,
        isLoading: Bool = false,
        error: String? = nil,
        config: JchuDetailsScaffoldConfig = JchuDetailsScaffoldConfig(),
        topBarConfig: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil,
        @ViewBuilder content: @escaping (Details) -> Content
    ) {
        self.init(
            title,
            details: details,
            isLoading: isLoading,
            error: error,
            config: config,
            topBarConfig: topBarConfig,
            onBack: onBack,
            bottomBar: { EmptyView() },
            content: content,
            emptyContent: {
                AnyView(
                    JchuDefaultEmptyState()
                        .foregroundStyle(config.scaffoldColors.contentColor)
                )
            },
            loadingContent: {
                AnyView(
                    JchuLoadingIndicator(label: "Loading")
                        .foregroundStyle(config.scaffoldColors.contentColor)
                )
            }
        )
    }
}

#Preview("Details scaffold") {
    JchuDetailsScaffold("Details", details: "Reusable detail") { value in
        Text(value)
    }
    .jchuTheme(.standard)
}
