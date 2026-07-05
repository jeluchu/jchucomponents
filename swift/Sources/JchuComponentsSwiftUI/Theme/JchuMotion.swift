//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI

/// Duration tokens for short, medium and long interface transitions.
public struct JchuMotion: Sendable {
    public let durationShort: Double
    public let durationMedium: Double
    public let durationLong: Double

    /// Creates a motion scale expressed in seconds.
    ///
    /// - Parameters:
    ///   - durationShort: Duration for small state changes.
    ///   - durationMedium: Duration for standard transitions.
    ///   - durationLong: Duration for prominent transitions.
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
