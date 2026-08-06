import SwiftUI

public struct JchuINookRequirementsCardColors {
    public let strokeColor: Color
    public let contentColor: Color
    public let containerColor: Color
    public let amountInfoColors: JchuINookAmountInfoColors
    public let amountColors: JchuINookRequirementsAmountCounterColors
    public let requirementColor: JchuINookRequirementsAmountCounterColors

    public init(
        strokeColor: Color = .gray.opacity(0.3),
        contentColor: Color = .gray,
        containerColor: Color = .clear,
        amountInfoColors: JchuINookAmountInfoColors = JchuINookAmountInfoColors(),
        amountColors: JchuINookRequirementsAmountCounterColors = JchuINookRequirementsAmountCounterColors(),
        requirementColor: JchuINookRequirementsAmountCounterColors = JchuINookRequirementsAmountCounterColors()
    ) {
        self.strokeColor = strokeColor
        self.contentColor = contentColor
        self.containerColor = containerColor
        self.amountInfoColors = amountInfoColors
        self.amountColors = amountColors
        self.requirementColor = requirementColor
    }
}

public struct JchuINookRequirementsAmountCounterColors {
    public let contentColor: Color
    public let containerColor: Color
    public let iconColor: Color

    public init(
        contentColor: Color = .black,
        containerColor: Color = .gray.opacity(0.1),
        iconColor: Color = .gray
    ) {
        self.contentColor = contentColor
        self.containerColor = containerColor
        self.iconColor = iconColor
    }
}

public struct JchuINookAmountInfoColors {
    public let contentColor: Color
    public let containerColor: Color
    public let iconColor: Color

    public init(
        contentColor: Color = .black,
        containerColor: Color = .gray.opacity(0.1),
        iconColor: Color = .blue
    ) {
        self.contentColor = contentColor
        self.containerColor = containerColor
        self.iconColor = iconColor
    }
}

public struct JchuINookAmountInfoDialogDefaults {
    public let title: String
    public let message: String

    public init(
        title: String = "Information",
        message: String = "Amount details"
    ) {
        self.title = title
        self.message = message
    }
}

/// Literal image-asset port of iNook's native requirements card.
public struct JchuINookRequirementsCard: View {
    private let title: String
    private let amount: String
    private let image: String
    private let colors: JchuINookRequirementsCardColors

    public init(
        title: String,
        amount: String,
        image: String,
        colors: JchuINookRequirementsCardColors = JchuINookRequirementsCardColors()
    ) {
        self.title = title
        self.amount = amount
        self.image = image
        self.colors = colors
    }

    public var body: some View {
        VStack(spacing: 10) {
            Text(title)
                .font(.custom("nintendoP_Humming-E_002pr", size: 12))
                .foregroundColor(colors.contentColor)
                .lineLimit(1)
                .truncationMode(.tail)

            JchuINookRequirementsAmountCounter(
                amount: amount,
                image: Image(image),
                colors: colors.amountColors
            )
        }
        .frame(maxWidth: .infinity)
        .padding(10)
        .background(
            RoundedRectangle(cornerRadius: 15)
                .fill(colors.containerColor)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 15)
                .stroke(colors.strokeColor, lineWidth: 1)
        )
    }
}

/// Native requirements card variant backed by an SF Symbol.
public struct JchuINookRequirementsCardIcon: View {
    private let title: String
    private let amount: String
    private let iconName: String
    private let colors: JchuINookRequirementsCardColors

    public init(
        title: String,
        amount: String,
        iconName: String,
        colors: JchuINookRequirementsCardColors = JchuINookRequirementsCardColors()
    ) {
        self.title = title
        self.amount = amount
        self.iconName = iconName
        self.colors = colors
    }

    public var body: some View {
        VStack(spacing: 10) {
            Text(title)
                .font(.custom("nintendoP_Humming-E_002pr", size: 13))
                .foregroundColor(colors.contentColor)
                .lineLimit(1)
                .truncationMode(.tail)

            JchuINookRequirementsAmountCounter(
                amount: amount,
                image: Image(systemName: iconName),
                colors: colors.amountColors
            )
        }
        .frame(maxWidth: .infinity)
        .padding(10)
        .background(
            RoundedRectangle(cornerRadius: 15)
                .fill(colors.containerColor)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 15)
                .stroke(colors.strokeColor, lineWidth: 1)
        )
    }
}

/// Literal marquee-text port of iNook's native requirements card.
public struct JchuINookRequirementsCardText: View {
    private let title: String
    private let requirement: String
    private let colors: JchuINookRequirementsCardColors

    public init(
        title: String,
        requirement: String,
        colors: JchuINookRequirementsCardColors = JchuINookRequirementsCardColors()
    ) {
        self.title = title
        self.requirement = requirement
        self.colors = colors
    }

