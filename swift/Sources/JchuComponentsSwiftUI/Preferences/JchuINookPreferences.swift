import SwiftUI
import UIKit

private extension Color {
    static let jchuINookDarkCreams = Color(red: 0.627, green: 0.506, blue: 0.424)
    static let jchuINookLightCreams = Color(red: 0.933, green: 0.914, blue: 0.863)
    static let jchuINookText = Color(red: 0.545, green: 0.494, blue: 0.427)
}

/// Faithful public port of iNook's symbol toggle style.
public struct JchuSymbolToggleStyle: ToggleStyle {
    public var icon: String
    public var borderWidth: CGFloat
    public var checkedTrackColor: Color
    public var uncheckedTrackColor: Color
    public var checkedBorderColor: Color
    public var uncheckedBorderColor: Color
    public var checkedThumbColor: Color
    public var uncheckedThumbColor: Color
    public var checkedIconColor: Color
    public var uncheckedIconColor: Color

    public init(
        icon: String = "checkmark",
        borderWidth: CGFloat = 2,
        checkedTrackColor: Color = Color(red: 0.416, green: 0.533, blue: 0.424).opacity(0.5),
        uncheckedTrackColor: Color = .clear,
        checkedBorderColor: Color = Color(red: 0.416, green: 0.533, blue: 0.424),
        uncheckedBorderColor: Color = .clear,
        checkedThumbColor: Color = .clear,
        uncheckedThumbColor: Color = .clear,
        checkedIconColor: Color = Color(red: 0.996, green: 0.973, blue: 0.894).opacity(0.9),
        uncheckedIconColor: Color = Color(red: 0.996, green: 0.973, blue: 0.894).opacity(0.9)
    ) {
        self.icon = icon
        self.borderWidth = borderWidth
        self.checkedTrackColor = checkedTrackColor
        self.uncheckedTrackColor = uncheckedTrackColor
        self.checkedBorderColor = checkedBorderColor
        self.uncheckedBorderColor = uncheckedBorderColor
        self.checkedThumbColor = checkedThumbColor
        self.uncheckedThumbColor = uncheckedThumbColor
        self.checkedIconColor = checkedIconColor
        self.uncheckedIconColor = uncheckedIconColor
    }

    public func makeBody(configuration: Configuration) -> some View {
        HStack {
            configuration.label
            Spacer()
            ZStack {
                RoundedRectangle(cornerRadius: 30)
                    .fill(configuration.isOn ? checkedTrackColor : uncheckedTrackColor)
                    .overlay(
                        RoundedRectangle(cornerRadius: 30)
                            .strokeBorder(
                                configuration.isOn ? checkedBorderColor : uncheckedBorderColor,
                                lineWidth: borderWidth
                            )
                    )

                Circle()
                    .fill(configuration.isOn ? checkedThumbColor : uncheckedThumbColor)
                    .padding(6)
                    .overlay {
                        Image(systemName: icon)
                            .resizable()
                            .scaledToFit()
                            .padding(10)
                            .foregroundColor(
                                configuration.isOn ? checkedIconColor : uncheckedIconColor
                            )
                    }
                    .offset(x: configuration.isOn ? 10 : -10)
                    .animation(
                        .spring(response: 0.3, dampingFraction: 0.7),
                        value: configuration.isOn
                    )
            }
            .frame(width: 50, height: 32)
            .contentShape(Rectangle())
            .onTapGesture {
                UIImpactFeedbackGenerator(style: .light).impactOccurred()
                withAnimation(.spring(response: 0.3, dampingFraction: 0.7)) {
                    configuration.isOn.toggle()
                }
            }
        }
    }
}

public struct JchuPreferenceToggleColors {
    public var containerColor: Color
    public var contentColor: Color

    public init(
        containerColor: Color = Color(red: 0.627, green: 0.506, blue: 0.424).opacity(0.1),
        contentColor: Color = Color(red: 0.627, green: 0.506, blue: 0.424)
    ) {
        self.containerColor = containerColor
        self.contentColor = contentColor
    }
}

/// Faithful port of iNook's image preference toggle.
public struct JchuPreferenceToggle: View {
    @Binding private var isActive: Bool

    private let image: Image
    private let title: String
    private let colors: JchuPreferenceToggleColors
    private let onToggle: ((Bool) -> Void)?

    public init(
        image: String,
        title: String,
        isActive: Binding<Bool>,
        colors: JchuPreferenceToggleColors = JchuPreferenceToggleColors(),
        onToggle: ((Bool) -> Void)? = nil
    ) {
        self.image = Image(image)
        self.title = title
        _isActive = isActive
        self.colors = colors
        self.onToggle = onToggle
    }

    public init(
        systemImageName: String,
        title: String,
        isActive: Binding<Bool>,
        colors: JchuPreferenceToggleColors = JchuPreferenceToggleColors(),
        onToggle: ((Bool) -> Void)? = nil
    ) {
        image = Image(systemName: systemImageName)
        self.title = title
        _isActive = isActive
        self.colors = colors
        self.onToggle = onToggle
    }

    public var body: some View {
        HStack(alignment: .center) {
            Toggle(
                isOn: Binding(
                    get: { isActive },
                    set: { newValue in
                        isActive = newValue
                        onToggle?(newValue)
                    }
                )
            ) {
                image
                    .resizable()
                    .frame(width: 30, height: 30)
                    .foregroundStyle(colors.contentColor)

                Text(title)
                    .font(.callout)
                    .foregroundColor(colors.contentColor)
                    .padding(.leading, 5)

                Spacer(minLength: 20)
            }
            .toggleStyle(
                JchuSymbolToggleStyle(
                    icon: "leaf.fill",
                    checkedTrackColor: .jchuINookDarkCreams.opacity(0.2),
                    uncheckedTrackColor: .jchuINookDarkCreams.opacity(0.2),
                    checkedBorderColor: .jchuINookDarkCreams,
                    uncheckedBorderColor: .jchuINookDarkCreams.opacity(0.4),
                    checkedThumbColor: .jchuINookDarkCreams,
                    uncheckedThumbColor: .jchuINookDarkCreams,
                    checkedIconColor: .jchuINookLightCreams,
                    uncheckedIconColor: .jchuINookLightCreams
                )
            )
        }
        .padding(.all, 20)
        .background(Color.jchuINookDarkCreams.opacity(0.1).cornerRadius(15))
    }
}

/// Faithful port of iNook's title-and-description settings toggle.
public struct JchuSettingsToggle: View {
    @Binding private var isActive: Bool

    private let title: String
    private let description: String

    public init(title: String, description: String, isActive: Binding<Bool>) {
        self.title = title
        self.description = description
        _isActive = isActive
    }

    public var body: some View {
        Toggle(isOn: $isActive) {
            VStack(alignment: .leading, spacing: 5) {
                Text(title)
                    .font(.callout)
                    .foregroundColor(.jchuINookText)
                Text(description)
                    .font(.footnote)
                    .foregroundColor(.jchuINookText.opacity(0.7))
            }
            Spacer(minLength: 20)
        }
        .toggleStyle(JchuSymbolToggleStyle(icon: "leaf.fill"))
    }
}

#Preview {
    @Previewable @State var isActive = true

    VStack {
        JchuPreferenceToggle(
            systemImageName: "bell",
            title: "Notifications",
            isActive: $isActive
        )
        JchuSettingsToggle(
            title: "Notifications",
            description: "Receive updates",
            isActive: $isActive
        )
    }
    .padding()
}
