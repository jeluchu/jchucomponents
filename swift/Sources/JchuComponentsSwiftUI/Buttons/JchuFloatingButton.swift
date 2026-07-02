import SwiftUI

public enum JchuFloatingButtonSize: Sendable {
    case small
    case medium
    case large

    var diameter: CGFloat {
        switch self {
        case .small: 40
        case .medium: 52
        case .large: 64
        }
    }

    var iconSize: CGFloat {
        switch self {
        case .small: 16
        case .medium: 22
        case .large: 28
        }
    }
}

public struct JchuFloatingButton: View {
    @Environment(\.jchuTheme) private var theme

    private let systemImage: String
    private let accessibilityLabel: LocalizedStringKey
    private let size: JchuFloatingButtonSize
    private let isEnabled: Bool
    private let isVisible: Bool
    private let action: () -> Void

    public init(
        systemImage: String,
        accessibilityLabel: LocalizedStringKey,
        size: JchuFloatingButtonSize = .medium,
        isEnabled: Bool = true,
        isVisible: Bool = true,
        action: @escaping () -> Void
    ) {
        self.systemImage = systemImage
        self.accessibilityLabel = accessibilityLabel
        self.size = size
        self.isEnabled = isEnabled
        self.isVisible = isVisible
        self.action = action
    }

    public var body: some View {
        Button(action: action) {
            Image(systemName: systemImage)
                .font(.system(size: size.iconSize, weight: .semibold))
                .frame(width: size.diameter, height: size.diameter)
                .foregroundStyle(theme.colors.background)
                .background(
                    isEnabled
                        ? theme.colors.primary
                        : theme.colors.contentSecondary.opacity(0.35)
                )
                .clipShape(Circle())
                .shadow(
                    color: .black.opacity(isEnabled ? 0.18 : 0),
                    radius: theme.spacing.dimen04,
                    y: theme.spacing.dimen02
                )
        }
        .buttonStyle(.plain)
        .disabled(!isEnabled)
        .accessibilityLabel(Text(accessibilityLabel))
        .opacity(isVisible ? 1 : 0)
        .scaleEffect(isVisible ? 1 : 0.8)
        .allowsHitTesting(isVisible)
        .accessibilityHidden(!isVisible)
        .animation(
            .easeInOut(duration: theme.motion.durationShort),
            value: isVisible
        )
    }
}

#Preview {
    HStack {
        JchuFloatingButton(
            systemImage: "plus",
            accessibilityLabel: "Add",
            size: .small
        ) {}
        JchuFloatingButton(
            systemImage: "square.and.arrow.up",
            accessibilityLabel: "Share"
        ) {}
        JchuFloatingButton(
            systemImage: "heart.fill",
            accessibilityLabel: "Favorite",
            size: .large,
            isEnabled: false
        ) {}
    }
    .padding()
}
