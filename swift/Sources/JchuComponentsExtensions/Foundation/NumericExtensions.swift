//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import Foundation

public extension BinaryInteger {
    /// Whether this value is greater than zero.
    var isPositive: Bool {
        self > 0
    }

    /// Whether this value is greater than or equal to zero.
    var isPositiveOrZero: Bool {
        self >= 0
    }

    /// Whether this value is less than zero.
    var isNegative: Bool {
        self < 0
    }

    /// Whether this value is less than or equal to zero.
    var isNegativeOrZero: Bool {
        self <= 0
    }
}

public extension BinaryFloatingPoint {
    /// Whether this value is greater than zero.
    var isPositive: Bool {
        self > 0
    }

    /// Whether this value is greater than or equal to zero.
    var isPositiveOrZero: Bool {
        self >= 0
    }

    /// Whether this value is less than zero.
    var isNegative: Bool {
        self < 0
    }

    /// Whether this value is less than or equal to zero.
    var isNegativeOrZero: Bool {
        self <= 0
    }
}

public extension Int {
    /// Integer zero.
    static var empty: Int {
        0
    }

    /// Whether this value is not zero.
    var isNotEmpty: Bool {
        self != .empty
    }

    /// Formats a millisecond duration as `m:ss` or `h:mm:ss`.
    var millisecondsToTimer: String {
        let hours = self / (1000 * 60 * 60)
        let minutes = (self % (1000 * 60 * 60)) / (1000 * 60)
        let seconds = (self % (1000 * 60)) / 1000
        let secondsString = seconds < 10 ? "0\(seconds)" : "\(seconds)"

        if hours > 0 {
            return "\(hours):\(String(format: "%02d", minutes)):\(secondsString)"
        }

        return "\(minutes):\(secondsString)"
    }

    /// Rounds toward positive infinity to a multiple of ten.
    ///
    /// Values that cannot be rounded without overflowing remain unchanged.
    var roundedUpToNearestTen: Int {
        let remainder = self % 10
        guard remainder != 0 else {
            return self
        }

        if self < 0 {
            return self - remainder
        }

        let (rounded, overflow) = addingReportingOverflow(10 - remainder)
        return overflow ? self : rounded
    }

    /// Formats this integer with Spanish-style thousands separators.
    var thousandsFormatted: String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        formatter.groupingSeparator = "."
        formatter.groupingSize = 3
        formatter.locale = Locale(identifier: "es_ES")
        return formatter.string(from: NSNumber(value: self)) ?? String(self)
    }
}

public extension Optional where Wrapped == Int {
    func orEmpty(defaultValue: Int = .empty) -> Int {
        self ?? defaultValue
    }

    var roundedUpToNearestTen: Int {
        orEmpty().roundedUpToNearestTen
    }
}

public extension Double {
    static var empty: Double {
        0.0
    }
}

public extension Optional where Wrapped == Double {
    func orEmpty(defaultValue: Double = .empty) -> Double {
        self ?? defaultValue
    }
}

public extension Float {
    static var empty: Float {
        0.0
    }
}

public extension Optional where Wrapped == Float {
    func orEmpty(defaultValue: Float = .empty) -> Float {
        self ?? defaultValue
    }
}

public extension Int64 {
    static var empty: Int64 {
        0
    }

    /// Whole mebibytes represented by this byte count.
    var bytesToMegabytes: String {
        String(self / (1024 * 1024))
    }
}

public extension Optional where Wrapped == Int64 {
    func orEmpty(defaultValue: Int64 = .empty) -> Int64 {
        self ?? defaultValue
    }
}
