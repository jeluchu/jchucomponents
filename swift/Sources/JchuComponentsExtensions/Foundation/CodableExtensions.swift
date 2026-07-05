import Foundation

public extension Encodable {
    /// Encodes this value as a UTF-8 JSON string.
    ///
    /// - Parameter encoder: The encoder used to serialize the value.
    /// - Returns: `nil` when encoding or UTF-8 conversion fails.
    func toJson(encoder: JSONEncoder = JSONEncoder()) -> String? {
        guard let data = try? encoder.encode(self) else {
            return nil
        }

        return String(data: data, encoding: .utf8)
    }
}

public extension String {
    /// Decodes this JSON string into a value.
    ///
    /// - Parameters:
    ///   - type: The expected decoded type.
    ///   - decoder: The decoder used to deserialize the string.
    /// - Returns: `nil` for empty, malformed or incompatible JSON.
    func fromJson<T: Decodable>(
        _ type: T.Type,
        decoder: JSONDecoder = JSONDecoder()
    ) -> T? {
        guard !isEmpty, let data = data(using: .utf8) else {
            return nil
        }

        return try? decoder.decode(type, from: data)
    }
}
