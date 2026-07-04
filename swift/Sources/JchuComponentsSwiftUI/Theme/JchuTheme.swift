//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

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
