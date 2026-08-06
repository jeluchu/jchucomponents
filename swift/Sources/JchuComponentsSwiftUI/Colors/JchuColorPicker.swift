import SwiftUI

/// A typed color option displayed by ``JchuColorPicker``.
public struct JchuColorOption<Value: Hashable>: Identifiable {
    public let value: Value
    public let color: Color
    public let accessibilityLabel: LocalizedStringKey
    public let isEnabled: Bool

    public var id: Value { value }

    public init(
        value: Value,
        color: Color,
        accessibilityLabel: LocalizedStringKey,
        isEnabled: Bool = true
    ) {
        self.value = value
        self.color = color
        self.accessibilityLabel = accessibilityLabel
        self.isEnabled = isEnabled
    }
}

/// A wrapping, single-choice color palette controlled by a binding.
public struct JchuColorPicker<Value: Hashable>: View {
    @Environment(\.jchuTheme) private var theme
    @Binding private var selection: Value

    private let options: [JchuColorOption<Value>]
    private let itemSize: CGFloat
    private let selectionIndicatorColor: Color

    public init(
        options: [JchuColorOption<Value>],
        selection: Binding<Value>,
        itemSize: CGFloat = 44,
        selectionIndicatorColor: Color = .white
    ) {
        self.options = options
        _selection = selection
        self.itemSize = itemSize
        self.selectionIndicatorColor = selectionIndicatorColor
    }

    public var body: some View {
        LazyVGrid(
            columns: [
                GridItem(
                    .adaptive(minimum: itemSize, maximum: itemSize),
                    spacing: theme.spacing.dimen08
                )
            ],
            alignment: .leading,
            spacing: theme.spacing.dimen08
        ) {
            ForEach(options) { option in
                let isSelected = option.value == selection

                Button {
                    selection = option.value
                } label: {
                    Circle()
                        .fill(option.color)
                        .overlay {
                            Circle().stroke(
                                isSelected ? theme.colors.content : theme.colors.contentSecondary,
                                lineWidth: isSelected ? 3 : 1
                            )
                        }
                        .overlay {
                            if isSelected {
                                Image(systemName: "checkmark")
                                    .font(.headline.bold())
                                    .foregroundStyle(selectionIndicatorColor)
                            }
                        }
                        .frame(width: itemSize, height: itemSize)
                }
                .buttonStyle(.plain)
                .disabled(!option.isEnabled)
                .opacity(option.isEnabled ? 1 : 0.38)
                .accessibilityLabel(Text(option.accessibilityLabel))
                .accessibilityAddTraits(isSelected ? .isSelected : [])
            }
        }
    }
}

#Preview {
    @Previewable @State var selection = "green"

    JchuColorPicker(
        options: [
            JchuColorOption(value: "green", color: .green, accessibilityLabel: "Green"),
            JchuColorOption(value: "blue", color: .blue, accessibilityLabel: "Blue"),
            JchuColorOption(value: "orange", color: .orange, accessibilityLabel: "Orange")
        ],
        selection: $selection
    )
    .padding()
    .jchuTheme(.standard)
}
