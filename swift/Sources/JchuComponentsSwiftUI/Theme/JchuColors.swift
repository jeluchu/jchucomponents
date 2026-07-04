//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI

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
