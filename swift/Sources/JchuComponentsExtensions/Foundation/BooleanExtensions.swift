import Foundation

public extension Optional where Wrapped == Bool {
    func orFalse(defaultValue: Bool = false) -> Bool {
        self ?? defaultValue
    }

    var isTrue: Bool {
        self == true
    }
}
