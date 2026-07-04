//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import Foundation

public extension BinaryInteger {
    var isPositive: Bool {
        self > 0
    }

    var isPositiveOrZero: Bool {
        self >= 0
    }

    var isNegative: Bool {
        self < 0
    }

    var isNegativeOrZero: Bool {
        self <= 0
    }
}

public extension BinaryFloatingPoint {
    var isPositive: Bool {
        self > 0
    }

    var isPositiveOrZero: Bool {
        self >= 0
    }

    var isNegative: Bool {
        self < 0
    }

    var isNegativeOrZero: Bool {
        self <= 0
    }
}

public extension Int {
    static var empty: Int {
        0
    }

    var isNotEmpty: Bool {
        self != .empty
    }

    var millisecondsToTimer: String {
        let hours = self / (1000 * 60 * 60)
        let minutes = (self % (1000 * 60 * 60)) / (1000 * 60)
        let seconds = (self % (1000 * 60)) / 1000
        let secondsString = seconds < 10 ? "0\(seconds)" : "\(seconds)"

        if hours > 0 {
            return "\(hours):\(minutes):\(secondsString)"
        }

        return "\(minutes):\(secondsString)"
    }

    var roundedUpToNearestTen: Int {
        Int((Double(self + 5) / 10.0).rounded()) * 10
    }

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

    var bytesToMegabytes: String {
        String(self / (1024 * 1024))
    }
}

public extension Optional where Wrapped == Int64 {
    func orEmpty(defaultValue: Int64 = .empty) -> Int64 {
        self ?? defaultValue
    }
}
