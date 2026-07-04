import Kingfisher
import SwiftUI

public struct JchuNetworkImageConfiguration: Sendable {
    public var contentMode: SwiftUI.ContentMode
    public var cornerRadius: CGFloat
    public var fadeDuration: TimeInterval
    public var targetSize: CGSize?
    public var scaleFactor: CGFloat
    public var cacheOriginalImage: Bool
    public var backgroundDecode: Bool
    public var retryCount: Int
    public var requestModifier: (@Sendable (inout URLRequest) -> Void)?

    public init(
        contentMode: SwiftUI.ContentMode = .fill,
        cornerRadius: CGFloat = 0,
        fadeDuration: TimeInterval = 0.2,
        targetSize: CGSize? = nil,
        scaleFactor: CGFloat = 1,
        cacheOriginalImage: Bool = true,
        backgroundDecode: Bool = true,
        retryCount: Int = 1,
        requestModifier: (@Sendable (inout URLRequest) -> Void)? = nil
    ) {
        self.contentMode = contentMode
        self.cornerRadius = cornerRadius
        self.fadeDuration = fadeDuration
        self.targetSize = targetSize
        self.scaleFactor = scaleFactor
        self.cacheOriginalImage = cacheOriginalImage
        self.backgroundDecode = backgroundDecode
        self.retryCount = retryCount
        self.requestModifier = requestModifier
    }

    public static func poster(
        size: CGSize,
        cornerRadius: CGFloat = 12
    ) -> JchuNetworkImageConfiguration {
        JchuNetworkImageConfiguration(
            contentMode: .fill,
            cornerRadius: cornerRadius,
            targetSize: size
        )
    }

    public static func galleryThumbnail(
        size: CGSize,
        cornerRadius: CGFloat = 18,
        requestModifier: (@Sendable (inout URLRequest) -> Void)? = nil
    ) -> JchuNetworkImageConfiguration {
        JchuNetworkImageConfiguration(
            contentMode: .fill,
            cornerRadius: cornerRadius,
            targetSize: size,
            retryCount: 2,
            requestModifier: requestModifier
        )
    }

    public static func fullScreen(
        targetSize: CGSize = CGSize(width: 1280, height: 1280),
        requestModifier: (@Sendable (inout URLRequest) -> Void)? = nil
    ) -> JchuNetworkImageConfiguration {
        JchuNetworkImageConfiguration(
            contentMode: .fit,
            targetSize: targetSize,
            retryCount: 2,
            requestModifier: requestModifier
        )
    }

    var kingfisherOptions: KingfisherOptionsInfo {
        var options: KingfisherOptionsInfo = [
            .scaleFactor(scaleFactor)
        ]

        if let targetSize {
            options.append(.processor(DownsamplingImageProcessor(size: targetSize)))
        }

        if cacheOriginalImage {
            options.append(.cacheOriginalImage)
        }

        if backgroundDecode {
            options.append(.backgroundDecode)
        }

        if retryCount > 0 {
            options.append(.retryStrategy(DelayRetryStrategy(maxRetryCount: retryCount, retryInterval: .seconds(1))))
        }

        if let requestModifier {
            options.append(.requestModifier(AnyModifier { request in
                var modifiedRequest = request
                requestModifier(&modifiedRequest)
                return modifiedRequest
            }))
        }

        return options
    }
}

@MainActor
public final class JchuNetworkImagePrefetcher: ObservableObject {
    private var prefetcher: ImagePrefetcher?

    public init() {}

    public func prefetch(
        urls: [URL],
        configuration: JchuNetworkImageConfiguration = JchuNetworkImageConfiguration()
    ) {
        guard !urls.isEmpty else { return }
        prefetcher?.stop()
        prefetcher = ImagePrefetcher(
            urls: urls,
            options: configuration.kingfisherOptions
        )
        prefetcher?.start()
    }

    public func prefetch(
        urlStrings: [String],
        configuration: JchuNetworkImageConfiguration = JchuNetworkImageConfiguration()
    ) {
        prefetch(
            urls: urlStrings.compactMap(URL.init(string:)),
            configuration: configuration
        )
    }

    public func stop() {
        prefetcher?.stop()
        prefetcher = nil
    }
}

public struct JchuNetworkImagePlaceholder: View {
    private let systemImage: String

    public init(systemImage: String = "photo") {
        self.systemImage = systemImage
    }

    public var body: some View {
        ZStack {
            Rectangle()
                .fill(Color.secondary.opacity(0.12))
            Image(systemName: systemImage)
                .font(.system(size: 28, weight: .medium))
                .foregroundStyle(.secondary)
        }
    }
}

public struct JchuNetworkImageErrorView: View {
    private let systemImage: String

    public init(systemImage: String = "photo.badge.exclamationmark") {
        self.systemImage = systemImage
    }

    public var body: some View {
        ZStack {
            Rectangle()
                .fill(Color.secondary.opacity(0.12))
            Image(systemName: systemImage)
                .font(.system(size: 28, weight: .medium))
                .foregroundStyle(.secondary)
        }
    }
}

