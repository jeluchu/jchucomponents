import Foundation
import UIKit

public extension URL {
    /// Downloads image data and returns its decoded dimensions.
    ///
    /// - Parameter session: The URL session used for the request.
    /// - Returns: The decoded image size in points.
    /// - Throws: A URL error for non-success responses or invalid image data.
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

    /// Returns whether the downloaded image is taller than it is wide.
    ///
    /// - Parameter session: The URL session used for the request.
    func isPortraitImage(
        session: URLSession = .shared
    ) async throws -> Bool {
        let size = try await imageSize(session: session)
        return size.height > size.width
    }
}
