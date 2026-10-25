import SwiftUI

/// A centered view with divider lines on either side.
public struct JchuIconDivider<IconContent: View>: View {
    private let icon: IconContent
    private let size: CGFloat
    private let padding: CGFloat
    private let color: Color
    private let isVertical: Bool

    public init(
        size: CGFloat = 30,
        padding: CGFloat = 20,
        color: Color = .gray,
        isVertical: Bool = false,
        @ViewBuilder icon: () -> IconContent
    ) {
        self.icon = icon()
        self.size = size
        self.padding = padding
        self.color = color
        self.isVertical = isVertical
    }

    public var body: some View {
        Group {
            if isVertical {
                VStack {
                    dividerLine
                    icon.frame(maxWidth: size, maxHeight: size)
                    dividerLine
                }
            } else {
                HStack {
                    dividerLine
                    icon.frame(maxWidth: size, maxHeight: size)
                    dividerLine
                }
            }
        }
        .foregroundColor(color)
    }

    private var dividerLine: some View {
        Divider()
            .overlay(color)
            .padding(isVertical ? .horizontal : .vertical, padding)
    }
}

/// A centered text label with divider lines on either side.
public struct JchuTextDivider: View {
    private let label: String
    private let padding: CGFloat
    private let color: Color
    private let isVertical: Bool
    private let font: Font

    public init(
        label: String,
        padding: CGFloat = 20,
        color: Color = .gray,
        isVertical: Bool = false,
        font: Font = .system(.headline)
    ) {
        self.label = label
        self.padding = padding
        self.color = color
        self.isVertical = isVertical
        self.font = font
    }

    public var body: some View {
        Group {
            if isVertical {
                VStack {
                    dividerLine
                    labelContent
                    dividerLine
                }
            } else {
                HStack {
                    dividerLine
                    labelContent
                    dividerLine
                }
            }
        }
    }

    private var labelContent: some View {
        Text(label)
            .foregroundColor(color)
            .fontWeight(.black)
            .multilineTextAlignment(.center)
            .font(font)
    }

    private var dividerLine: some View {
        Divider()
            .overlay(color.opacity(0.3))
            .padding(isVertical ? .horizontal : .vertical, padding)
    }
}
