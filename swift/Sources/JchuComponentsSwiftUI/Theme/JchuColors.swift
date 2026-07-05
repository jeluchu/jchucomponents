//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI

/// Semantic color roles used by JchuComponents views.
///
/// Values default to dynamic system colors and therefore adapt to the current
/// appearance and accessibility settings.
public struct JchuColors: Sendable {
    public let background: Color
    public let surface: Color
    public let primary: Color
    public let content: Color
    public let contentSecondary: Color
    public let error: Color

    /// Creates a semantic color palette.
    ///
    /// - Parameters:
    ///   - background: The color behind primary screen content.
    ///   - surface: The color for cards and elevated containers.
    ///   - primary: The accent color for prominent actions.
    ///   - content: The primary foreground color.
    ///   - contentSecondary: The secondary foreground color.
    ///   - error: The color for destructive actions and failure states.
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
