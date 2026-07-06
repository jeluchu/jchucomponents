import Foundation

public extension Optional where Wrapped == Bool {
    /// Returns the wrapped value or a supplied fallback.
    func orFalse(defaultValue: Bool = false) -> Bool {
        self ?? defaultValue
    }

    /// Whether the optional contains `true`.
    var isTrue: Bool {
        self == true
    }
}
