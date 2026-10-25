import SwiftUI

/// A customizable container for a settings row.
///
/// Provide app-specific icons and text through the content builder. The card owns the shared
/// container, spacing, disabled appearance and optional tap behavior.
public struct JchuSettingInfoCard<Content: View>: View {
    private let isEnabled: Bool
    private let onTap: (() -> Void)?
    private let containerColor: Color
    private let contentColor: Color
    private let disabledContainerColor: Color
    private let disabledContentColor: Color
    private let cornerRadius: CGFloat
    private let contentPadding: CGFloat
    private let content: Content

    public init(
        isEnabled: Bool = true,
        containerColor: Color = Color.gray.opacity(0.1),
        contentColor: Color = .primary,
        disabledContainerColor: Color = Color.gray.opacity(0.06),
        disabledContentColor: Color = .secondary,
        cornerRadius: CGFloat = 15,
        contentPadding: CGFloat = 15,
        onTap: (() -> Void)? = nil,
        @ViewBuilder content: () -> Content
    ) {
        self.isEnabled = isEnabled
        self.containerColor = containerColor
        self.contentColor = contentColor
        self.disabledContainerColor = disabledContainerColor
        self.disabledContentColor = disabledContentColor
        self.cornerRadius = cornerRadius
        self.contentPadding = contentPadding
        self.onTap = onTap
        self.content = content()
    }

    public var body: some View {
        Group {
            if let onTap {
                Button(action: onTap) {
                    cardContent
                }
                .buttonStyle(.plain)
                .disabled(!isEnabled)
            } else {
                cardContent
            }
        }
        .foregroundColor(isEnabled ? contentColor : disabledContentColor)
    }

    private var cardContent: some View {
        content
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(contentPadding)
            .background(isEnabled ? containerColor : disabledContainerColor)
            .clipShape(RoundedRectangle(cornerRadius: cornerRadius))
    }
}
