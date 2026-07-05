import UIKit

public extension UIImage {
    /// Returns an aspect-fit image whose longest edge does not exceed a limit.
    ///
    /// Images already within the limit are returned unchanged.
    func resizedToFit(
        maxDimension: CGFloat,
        opaque: Bool = false,
        backgroundColor: UIColor = .white
    ) -> UIImage {
        guard maxDimension > 0 else {
            return self
        }

        let currentMaximum = max(size.width, size.height)
        guard currentMaximum > maxDimension else {
            return self
        }

        let ratio = maxDimension / currentMaximum
        let targetSize = CGSize(
            width: size.width * ratio,
            height: size.height * ratio
        )
        let format = UIGraphicsImageRendererFormat()
        format.opaque = opaque
        format.scale = scale

        return UIGraphicsImageRenderer(size: targetSize, format: format).image { context in
            if opaque {
                context.cgContext.setFillColor(backgroundColor.cgColor)
                context.fill(CGRect(origin: .zero, size: targetSize))
            }
            draw(in: CGRect(origin: .zero, size: targetSize))
        }
    }

    /// Returns an aspect-fit image contained by the supplied size.
    func resizedToFit(in targetSize: CGSize) -> UIImage {
        guard size.width > 0, size.height > 0,
              targetSize.width > 0, targetSize.height > 0 else {
            return self
        }

        let ratio = min(targetSize.width / size.width, targetSize.height / size.height)
        let fittedSize = CGSize(width: size.width * ratio, height: size.height * ratio)
        return UIGraphicsImageRenderer(size: fittedSize).image { _ in
            draw(in: CGRect(origin: .zero, size: fittedSize))
        }
    }

    /// Produces JPEG data while reducing dimensions and compression quality to
    /// approach a maximum byte count.
    ///
    /// The smallest generated representation is returned when the byte target
    /// cannot be reached without dropping below `minimumQuality`.
    func optimizedJPEG(
        maxDimension: CGFloat = 1_024,
        initialQuality: CGFloat = 0.8,
        minimumQuality: CGFloat = 0.1,
        maximumByteCount: Int = 500 * 1_024
    ) -> Data? {
        guard maximumByteCount > 0 else {
            return nil
        }

        let image = resizedToFit(
            maxDimension: maxDimension,
            opaque: true
        )
        var quality = min(max(initialQuality, minimumQuality), 1)
        let minimum = min(max(minimumQuality, 0), quality)
        var lastData: Data?

        while quality >= minimum {
            guard let data = image.jpegData(compressionQuality: quality) else {
                return lastData
            }
            lastData = data
            if data.count <= maximumByteCount {
                return data
            }
            quality -= 0.1
        }

        return lastData
    }
}
