import Foundation

public extension Encodable {
    func toJson(encoder: JSONEncoder = JSONEncoder()) -> String? {
        guard let data = try? encoder.encode(self) else {
            return nil
        }

        return String(data: data, encoding: .utf8)
    }
}

public extension String {
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
