//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI

/// Semantic SwiftUI font roles used by JchuComponents views.
public struct JchuTypography: Sendable {
    public let title: Font
    public let section: Font
    public let body: Font
    public let label: Font

    /// Creates a semantic typography scale.
    ///
    /// - Parameters:
    ///   - title: Font for screen and prominent content titles.
    ///   - section: Font for section headings.
    ///   - body: Font for primary readable content.
    ///   - label: Font for compact labels and metadata.
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
