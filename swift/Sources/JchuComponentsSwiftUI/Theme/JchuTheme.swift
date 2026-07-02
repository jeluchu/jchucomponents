import SwiftUI

public struct JchuTheme: Sendable {
    public var colors: JchuColors
    public var spacing: JchuSpacing
    public var shapes: JchuShapes
    public var typography: JchuTypography
    public var motion: JchuMotion

    public init(
        colors: JchuColors = JchuColors(),
        spacing: JchuSpacing = JchuSpacing(),
        shapes: JchuShapes = JchuShapes(),
        typography: JchuTypography = JchuTypography(),
        motion: JchuMotion = JchuMotion()
    ) {
        self.colors = colors
        self.spacing = spacing
        self.shapes = shapes
        self.typography = typography
        self.motion = motion
    }

    public static let standard = JchuTheme()
}

public struct JchuColors: Sendable {
    public let background: Color
    public let surface: Color
    public let primary: Color
    public let content: Color
    public let contentSecondary: Color
    public let error: Color

    public init(
        background: Color = Color(.systemGroupedBackground),
        surface: Color = Color(.secondarySystemGroupedBackground),
        primary: Color = .accentColor,
        content: Color = .primary,
        contentSecondary: Color = .secondary,
        error: Color = .red
    ) {
        self.background = background
        self.surface = surface
        self.primary = primary
        self.content = content
        self.contentSecondary = contentSecondary
        self.error = error
    }
}

public struct JchuSpacing: Sendable {
    public let dimen02: CGFloat
    public let dimen04: CGFloat
    public let dimen08: CGFloat
    public let dimen10: CGFloat
    public let dimen12: CGFloat
    public let dimen15: CGFloat
    public let dimen16: CGFloat
    public let dimen20: CGFloat
    public let dimen24: CGFloat
    public let dimen32: CGFloat

    public init(
        dimen02: CGFloat = 2,
        dimen04: CGFloat = 4,
        dimen08: CGFloat = 8,
        dimen10: CGFloat = 10,
        dimen12: CGFloat = 12,
        dimen15: CGFloat = 15,
        dimen16: CGFloat = 16,
        dimen20: CGFloat = 20,
        dimen24: CGFloat = 24,
        dimen32: CGFloat = 32
    ) {
        self.dimen02 = dimen02
        self.dimen04 = dimen04
        self.dimen08 = dimen08
        self.dimen10 = dimen10
        self.dimen12 = dimen12
        self.dimen15 = dimen15
        self.dimen16 = dimen16
        self.dimen20 = dimen20
        self.dimen24 = dimen24
        self.dimen32 = dimen32
    }
}

public struct JchuShapes: Sendable {
    public let corner04: CGFloat
    public let corner08: CGFloat
    public let corner10: CGFloat
    public let corner12: CGFloat
    public let corner16: CGFloat
    public let corner24: CGFloat

    public init(
        corner04: CGFloat = 4,
        corner08: CGFloat = 8,
        corner10: CGFloat = 10,
        corner12: CGFloat = 12,
        corner16: CGFloat = 16,
        corner24: CGFloat = 24
    ) {
        self.corner04 = corner04
        self.corner08 = corner08
        self.corner10 = corner10
        self.corner12 = corner12
        self.corner16 = corner16
        self.corner24 = corner24
    }
}

public struct JchuTypography: Sendable {
    public let title: Font
    public let section: Font
    public let body: Font
    public let label: Font

    public init(
        title: Font = .title2.weight(.bold),
        section: Font = .headline,
        body: Font = .body,
        label: Font = .caption.weight(.medium)
    ) {
        self.title = title
        self.section = section
        self.body = body
        self.label = label
    }
}

public struct JchuMotion: Sendable {
    public let durationShort: Double
    public let durationMedium: Double
    public let durationLong: Double

    public init(
        durationShort: Double = 0.15,
        durationMedium: Double = 0.25,
        durationLong: Double = 0.4
    ) {
        self.durationShort = durationShort
        self.durationMedium = durationMedium
        self.durationLong = durationLong
    }
}

private struct JchuThemeKey: EnvironmentKey {
    static let defaultValue = JchuTheme.standard
}

public extension EnvironmentValues {
    var jchuTheme: JchuTheme {
        get { self[JchuThemeKey.self] }
        set { self[JchuThemeKey.self] = newValue }
    }
}

public extension View {
    func jchuTheme(_ theme: JchuTheme) -> some View {
        environment(\.jchuTheme, theme)
    }
}
