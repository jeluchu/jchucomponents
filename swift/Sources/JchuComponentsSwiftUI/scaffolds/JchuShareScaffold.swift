import SwiftUI

public struct JchuShareScaffold<Content: View, LoadingContent: View, FailureContent: View>: View {
    @Environment(\.presentationMode) private var presentationMode

    private let title: LocalizedStringKey
    private let isLoading: Bool
    private let error: String?
    private let config: JchuShareScaffoldConfig
    private let topBarConfig: JchuScaffoldTopBarConfig
    private let onBack: (() -> Void)?
    private let onShare: () -> Void
    private let onDownload: () -> Void
    private let content: Content
    private let loadingContent: LoadingContent
    private let failureContent: (String?) -> FailureContent

    public init(
        _ title: LocalizedStringKey,
        isLoading: Bool = false,
        error: String? = nil,
        config: JchuShareScaffoldConfig = JchuShareScaffoldConfig(),
        topBarConfig: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil,
        onShare: @escaping () -> Void,
        onDownload: @escaping () -> Void,
        @ViewBuilder content: () -> Content,
        @ViewBuilder loadingContent: () -> LoadingContent,
        @ViewBuilder failureContent: @escaping (String?) -> FailureContent
    ) {
        self.title = title
        self.isLoading = isLoading
        self.error = error
        self.config = config
        self.topBarConfig = topBarConfig
        self.onBack = onBack
        self.onShare = onShare
        self.onDownload = onDownload
        self.content = content()
        self.loadingContent = loadingContent()
        self.failureContent = failureContent
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
                JchuShareBottomBar(
                    colors: config.shareBarColors,
                    onShare: onShare,
                    onDownload: onDownload
                )
            }
        ) {
            JchuRemoteScreenContent(
                data: error == nil ? true : nil,
                isLoading: isLoading,
                error: error,
                loadingContent: {
                    loadingContent
                },
                successContent: { _ in
                    content
                },
                failureContent: failureContent
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

public extension JchuShareScaffold where LoadingContent == JchuLoadingIndicator, FailureContent == JchuDefaultEmptyState {
    init(
        _ title: LocalizedStringKey,
        isLoading: Bool = false,
        error: String? = nil,
        config: JchuShareScaffoldConfig = JchuShareScaffoldConfig(),
        topBarConfig: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil,
        onShare: @escaping () -> Void,
        onDownload: @escaping () -> Void,
        @ViewBuilder content: () -> Content
    ) {
        self.init(
            title,
            isLoading: isLoading,
            error: error,
            config: config,
            topBarConfig: topBarConfig,
            onBack: onBack,
            onShare: onShare,
            onDownload: onDownload,
            content: content,
            loadingContent: {
                JchuLoadingIndicator(label: "Loading")
            },
            failureContent: { _ in
                JchuDefaultEmptyState("Unable to load content", systemImage: "exclamationmark.triangle")
            }
        )
    }
}

public struct JchuShareBottomBar: View {
    @Environment(\.jchuTheme) private var theme

    private let colors: JchuShareBarColors
    private let shareTitle: LocalizedStringKey
    private let downloadSystemImage: String
    private let onShare: () -> Void
    private let onDownload: () -> Void

    public init(
        colors: JchuShareBarColors = JchuShareBarColors(),
        shareTitle: LocalizedStringKey = "Share",
        downloadSystemImage: String = "square.and.arrow.down",
        onShare: @escaping () -> Void,
        onDownload: @escaping () -> Void
    ) {
        self.colors = colors
        self.shareTitle = shareTitle
        self.downloadSystemImage = downloadSystemImage
        self.onShare = onShare
        self.onDownload = onDownload
    }

    public var body: some View {
        HStack(spacing: theme.spacing.dimen10) {
            Button(action: onShare) {
                Text(shareTitle)
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
            .tint(colors.shareContainerColor)
            .foregroundStyle(colors.shareContentColor)
            .controlSize(.large)

            Button(action: onDownload) {
                Image(systemName: downloadSystemImage)
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.bordered)
            .tint(colors.downloadContainerColor)
            .foregroundStyle(colors.downloadContentColor)
            .controlSize(.large)
            .frame(maxWidth: 92)
        }
        .padding(theme.spacing.dimen12)
        .padding(.bottom, theme.spacing.dimen20)
        .frame(maxWidth: .infinity)
        .background(
            UnevenRoundedRectangle(topLeadingRadius: 20, topTrailingRadius: 20)
                .fill(colors.containerColor)
                .ignoresSafeArea(edges: .bottom)
        )
    }
}

public struct JchuShareScaffoldConfig {
    public var shareBarColors: JchuShareBarColors
    public var scaffoldColors: JchuPurchaseScaffoldColors

    public init(
        shareBarColors: JchuShareBarColors = JchuShareBarColors(),
        scaffoldColors: JchuPurchaseScaffoldColors = JchuPurchaseScaffoldColors()
    ) {
        self.shareBarColors = shareBarColors
        self.scaffoldColors = scaffoldColors
    }
}

public struct JchuShareBarColors {
    public var containerColor: Color
    public var shareContentColor: Color
    public var shareContainerColor: Color
    public var downloadContentColor: Color
    public var downloadContainerColor: Color

    public init(
        containerColor: Color = Color(uiColor: .systemBackground),
        shareContentColor: Color = .white,
        shareContainerColor: Color = .accentColor,
        downloadContentColor: Color = .accentColor,
        downloadContainerColor: Color = .accentColor
    ) {
        self.containerColor = containerColor
        self.shareContentColor = shareContentColor
        self.shareContainerColor = shareContainerColor
        self.downloadContentColor = downloadContentColor
        self.downloadContainerColor = downloadContainerColor
    }
}

public extension JchuScreenColorTheme {
    func toShareScaffoldConfig() -> JchuShareScaffoldConfig {
        JchuShareScaffoldConfig(
            shareBarColors: JchuShareBarColors(
                containerColor: primary.opacity(0.16),
                shareContentColor: secondary,
                shareContainerColor: primary.opacity(0.82),
                downloadContentColor: primary,
                downloadContainerColor: primary.opacity(0.82)
            ),
            scaffoldColors: JchuPurchaseScaffoldColors(
                contentColor: primary,
                containerColor: secondary
            )
        )
    }
}
