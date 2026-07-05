//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI

/// Corner-radius tokens used to keep component shapes consistent.
///
/// Token names encode their default point value. For example, `corner16`
/// defaults to 16 points. Supply overrides when constructing the scale to
/// align JchuComponents with an application design system.
public struct JchuShapes: Sendable {
    public let corner00: CGFloat
    public let corner02: CGFloat
    public let corner03: CGFloat
    public let corner04: CGFloat
    public let corner05: CGFloat
    public let corner06: CGFloat
    public let corner07: CGFloat
    public let corner08: CGFloat
    public let corner09: CGFloat
    public let corner10: CGFloat
    public let corner11: CGFloat
    public let corner12: CGFloat
    public let corner13: CGFloat
    public let corner14: CGFloat
    public let corner15: CGFloat
    public let corner16: CGFloat
    public let corner17: CGFloat
    public let corner18: CGFloat
    public let corner19: CGFloat
    public let corner20: CGFloat
    public let corner21: CGFloat
    public let corner22: CGFloat
    public let corner24: CGFloat
    public let corner25: CGFloat
    public let corner26: CGFloat
    public let corner30: CGFloat
    public let corner32: CGFloat
    public let corner34: CGFloat
    public let corner40: CGFloat
    public let corner48: CGFloat
    public let corner50: CGFloat
    public let corner100: CGFloat
    public let corner999: CGFloat

    public init(
        corner00: CGFloat = 0,
        corner02: CGFloat = 2,
        corner03: CGFloat = 3,
        corner04: CGFloat = 4,
        corner05: CGFloat = 5,
        corner06: CGFloat = 6,
        corner07: CGFloat = 7,
        corner08: CGFloat = 8,
        corner09: CGFloat = 9,
        corner10: CGFloat = 10,
        corner11: CGFloat = 11,
        corner12: CGFloat = 12,
        corner13: CGFloat = 13,
        corner14: CGFloat = 14,
        corner15: CGFloat = 15,
        corner16: CGFloat = 16,
        corner17: CGFloat = 17,
        corner18: CGFloat = 18,
        corner19: CGFloat = 19,
        corner20: CGFloat = 20,
        corner21: CGFloat = 21,
        corner22: CGFloat = 22,
        corner24: CGFloat = 24,
        corner25: CGFloat = 25,
        corner26: CGFloat = 26,
        corner30: CGFloat = 30,
        corner32: CGFloat = 32,
        corner34: CGFloat = 34,
        corner40: CGFloat = 40,
        corner48: CGFloat = 48,
        corner50: CGFloat = 50,
        corner100: CGFloat = 100,
        corner999: CGFloat = 999
    ) {
        self.corner00 = corner00
        self.corner02 = corner02
        self.corner03 = corner03
        self.corner04 = corner04
        self.corner05 = corner05
        self.corner06 = corner06
        self.corner07 = corner07
        self.corner08 = corner08
        self.corner09 = corner09
        self.corner10 = corner10
        self.corner11 = corner11
        self.corner12 = corner12
        self.corner13 = corner13
        self.corner14 = corner14
        self.corner15 = corner15
        self.corner16 = corner16
        self.corner17 = corner17
        self.corner18 = corner18
        self.corner19 = corner19
        self.corner20 = corner20
        self.corner21 = corner21
        self.corner22 = corner22
        self.corner24 = corner24
        self.corner25 = corner25
        self.corner26 = corner26
        self.corner30 = corner30
        self.corner32 = corner32
        self.corner34 = corner34
        self.corner40 = corner40
        self.corner48 = corner48
        self.corner50 = corner50
        self.corner100 = corner100
        self.corner999 = corner999
    }
}
