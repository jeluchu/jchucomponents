/// Namespace used to add JchuComponents helpers without colliding with APIs
/// from the Swift standard library, Foundation or other dependencies.
public struct JchuExtension<Base> {
    public let base: Base

    public init(_ base: Base) {
        self.base = base
    }
}

public protocol JchuCompatible {}

public extension JchuCompatible {
    var jchu: JchuExtension<Self> {
        JchuExtension(self)
    }

    static var jchu: JchuExtension<Self.Type> {
        JchuExtension(Self.self)
    }
}
