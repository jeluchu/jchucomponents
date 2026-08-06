import SwiftUI

/// A reusable preference row with an optional SF Symbol and supporting text.
public struct JchuPreferenceItem: View {
    @Environment(\.jchuTheme) private var theme

    private let title: LocalizedStringKey
    private let description: LocalizedStringKey?
    private let systemImage: String?
    private let isEnabled: Bool
    private let action: () -> Void

    public init(
        _ title: LocalizedStringKey,
        description: LocalizedStringKey? = nil,
        systemImage: String? = nil,
        isEnabled: Bool = true,
        action: @escaping () -> Void
    ) {
        self.title = title
        self.description = description
        self.systemImage = systemImage
        self.isEnabled = isEnabled
        self.action = action
    }

    public var body: some View {
        Button(action: action) {
            JchuPreferenceLabel(
                title: title,
                description: description,
                systemImage: systemImage
            )
        }
        .buttonStyle(.plain)
        .disabled(!isEnabled)
        .opacity(isEnabled ? 1 : 0.45)
        .padding(.horizontal, theme.spacing.dimen16)
        .padding(.vertical, theme.spacing.dimen14)
        .background(theme.colors.surface)
    }
}

/// A preference row backed by a caller-owned Boolean binding.
public struct JchuPreferenceSwitch: View {
    @Environment(\.jchuTheme) private var theme
    @Binding private var isOn: Bool

    private let title: LocalizedStringKey
    private let description: LocalizedStringKey?
    private let systemImage: String?
    private let isEnabled: Bool

    public init(
        _ title: LocalizedStringKey,
        description: LocalizedStringKey? = nil,
        systemImage: String? = nil,
        isOn: Binding<Bool>,
        isEnabled: Bool = true
    ) {
        self.title = title
        self.description = description
        self.systemImage = systemImage
        _isOn = isOn
        self.isEnabled = isEnabled
    }

    public var body: some View {
        Toggle(isOn: $isOn) {
            JchuPreferenceLabel(
                title: title,
                description: description,
                systemImage: systemImage
            )
        }
        .toggleStyle(.switch)
        .disabled(!isEnabled)
        .opacity(isEnabled ? 1 : 0.45)
        .padding(.horizontal, theme.spacing.dimen16)
        .padding(.vertical, theme.spacing.dimen14)
        .background(theme.colors.surface)
    }
}

/// A single-choice preference row with native selected accessibility state.
public struct JchuPreferenceChoice: View {
    @Environment(\.jchuTheme) private var theme

    private let title: LocalizedStringKey
    private let isSelected: Bool
    private let isEnabled: Bool
    private let action: () -> Void

    public init(
        _ title: LocalizedStringKey,
        isSelected: Bool,
        isEnabled: Bool = true,
        action: @escaping () -> Void
    ) {
        self.title = title
        self.isSelected = isSelected
        self.isEnabled = isEnabled
        self.action = action
    }

    public var body: some View {
        Button(action: action) {
            HStack(spacing: theme.spacing.dimen16) {
                Text(title)
                    .font(theme.typography.section)
                    .foregroundStyle(theme.colors.content)
                    .frame(maxWidth: .infinity, alignment: .leading)

                Image(systemName: isSelected ? "largecircle.fill.circle" : "circle")
                    .foregroundStyle(theme.colors.primary)
                    .accessibilityHidden(true)
            }
        }
        .buttonStyle(.plain)
        .disabled(!isEnabled)
        .opacity(isEnabled ? 1 : 0.45)
        .padding(.horizontal, theme.spacing.dimen16)
        .padding(.vertical, theme.spacing.dimen14)
        .background(theme.colors.surface)
        .accessibilityAddTraits(isSelected ? .isSelected : [])
    }
}

private struct JchuPreferenceLabel: View {
    @Environment(\.jchuTheme) private var theme

    let title: LocalizedStringKey
    let description: LocalizedStringKey?
    let systemImage: String?

    var body: some View {
        HStack(spacing: theme.spacing.dimen16) {
            if let systemImage {
                Image(systemName: systemImage)
                    .font(.title3)
                    .foregroundStyle(theme.colors.contentSecondary)
                    .frame(width: theme.spacing.dimen24)
                    .accessibilityHidden(true)
            }

            VStack(alignment: .leading, spacing: theme.spacing.dimen04) {
                Text(title)
                    .font(theme.typography.section)
                    .foregroundStyle(theme.colors.content)

                if let description {
                    Text(description)
                        .font(theme.typography.body)
                        .foregroundStyle(theme.colors.contentSecondary)
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)
        }
        .contentShape(Rectangle())
    }
}

#Preview {
    @Previewable @State var enabled = true
    @Previewable @State var choice = "Daily"

    VStack(spacing: 1) {
        JchuPreferenceItem(
            "Catalog updates",
            description: "Open the update settings",
            systemImage: "bell"
        ) {}
        JchuPreferenceSwitch(
            "Enable previews",
            description: "Show experimental components",
            systemImage: "sparkles",
            isOn: $enabled
        )
        JchuPreferenceChoice("Daily", isSelected: choice == "Daily") { choice = "Daily" }
        JchuPreferenceChoice("Weekly", isSelected: choice == "Weekly") { choice = "Weekly" }
    }
    .jchuTheme(.standard)
}
