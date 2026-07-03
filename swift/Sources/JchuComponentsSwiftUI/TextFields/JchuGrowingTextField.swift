import SwiftUI

public struct JchuGrowingTextField: View {
    @Binding private var value: String
    private let defaults: GrowingTextFieldDefaults
    private let onValueChange: (String) -> Void

    public init(
        value: Binding<String>,
        defaults: GrowingTextFieldDefaults,
        onValueChange: @escaping (String) -> Void = { _ in }
    ) {
        _value = value
        self.defaults = defaults
        self.onValueChange = onValueChange
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Label {
                Text(defaults.label)
            } icon: {
                if let systemImage = defaults.systemImage {
                    Image(systemName: systemImage)
                }
            }
            .font(.body)
            .foregroundStyle(defaults.contentColor)

            TextField(defaults.placeholder, text: $value, axis: .vertical)
                .lineLimit(defaults.maxLines)
                .frame(
                    minHeight: CGFloat(defaults.minLines) * 22,
                    alignment: .topLeading
                )
                .padding(12)
                .foregroundStyle(defaults.contentColor)
                .background(
                    defaults.containerColor,
                    in: RoundedRectangle(cornerRadius: 16)
                )

            if let maximum = defaults.maxCharacters {
                Text("\(min(value.count, maximum)) / \(maximum)")
                    .font(.caption)
                    .foregroundStyle(defaults.contentColor.opacity(0.7))
                    .frame(maxWidth: .infinity, alignment: .trailing)
                    .contentTransition(.numericText())
                    .accessibilityLabel(Text("Character count"))
            }
        }
        .animation(
            defaults.animateContentChanges
                ? .spring(response: 0.3, dampingFraction: 0.9)
                : nil,
            value: value
        )
        .onChange(of: value) { _, updatedValue in
            let limitedValue = defaults.maxCharacters.map {
                String(updatedValue.prefix($0))
            } ?? updatedValue

            if limitedValue != updatedValue {
                value = limitedValue
            }
            onValueChange(limitedValue)
        }
    }
}

public struct GrowingTextFieldDefaults {
    public var label: LocalizedStringKey
    public var placeholder: LocalizedStringKey
    public var systemImage: String?
    public var minLines: Int
    public var maxLines: Int?
    public var maxCharacters: Int?
    public var containerColor: Color
    public var contentColor: Color
    public var animateContentChanges: Bool

    public init(
        label: LocalizedStringKey,
        placeholder: LocalizedStringKey = "",
        systemImage: String? = nil,
        minLines: Int = 3,
        maxLines: Int? = nil,
        maxCharacters: Int? = nil,
        containerColor: Color = Color(uiColor: .secondarySystemBackground),
        contentColor: Color = .primary,
        animateContentChanges: Bool = true
    ) {
        precondition(minLines > 0)
        precondition(maxLines == nil || maxLines! >= minLines)
        precondition(maxCharacters == nil || maxCharacters! >= 0)

        self.label = label
        self.placeholder = placeholder
        self.systemImage = systemImage
        self.minLines = minLines
        self.maxLines = maxLines
        self.maxCharacters = maxCharacters
        self.containerColor = containerColor
        self.contentColor = contentColor
        self.animateContentChanges = animateContentChanges
    }
}

#Preview {
    @Previewable @State var value = ""

    JchuGrowingTextField(
        value: $value,
        defaults: GrowingTextFieldDefaults(
            label: "Notes",
            placeholder: "Write something…",
            systemImage: "note.text",
            maxCharacters: 200
        )
    )
    .padding()
}
