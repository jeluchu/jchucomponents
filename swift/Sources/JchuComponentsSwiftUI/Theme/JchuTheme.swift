//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI

/// The complete set of design tokens available to JchuComponents views.
///
/// Replace individual token groups to adapt the library to an application
/// while retaining defaults for the remaining groups.
public struct JchuTheme: Sendable {
    public var colors: JchuColors
    public var spacing: JchuSpacing
    public var shapes: JchuShapes
    public var typography: JchuTypography
    public var motion: JchuMotion
    public var fontSizes: JchuFontSizes

    /// Creates a theme from semantic token groups.
    ///
    /// - Parameters:
    ///   - colors: Semantic interface colors.
    ///   - spacing: Spacing and sizing scale.
    ///   - shapes: Corner-radius scale.
    ///   - typography: Semantic text styles.
    ///   - motion: Transition durations.
    ///   - fontSizes: Explicit font-size tokens for custom text.
    public init(
        colors: JchuColors = JchuColors(),
        spacing: JchuSpacing = JchuSpacing(),
        shapes: JchuShapes = JchuShapes(),
        typography: JchuTypography = JchuTypography(),
        motion: JchuMotion = JchuMotion(),
        fontSizes: JchuFontSizes = JchuFontSizes()
    ) {
        self.colors = colors
        self.spacing = spacing
        self.shapes = shapes
        self.typography = typography
        self.motion = motion
        self.fontSizes = fontSizes
    }

    /// The default theme backed by dynamic system colors and native text styles.
    public static let standard = JchuTheme()
}

private struct JchuThemeKey: EnvironmentKey {
    static let defaultValue = JchuTheme.standard
}

public extension EnvironmentValues {
    /// The JchuComponents theme inherited by the current view.
    var jchuTheme: JchuTheme {
        get { self[JchuThemeKey.self] }
        set { self[JchuThemeKey.self] = newValue }
    }
}

public extension View {
    /// Provides a JchuComponents theme to this view hierarchy.
    ///
    /// - Parameter theme: The theme inherited by descendant views.
    func jchuTheme(_ theme: JchuTheme) -> some View {
        environment(\.jchuTheme, theme)
    }
}
