import Foundation

public extension Data {
    var detectedImageFileExtension: String? {
        if starts(with: [0xFF, 0xD8, 0xFF]) {
            return "jpg"
        }
        if starts(with: [0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A]) {
            return "png"
        }
        if starts(with: [0x47, 0x49, 0x46, 0x38]) {
            return "gif"
        }
        if count >= 12,
           self[startIndex..<index(startIndex, offsetBy: 4)] == Data("RIFF".utf8),
           self[index(startIndex, offsetBy: 8)..<index(startIndex, offsetBy: 12)] == Data("WEBP".utf8) {
            return "webp"
        }
        return nil
    }

    var detectedImageMIMEType: String? {
        switch detectedImageFileExtension {
        case "jpg": "image/jpeg"
        case "png": "image/png"
        case "gif": "image/gif"
        case "webp": "image/webp"
        default: nil
        }
    }

    func imageFileName(
        prefix: String = "image",
        date: Date = Date()
    ) -> String? {
        guard let fileExtension = detectedImageFileExtension else {
            return nil
        }
        return "\(prefix)_\(Int(date.timeIntervalSince1970)).\(fileExtension)"
    }

    mutating func appendUTF8(_ string: String) {
        append(contentsOf: string.utf8)
    }

    func dataURI(mimeType: String) -> String {
        "data:\(mimeType);base64,\(base64EncodedString())"
    }

    var detectedImageDataURI: String? {
        detectedImageMIMEType.map(dataURI(mimeType:))
    }
}
