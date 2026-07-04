import Foundation
import UIKit

public extension URL {
    func imageSize(
        session: URLSession = .shared
    ) async throws -> CGSize {
        let (data, response) = try await session.data(from: self)
        if let httpResponse = response as? HTTPURLResponse,
           !(200..<300).contains(httpResponse.statusCode) {
            throw URLError(.badServerResponse)
        }
        guard let image = UIImage(data: data) else {
            throw URLError(.cannotDecodeContentData)
        }
        return image.size
    }

    func isPortraitImage(
        session: URLSession = .shared
    ) async throws -> Bool {
        let size = try await imageSize(session: session)
        return size.height > size.width
    }
}
