/// Namespace used to add JchuComponents helpers without colliding with APIs
/// from the Swift standard library, Foundation or other dependencies.
public struct JchuExtension<Base> {
    /// The value wrapped by the JchuComponents namespace.
    public let base: Base

    /// Creates a namespace wrapper for a value.
    public init(_ base: Base) {
        self.base = base
    }
}

/// A type that exposes JchuComponents helpers through the `.jchu` namespace.
public protocol JchuCompatible {}

public extension JchuCompatible {
    /// JchuComponents helpers scoped to this value.
    var jchu: JchuExtension<Self> {
        JchuExtension(self)
    }

    /// JchuComponents helpers scoped to this type.
    static var jchu: JchuExtension<Self.Type> {
        JchuExtension(Self.self)
    }
}
