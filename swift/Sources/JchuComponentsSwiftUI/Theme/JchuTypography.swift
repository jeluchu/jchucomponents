//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI

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
