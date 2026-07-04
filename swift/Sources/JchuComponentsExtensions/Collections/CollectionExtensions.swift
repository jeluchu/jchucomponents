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

public extension Array where Element: Equatable {
    mutating func addAllIfNotExist(_ elements: some Collection<Element>) {
        for element in elements where !contains(element) {
            append(element)
        }
    }
}

public extension Array where Element == String {
    func concatenateLowercase() -> String {
        map { $0.lowercased() }.joined()
    }
}

public extension Sequence {
    func chunked(into size: Int) -> [[Element]] {
        guard size > 0 else {
            return []
        }

        return reduce(into: []) { chunks, element in
            if chunks.isEmpty || chunks[chunks.count - 1].count == size {
                chunks.append([element])
            } else {
                chunks[chunks.count - 1].append(element)
            }
        }
    }
}