public struct JchuNetworkImage<Placeholder: View, Failure: View>: View {
    private let url: URL?
    private let configuration: JchuNetworkImageConfiguration
    private let placeholder: () -> Placeholder
    private let failure: () -> Failure
    private let onLoadingStateChange: ((Bool) -> Void)?
    private let onFailure: ((KingfisherError) -> Void)?
    private let onSuccess: (() -> Void)?
    @State private var hasFailed = false

    public init(
        url: URL?,
        configuration: JchuNetworkImageConfiguration = JchuNetworkImageConfiguration(),
        onLoadingStateChange: ((Bool) -> Void)? = nil,
        onFailure: ((KingfisherError) -> Void)? = nil,
        onSuccess: (() -> Void)? = nil,
        @ViewBuilder placeholder: @escaping () -> Placeholder,
        @ViewBuilder failure: @escaping () -> Failure
    ) {
        self.url = url
        self.configuration = configuration
        self.placeholder = placeholder
        self.failure = failure
        self.onLoadingStateChange = onLoadingStateChange
        self.onFailure = onFailure
        self.onSuccess = onSuccess
    }

    public var body: some View {
        Group {
            if let url, !hasFailed {
                configuredImage(url)
                    .placeholder {
                        placeholder()
                    }
                    .onSuccess { _ in
                        hasFailed = false
                        onLoadingStateChange?(false)
                        onSuccess?()
                    }
                    .onFailure { error in
                        hasFailed = true
                        onLoadingStateChange?(false)
                        onFailure?(error)
                    }
                    .cancelOnDisappear(true)
                    .fade(duration: configuration.fadeDuration)
                    .resizable()
                    .aspectRatio(contentMode: configuration.contentMode)
                    .onAppear {
                        onLoadingStateChange?(true)
                    }
            } else {
                failure()
                    .onAppear {
                        onLoadingStateChange?(false)
                    }
            }
        }
        .clipShape(RoundedRectangle(cornerRadius: configuration.cornerRadius, style: .continuous))
    }

    private func configuredImage(_ url: URL) -> KFImage {
        var image = KFImage(url)
            .backgroundDecode(configuration.backgroundDecode)
            .cacheOriginalImage(configuration.cacheOriginalImage)
            .scaleFactor(configuration.scaleFactor)

        if let targetSize = configuration.targetSize {
            image = image.downsampling(size: targetSize)
        }

        if configuration.retryCount > 0 {
            image = image.retry(maxCount: configuration.retryCount, interval: .seconds(1))
        }

        if let requestModifier = configuration.requestModifier {
            image = image.requestModifier(requestModifier)
        }

        return image
    }
}

#Preview("Network image states") {
    VStack(spacing: 16) {
        JchuNetworkImage(
            url: URL(string: "https://picsum.photos/id/237/400/260"),
            configuration: .galleryThumbnail(size: CGSize(width: 220, height: 140))
        ) {
            JchuNetworkImagePlaceholder()
        } failure: {
            JchuNetworkImageErrorView()
        }
        .frame(width: 220, height: 140)

        JchuNetworkImage(
            url: nil,
            configuration: .galleryThumbnail(size: CGSize(width: 220, height: 140))
        ) {
            JchuNetworkImagePlaceholder()
        } failure: {
            JchuNetworkImageErrorView()
        }
        .frame(width: 220, height: 140)
    }
    .padding()
}

public extension JchuNetworkImage where Placeholder == JchuNetworkImagePlaceholder, Failure == JchuNetworkImageErrorView {
    init(
        url: URL?,
        contentMode: SwiftUI.ContentMode = .fill,
        cornerRadius: CGFloat = 0,
        fadeDuration: TimeInterval = 0.2,
        targetSize: CGSize? = nil,
        onLoadingStateChange: ((Bool) -> Void)? = nil
    ) {
        self.init(
            url: url,
            configuration: JchuNetworkImageConfiguration(
                contentMode: contentMode,
                cornerRadius: cornerRadius,
                fadeDuration: fadeDuration,
                targetSize: targetSize
            ),
            onLoadingStateChange: onLoadingStateChange,
            placeholder: { JchuNetworkImagePlaceholder() },
            failure: { JchuNetworkImageErrorView() }
        )
    }

    init(
        urlString: String?,
        contentMode: SwiftUI.ContentMode = .fill,
        cornerRadius: CGFloat = 0,
        fadeDuration: TimeInterval = 0.2,
        targetSize: CGSize? = nil,
        onLoadingStateChange: ((Bool) -> Void)? = nil
    ) {
        self.init(
            url: urlString.flatMap(URL.init(string:)),
            contentMode: contentMode,
            cornerRadius: cornerRadius,
            fadeDuration: fadeDuration,
            targetSize: targetSize,
            onLoadingStateChange: onLoadingStateChange
        )
    }

    init(
        urlString: String?,
        configuration: JchuNetworkImageConfiguration,
        onLoadingStateChange: ((Bool) -> Void)? = nil
    ) {
        self.init(
            url: urlString.flatMap(URL.init(string:)),
            configuration: configuration,
            onLoadingStateChange: onLoadingStateChange,
            placeholder: { JchuNetworkImagePlaceholder() },
            failure: { JchuNetworkImageErrorView() }
        )
    }
}
