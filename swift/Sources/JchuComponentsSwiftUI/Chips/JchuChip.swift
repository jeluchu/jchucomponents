import SwiftUI

public struct JchuChip: View {
    private let title: LocalizedStringKey
    private let isSelected: Bool
    private let action: () -> Void

    public init(
        _ title: LocalizedStringKey,
        isSelected: Bool = false,
        action: @escaping () -> Void
    ) {
        self.title = title
        self.isSelected = isSelected
        self.action = action
    }

    public var body: some View {
        Button(action: action) {
            Text(title)
                .font(.subheadline.weight(.medium))
                .padding(.horizontal, 12)
                .padding(.vertical, 8)
        }
        .buttonStyle(.plain)
        .foregroundStyle(isSelected ? Color.white : Color.accentColor)
        .background(
            isSelected ? Color.accentColor : Color.accentColor.opacity(0.12),
            in: Capsule()
        )
        .accessibilityAddTraits(isSelected ? .isSelected : [])
    }
}

#Preview {
    HStack {
        JchuChip("Default") {}
        JchuChip("Selected", isSelected: true) {}
    }
    .padding()
}
