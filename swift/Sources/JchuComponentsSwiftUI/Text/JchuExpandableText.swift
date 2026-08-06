import SwiftUI

/// Text with a caller-owned expanded state and localized disclosure actions.
public struct JchuExpandableText: View {
    @Environment(\.jchuTheme) private var theme
    @Binding private var isExpanded: Bool

    private let text: String
    private let collapsedLineLimit: Int
    private let expandLabel: LocalizedStringKey
    private let collapseLabel: LocalizedStringKey

    public init(
        _ text: String,
        isExpanded: Binding<Bool>,
        collapsedLineLimit: Int = 3,
        expandLabel: LocalizedStringKey = "Show more",
        collapseLabel: LocalizedStringKey = "Show less"
    ) {
        precondition(collapsedLineLimit > 0, "collapsedLineLimit must be greater than zero")
        self.text = text
        _isExpanded = isExpanded
        self.collapsedLineLimit = collapsedLineLimit
        self.expandLabel = expandLabel
        self.collapseLabel = collapseLabel
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: theme.spacing.dimen04) {
            Text(text)
                .font(theme.typography.body)
                .foregroundStyle(theme.colors.content)
                .lineLimit(isExpanded ? nil : collapsedLineLimit)
                .frame(maxWidth: .infinity, alignment: .leading)

            Button {
                withAnimation {
                    isExpanded.toggle()
                }
            } label: {
                HStack(spacing: theme.spacing.dimen04) {
                    Text(isExpanded ? collapseLabel : expandLabel)
                    Image(systemName: isExpanded ? "chevron.up" : "chevron.down")
                        .accessibilityHidden(true)
                }
                .font(theme.typography.body.weight(.semibold))
            }
            .buttonStyle(.plain)
            .foregroundStyle(theme.colors.primary)
            .frame(maxWidth: .infinity, alignment: .trailing)
        }
    }
}

#Preview {
    @Previewable @State var isExpanded = false

    JchuExpandableText(
        String(
            repeating: "A reusable description that can grow without owning application state. ",
            count: 3
        ),
        isExpanded: $isExpanded
    )
    .padding()
    .jchuTheme(.standard)
}
