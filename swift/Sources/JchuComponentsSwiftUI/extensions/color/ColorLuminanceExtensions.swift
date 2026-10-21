import Foundation
import SwiftUI
#if canImport(UIKit)
import UIKit
#elseif canImport(AppKit)
import AppKit
#endif

public extension Color {
    /// WCAG relative luminance for this color, from 0 (black) to 1 (white).
    var relativeLuminance: Double {
        #if canImport(UIKit)
        let sourceColor = UIColor(self).cgColor
        #elseif canImport(AppKit)
        let sourceColor = NSColor(self).cgColor
        #else
        return 0.5
        #endif

        guard
            let sRGB = CGColorSpace(name: CGColorSpace.sRGB),
            let color = sourceColor.converted(
                to: sRGB,
                intent: .defaultIntent,
                options: nil
            ),
            let components = color.components
        else {
            return 0.5
        }

        let red: Double
        let green: Double
        let blue: Double
        if components.count >= 3 {
            red = Double(components[0])
            green = Double(components[1])
            blue = Double(components[2])
        } else if components.count == 2 {
            red = Double(components[0])
            green = red
            blue = red
        } else {
            return 0.5
        }

        return Self.wcagLuminance(red: red, green: green, blue: blue)
    }

    /// Returns whether this color is lighter than the supplied luminance cutoff.
    func isLight(threshold: Double = 0.5) -> Bool {
        relativeLuminance > threshold
    }
}

private extension Color {
    static func wcagLuminance(red: Double, green: Double, blue: Double) -> Double {
        func linearize(_ component: Double) -> Double {
            let value = min(max(component, 0), 1)
            return value <= 0.04045 ? value / 12.92 : pow((value + 0.055) / 1.055, 2.4)
        }

        return 0.2126 * linearize(red)
            + 0.7152 * linearize(green)
            + 0.0722 * linearize(blue)
    }
}
