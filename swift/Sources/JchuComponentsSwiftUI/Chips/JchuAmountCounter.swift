import SwiftUI

public struct JchuAmountCounterColors {
    public let contentColor: Color
    public let containerColor: Color

    public init(
        contentColor: Color = .clear,
        containerColor: Color = .clear
    ) {
        self.contentColor = contentColor
        self.containerColor = containerColor
    }
}

/// Amount label preserving the existing iNook SwiftUI layout and behavior.
public struct JchuAmountCounter: View {
    private let amount: String
    private let colors: JchuAmountCounterColors
    private let icon: Image

    public init(
        amount: String,
        colors: JchuAmountCounterColors,
        iconName: String
    ) {
        self.amount = amount
        self.colors = colors
        icon = Image(iconName)
    }

    /// Catalog and non-asset convenience that does not alter the asset-name initializer.
    public init(
        amount: String,
        colors: JchuAmountCounterColors,
        systemImageName: String
    ) {
        self.amount = amount
        self.colors = colors
        icon = Image(systemName: systemImageName)
    }

    public var body: some View {
        HStack(spacing: 5) {
            icon
                .resizable()
                .frame(width: 30, height: 30)
                .foregroundColor(colors.contentColor)

            Text(amount)
                .font(.custom("nintendoP_Humming-E_002pr", size: 13))
                .foregroundColor(colors.contentColor)
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 4)
        .frame(maxWidth: .infinity)
        .background(colors.containerColor)
        .cornerRadius(8)
    }
}

#Preview {
    JchuAmountCounter(
        amount: "x3",
        colors: JchuAmountCounterColors(
            contentColor: .primary,
            containerColor: .secondary.opacity(0.2)
        ),
        systemImageName: "star.fill"
    )
    .padding()
}
