extension Array: JchuCompatible {}
extension ContiguousArray: JchuCompatible {}
extension Set: JchuCompatible {}
extension Dictionary: JchuCompatible {}

public extension JchuExtension where Base: Collection {
    /// Whether the wrapped collection contains at least one element.
    var isNotEmpty: Bool {
        !base.isEmpty
    }

    /// Returns the element at an index when the index belongs to the collection.
    subscript(safe index: Base.Index) -> Base.Element? {
        base.indices.contains(index) ? base[index] : nil
    }
}

public extension Array where Element: Equatable {
    /// Appends only elements that are not already present in the array.
    ///
    /// Existing order is preserved and new unique elements retain input order.
    mutating func addAllIfNotExist(_ elements: some Collection<Element>) {
        for element in elements where !contains(element) {
            append(element)
        }
    }
}

public extension Array where Element == String {
    /// Lowercases and concatenates all strings without a separator.
    func concatenateLowercase() -> String {
        map { $0.lowercased() }.joined()
    }
}

public extension Sequence {
    /// Groups elements into arrays containing at most `size` elements.
    ///
    /// - Returns: An empty array when `size` is zero or negative.
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
