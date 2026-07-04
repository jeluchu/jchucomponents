//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI

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
