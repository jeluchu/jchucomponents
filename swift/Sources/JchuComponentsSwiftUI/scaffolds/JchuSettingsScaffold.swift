import SwiftUI

public struct JchuSettingsScaffold<Content: View>: View {
    @Environment(\.presentationMode) private var presentationMode
    @Environment(\.jchuTheme) private var theme

    private let title: LocalizedStringKey
    private let backgroundColor: Color?
    private let contentColor: Color?
    private let surfaceColor: Color?
    private let topBarConfig: JchuScaffoldTopBarConfig
    private let onBack: (() -> Void)?
    private let content: Content

    public init(
        _ title: LocalizedStringKey,
        backgroundColor: Color? = nil,
        contentColor: Color? = nil,
        surfaceColor: Color? = nil,
        topBarConfig: JchuScaffoldTopBarConfig = JchuScaffoldTopBarConfig(),
        onBack: (() -> Void)? = nil,
        @ViewBuilder content: () -> Content
    ) {
        self.title = title
        self.backgroundColor = backgroundColor
        self.contentColor = contentColor
        self.surfaceColor = surfaceColor
        self.topBarConfig = topBarConfig
        self.onBack = onBack
        self.content = content()
    }

    public var body: some View {
        let resolvedBackground = backgroundColor ?? theme.colors.background
        let resolvedContent = contentColor ?? theme.colors.content
        let resolvedSurface = surfaceColor ?? theme.colors.surface

        JchuScaffold(
            backgroundColor: resolvedBackground,
            showsTopBarOverlay: false,
            topBar: {
                JchuScaffoldTopBar(
                    title,
                    backgroundColor: resolvedBackground,
                    foregroundColor: resolvedContent,
                    config: topBarConfig,
                    onBack: handleBack
                )
            }
        ) {
            ScrollView(showsIndicators: false) {
                content
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(theme.spacing.dimen16)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(
                UnevenRoundedRectangle(topLeadingRadius: 40, topTrailingRadius: 40)
                    .fill(resolvedSurface)
                    .ignoresSafeArea(edges: .bottom)
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
