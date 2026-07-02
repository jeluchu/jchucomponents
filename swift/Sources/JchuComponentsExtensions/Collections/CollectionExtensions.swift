extension Array: JchuCompatible {}
extension ContiguousArray: JchuCompatible {}
extension Set: JchuCompatible {}
extension Dictionary: JchuCompatible {}

public extension JchuExtension where Base: Collection {
    var isNotEmpty: Bool {
        !base.isEmpty
    }

    subscript(safe index: Base.Index) -> Base.Element? {
        base.indices.contains(index) ? base[index] : nil
    }
}