    public var body: some View {
        VStack(spacing: 10) {
            Text(title)
                .font(.custom("nintendoP_Humming-E_002pr", size: 12))
                .foregroundColor(colors.contentColor)
                .lineLimit(1)
                .truncationMode(.tail)

            JchuINookRequirementsMarqueeText(
                text: requirement,
                font: .custom("nintendoP_Humming-E_002pr", size: 14),
                threshold: 15,
                alignment: .center
            )
            .foregroundColor(colors.requirementColor.contentColor)
            .padding(.horizontal, 8)
            .padding(.vertical, 8)
            .frame(maxWidth: .infinity)
            .background(
                RoundedRectangle(cornerRadius: 15)
                    .fill(colors.requirementColor.containerColor)
            )
        }
        .frame(maxWidth: .infinity)
        .padding(10)
        .background(
            RoundedRectangle(cornerRadius: 15)
                .fill(colors.containerColor)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 15)
                .stroke(colors.strokeColor, lineWidth: 1)
        )
    }
}

/// Literal amount-information port of iNook's native requirements card.
public struct JchuINookRequirementsCardInfo: View {
    private let title: String
    private let amount: String
    private let colors: JchuINookRequirementsCardColors
    private let dialogDefaults: JchuINookAmountInfoDialogDefaults

    public init(
        title: String,
        amount: String,
        colors: JchuINookRequirementsCardColors = JchuINookRequirementsCardColors(),
        dialogDefaults: JchuINookAmountInfoDialogDefaults = JchuINookAmountInfoDialogDefaults()
    ) {
        self.title = title
        self.amount = amount
        self.colors = colors
        self.dialogDefaults = dialogDefaults
    }

    public var body: some View {
        VStack(spacing: 10) {
            Text(title)
                .font(.custom("nintendoP_Humming-E_002pr", size: 14))
                .foregroundColor(colors.contentColor)
                .lineLimit(1)
                .truncationMode(.tail)

            JchuINookAmountInfo(
                amount: amount,
                dialogDefaults: dialogDefaults,
                colors: colors.amountInfoColors
            )
        }
        .frame(maxWidth: .infinity)
        .padding(10)
        .background(
            RoundedRectangle(cornerRadius: 15)
                .fill(colors.containerColor)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 15)
                .stroke(colors.strokeColor, lineWidth: 1)
        )
    }
}

private struct JchuINookRequirementsAmountCounter: View {
    let amount: String
    let image: Image
    let colors: JchuINookRequirementsAmountCounterColors

    var body: some View {
        HStack(spacing: 8) {
            image
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 24, height: 24)
                .foregroundColor(colors.iconColor)

            Text(amount)
                .font(.custom("nintendoP_Humming-E_002pr", size: 14))
                .foregroundColor(colors.contentColor)
        }
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 12)
        .padding(.vertical, 8)
        .background(
            RoundedRectangle(cornerRadius: 15)
                .fill(colors.containerColor)
        )
    }
}

private struct JchuINookAmountInfo: View {
    let amount: String
    let dialogDefaults: JchuINookAmountInfoDialogDefaults
    let colors: JchuINookAmountInfoColors

    @State private var showDialog = false

    var body: some View {
        Button {
            showDialog = true
        } label: {
            HStack(spacing: 8) {
                Text(amount)
                    .font(.custom("nintendoP_Humming-E_002pr", size: 14))
                    .foregroundColor(colors.contentColor)

                Image(systemName: "info.circle")
                    .foregroundColor(colors.iconColor)
                    .padding(5)
                    .background(
                        RoundedRectangle(cornerRadius: 10)
                            .fill(colors.containerColor)
                    )
            }
            .frame(maxWidth: .infinity)
            .padding(.horizontal, 12)
            .padding(.vertical, 8)
            .background(
                RoundedRectangle(cornerRadius: 15)
                    .fill(colors.containerColor)
            )
        }
        .alert(dialogDefaults.title, isPresented: $showDialog) {
            Button("OK", role: .cancel) {}
        } message: {
            Text(dialogDefaults.message)
        }
    }
}

private struct JchuINookRequirementsMarqueeText: View {
    let text: String
    var font: Font = .body
    var duration: Double = 6.0
    var delay: Double = 1.0
    var threshold: Int = 10
    var alignment: Alignment = .leading

    @State private var textSize: CGSize = .zero
    @State private var animate = false

    var body: some View {
        GeometryReader { geometry in
            let containerWidth = geometry.size.width

            if text.count > threshold {
                ZStack {
                    HStack(spacing: 30) {
                        Text(text)
                            .font(font)
                            .background(
                                GeometryReader { textGeometry in
                                    Color.clear
                                        .onAppear {
                                            textSize = textGeometry.size
                                            animate = true
                                        }
                                }
                            )
                            .fixedSize()

                        Text(text)
                            .font(font)
                            .fixedSize()
                    }
                    .offset(x: animate ? -textSize.width - 30 : 0)
                    .animation(
                        animate
                            ? Animation.linear(duration: duration)
                                .delay(delay)
                                .repeatForever(autoreverses: false)
                            : .default,
                        value: animate
                    )
                }
                .frame(width: containerWidth, alignment: alignment)
                .clipped()
            } else {
                Text(text)
                    .font(font)
                    .lineLimit(1)
                    .truncationMode(.tail)
                    .frame(width: containerWidth, alignment: alignment)
            }
        }
        .frame(height: 20)
    }
}
