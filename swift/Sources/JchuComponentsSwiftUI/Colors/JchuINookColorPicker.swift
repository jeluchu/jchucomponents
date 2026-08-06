import SwiftUI

public struct JchuINookColorOption: Identifiable {
    public let colorAssetName: String
    public let color: Color?
    public let isPremium: Bool

    public var id: String { colorAssetName }

    public init(colorAssetName: String, color: Color? = nil, isPremium: Bool = false) {
        self.colorAssetName = colorAssetName
        self.color = color
        self.isPremium = isPremium
    }
}

/// Faithful presentation and selection behavior from iNook's SwiftUI color settings card.
public struct JchuINookColorPicker<PremiumContent: View>: View {
    @Binding private var selection: String
    private let options: [JchuINookColorOption]
    private let isSubscribed: Bool
    private let isDarkTheme: Bool
    private let title: String
    private let description: String
    private let defaultColorName: String
    private let premiumContent: () -> PremiumContent

    public init(
        selection: Binding<String>,
        options: [JchuINookColorOption],
        isSubscribed: Bool,
        isDarkTheme: Bool,
        title: String = "Phone color",
        description: String = "Choose the color shown in the app.",
        defaultColorName: String = "lighGreenPastel",
        @ViewBuilder premiumContent: @escaping () -> PremiumContent
    ) {
        _selection = selection
        self.options = options
        self.isSubscribed = isSubscribed
        self.isDarkTheme = isDarkTheme
        self.title = title
        self.description = description
        self.defaultColorName = defaultColorName
        self.premiumContent = premiumContent
    }

    public var body: some View {
        VStack(spacing: 10) {
            Text(title)
                .font(.custom("nintendoP_Humming-E_002pr", size: 14))
                .fontWeight(.medium)
                .foregroundColor(jchuDarkCreams)
                .multilineTextAlignment(.center)
                .padding(.top, 15)
                .padding(.bottom, 5)

            VStack {
                LazyVGrid(
                    columns: Array(repeating: GridItem(.flexible()), count: 7),
                    spacing: 10
                ) {
                    ForEach(availableOptions) { option in
                        JchuINookColorItem(
                            option: option,
                            selected: option.colorAssetName == selection
                        ) {
                            selection = option.colorAssetName.isEmpty
                                ? defaultColorName
                                : option.colorAssetName
                        }
                    }
                }
                .padding(.vertical, 10)
                .padding(.horizontal, 10)
            }
            .background(jchuCosmicLatte.opacity(isDarkTheme ? 0.2 : 0.5))
            .clipShape(RoundedRectangle(cornerRadius: 20))
            .padding(.horizontal, 5)

            if !isSubscribed {
                premiumContent()
            }

            Text(description)
                .font(.custom("nintendoP_Humming-E_002pr", size: 12))
                .foregroundColor(jchuDarkCreams)
                .multilineTextAlignment(.center)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 10)
                .padding(.horizontal, 20)
                .padding(.bottom, 15)
        }
        .padding(.horizontal, 10)
        .frame(maxWidth: .infinity)
        .background(jchuDarkCreams.opacity(0.1))
        .clipShape(RoundedRectangle(cornerRadius: 16))
        .padding(.vertical, 10)
    }

    private var availableOptions: [JchuINookColorOption] {
        isSubscribed ? options : options.filter { !$0.isPremium }
    }
}

public extension JchuINookColorPicker where PremiumContent == EmptyView {
    init(
        selection: Binding<String>,
        options: [JchuINookColorOption],
        isSubscribed: Bool,
        isDarkTheme: Bool,
        title: String = "Phone color",
        description: String = "Choose the color shown in the app.",
        defaultColorName: String = "lighGreenPastel"
    ) {
        self.init(
            selection: selection,
            options: options,
            isSubscribed: isSubscribed,
            isDarkTheme: isDarkTheme,
            title: title,
            description: description,
            defaultColorName: defaultColorName,
            premiumContent: { EmptyView() }
        )
    }
}

private struct JchuINookColorItem: View {
    let option: JchuINookColorOption
    let selected: Bool
    let onClick: () -> Void

    var body: some View {
        Button(action: onClick) {
            ZStack {
                Circle()
                    .fill(option.color ?? Color(option.colorAssetName))
                    .frame(width: 40, height: 40)
                if selected {
                    Image(systemName: "checkmark")
                        .resizable()
                        .frame(width: 25, height: 25)
                        .foregroundColor(
                            jchuCheckmarkColor(for: option.color ?? Color(option.colorAssetName))
                        )
                }
            }
        }
        .buttonStyle(.plain)
        .animation(.easeInOut(duration: 0.2), value: selected)
    }
}

private let jchuDarkCreams = Color(red: 160 / 255, green: 129 / 255, blue: 108 / 255)
private let jchuCosmicLatte = Color(red: 254 / 255, green: 248 / 255, blue: 228 / 255)

private func jchuCheckmarkColor(for color: Color) -> Color {
    UIColor(color).jchuIsLight ? jchuDarkCreams : .white
}

private extension UIColor {
    var jchuIsLight: Bool {
        var red: CGFloat = 0
        var green: CGFloat = 0
        var blue: CGFloat = 0
        var alpha: CGFloat = 0
        if getRed(&red, green: &green, blue: &blue, alpha: &alpha) {
            return jchuRelativeLuminance(red, green, blue) > 0.5
        }
        var white: CGFloat = 0
        if getWhite(&white, alpha: &alpha) {
            return jchuRelativeLuminance(white, white, white) > 0.5
        }
        guard
            let converted = cgColor.converted(
                to: CGColorSpace(name: CGColorSpace.sRGB)!,
                intent: .defaultIntent,
                options: nil
            ),
            let components = converted.components
        else { return false }
        if components.count >= 3 {
            return jchuRelativeLuminance(components[0], components[1], components[2]) > 0.5
        }
        if components.count == 2 {
            return jchuRelativeLuminance(components[0], components[0], components[0]) > 0.5
        }
        return false
    }
}

private func jchuRelativeLuminance(_ red: CGFloat, _ green: CGFloat, _ blue: CGFloat) -> Double {
    func linearize(_ value: CGFloat) -> Double {
        let value = Double(value)
        return value <= 0.04045 ? value / 12.92 : pow((value + 0.055) / 1.055, 2.4)
    }
    return 0.2126 * linearize(red) + 0.7152 * linearize(green) + 0.0722 * linearize(blue)
}
