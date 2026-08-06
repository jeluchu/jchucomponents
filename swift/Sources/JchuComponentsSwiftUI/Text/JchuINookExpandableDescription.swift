import SwiftUI

/// iOS defaults copied from iNook's SwiftUI implementation.
public struct JchuExpandableDescriptionConfig {
    public let defaultExpanded: Bool
    public let maxCollapsedLines: Int
    public let minCharactersForExpansion: Int
    public let animationDuration: Double
    public let enableHapticFeedback: Bool
    public let textAlign: TextAlignment
    public let imageHeight: CGFloat

    public init(
        defaultExpanded: Bool = false,
        maxCollapsedLines: Int = 3,
        minCharactersForExpansion: Int = 100,
        animationDuration: Double = 0.35,
        enableHapticFeedback: Bool = true,
        textAlign: TextAlignment = .leading,
        imageHeight: CGFloat = 180
    ) {
        precondition(maxCollapsedLines > 0, "maxCollapsedLines must be greater than zero")
        precondition(minCharactersForExpansion >= 0, "minCharactersForExpansion must not be negative")
        precondition(animationDuration >= 0, "animationDuration must not be negative")
        precondition(imageHeight >= 0, "imageHeight must not be negative")
        self.defaultExpanded = defaultExpanded
        self.maxCollapsedLines = maxCollapsedLines
        self.minCharactersForExpansion = minCharactersForExpansion
        self.animationDuration = animationDuration
        self.enableHapticFeedback = enableHapticFeedback
        self.textAlign = textAlign
        self.imageHeight = imageHeight
    }
}

public struct JchuExpandableDescriptionColors {
    public let contentColor: Color
    public let titleColor: Color
    public let borderColor: Color
    public let containerColor: Color
    public let gradientColor: Color
    public let iconColor: Color

    public init(
        contentColor: Color = .primary,
        titleColor: Color = .primary,
        containerColor: Color = Color(uiColor: .systemBackground),
        borderColor: Color = Color(uiColor: .systemBackground),
        gradientColor: Color = Color(uiColor: .systemBackground),
        iconColor: Color = .secondary
    ) {
        self.contentColor = contentColor
        self.titleColor = titleColor
        self.containerColor = containerColor
        self.borderColor = borderColor
        self.gradientColor = gradientColor
        self.iconColor = iconColor
    }

    public static func `default`() -> Self { Self() }

    public static func dark() -> Self {
        Self(
            contentColor: Color(red: 224 / 255, green: 224 / 255, blue: 224 / 255),
            titleColor: .white,
            containerColor: Color(red: 30 / 255, green: 30 / 255, blue: 30 / 255),
            borderColor: .white,
            gradientColor: Color(red: 30 / 255, green: 30 / 255, blue: 30 / 255),
            iconColor: Color(red: 189 / 255, green: 189 / 255, blue: 189 / 255)
        )
    }
}

@MainActor
private final class JchuExpandableDescriptionState: ObservableObject {
    @Published var expanded: Bool
    private let onStateChange: ((Bool) -> Void)?

    init(defaultExpanded: Bool, onStateChange: ((Bool) -> Void)?) {
        expanded = defaultExpanded
        self.onStateChange = onStateChange
    }

    func toggle() {
        expanded.toggle()
        onStateChange?(expanded)
    }
}

public struct JchuExpandableDescription: View {
    private let image: String
    private let description: String?
    private let title: String
    private let config: JchuExpandableDescriptionConfig
    private let colors: JchuExpandableDescriptionColors
    @StateObject private var state: JchuExpandableDescriptionState

    public init(
        image: String,
        description: String?,
        title: String = "Description",
        config: JchuExpandableDescriptionConfig = .init(),
        colors: JchuExpandableDescriptionColors = .default(),
        onExpandedChange: ((Bool) -> Void)? = nil
    ) {
        self.image = image
        self.description = description
        self.title = title
        self.config = config
        self.colors = colors
        _state = StateObject(
            wrappedValue: JchuExpandableDescriptionState(
                defaultExpanded: config.defaultExpanded,
                onStateChange: onExpandedChange
            )
        )
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            JchuExpandableTitle(title, colors: colors)
            JchuEnhancedSummary(
                expanded: state.expanded,
                text: jchuProcessDescription(description),
                config: config,
                colors: colors
            )
            .onTapGesture(perform: handleTap)
            JchuFixedHeightNetworkImage(
                url: image,
                height: config.imageHeight,
                backgroundColor: colors.containerColor.opacity(0.2)
            )
        }
        .padding(15)
        .background(colors.containerColor, in: RoundedRectangle(cornerRadius: 15))
        .overlay(RoundedRectangle(cornerRadius: 15).stroke(colors.borderColor, lineWidth: 1))
    }

    private func handleTap() {
        jchuExpandableTap(config: config, state: state)
    }
}

public struct JchuExpandableDescriptionGallery: View {
    private let description: String?
    private let images: [String]
    private let title: String
    private let config: JchuExpandableDescriptionConfig
    private let colors: JchuExpandableDescriptionColors
    private let onImageClick: ((String, Int) -> Void)?
    @StateObject private var state: JchuExpandableDescriptionState

    public init(
        description: String?,
        images: [String],
        title: String = "Description",
        config: JchuExpandableDescriptionConfig = .init(),
        colors: JchuExpandableDescriptionColors = .default(),
        onExpandedChange: ((Bool) -> Void)? = nil,
        onImageClick: ((String, Int) -> Void)? = nil
    ) {
        self.description = description
        self.images = images
        self.title = title
        self.config = config
        self.colors = colors
        self.onImageClick = onImageClick
        _state = StateObject(
            wrappedValue: JchuExpandableDescriptionState(
                defaultExpanded: config.defaultExpanded,
                onStateChange: onExpandedChange
            )
        )
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text(title)
                .foregroundColor(colors.titleColor)
                .font(.custom("nintendoP_Humming-E_002pr", size: 14))
                .padding(.top, 15)
                .padding(.horizontal, 15)
            JchuEnhancedSummary(
                expanded: state.expanded,
                text: jchuProcessDescription(description),
                config: config,
                colors: colors
            )
            .padding(.horizontal, 15)
            .onTapGesture { jchuExpandableTap(config: config, state: state) }
            if !images.isEmpty {
                JchuExpandableImageGallery(images: images, onImageClick: onImageClick)
            }
        }
        .background(colors.containerColor, in: RoundedRectangle(cornerRadius: 15))
        .overlay(RoundedRectangle(cornerRadius: 15).stroke(colors.borderColor, lineWidth: 1))
    }
}

public struct JchuExpandableDescriptionAction<Destination: View>: View {
    private let image: String
    private let description: String?
    private let action: String
    private let title: String
    private let config: JchuExpandableDescriptionConfig
    private let colors: JchuExpandableDescriptionColors
    private let destination: () -> Destination
    @StateObject private var state: JchuExpandableDescriptionState

    public init(
        image: String,
        description: String?,
        action: String = "Accessories",
        title: String = "Description",
        config: JchuExpandableDescriptionConfig = .init(),
        colors: JchuExpandableDescriptionColors = .default(),
        onExpandedChange: ((Bool) -> Void)? = nil,
        @ViewBuilder destination: @escaping () -> Destination
    ) {
        self.image = image
        self.description = description
        self.action = action
        self.title = title
        self.config = config
        self.colors = colors
        self.destination = destination
        _state = StateObject(
            wrappedValue: JchuExpandableDescriptionState(
                defaultExpanded: config.defaultExpanded,
                onStateChange: onExpandedChange
            )
        )
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            JchuExpandableTitle(title, colors: colors)
            JchuEnhancedSummary(
                expanded: state.expanded,
                text: jchuProcessDescription(description),
                config: config,
                colors: colors
            )
            .onTapGesture { jchuExpandableTap(config: config, state: state) }
            JchuFixedHeightNetworkImage(
                url: image,
                height: config.imageHeight,
                backgroundColor: colors.containerColor.opacity(0.2)
            )
            NavigationLink(destination: JchuExpandableLazyView(build: destination)) {
                HStack(spacing: 10) {
                    Text(action)
                        .font(.custom("nintendoP_Humming-E_002pr", size: 14))
                        .foregroundColor(colors.contentColor)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Image(systemName: "chevron.right")
                        .resizable()
                        .renderingMode(.template)
                        .foregroundColor(colors.contentColor)
                        .frame(width: 20, height: 20)
                }
                .padding(15)
                .frame(maxWidth: .infinity)
                .background(
                    RoundedRectangle(cornerRadius: 10)
                        .fill(colors.contentColor.opacity(0.1))
                )
            }
            .buttonStyle(.plain)
        }
        .padding(15)
        .background(colors.containerColor, in: RoundedRectangle(cornerRadius: 15))
        .overlay(RoundedRectangle(cornerRadius: 15).stroke(colors.borderColor, lineWidth: 1))
    }
}

private struct JchuExpandableLazyView<Content: View>: View {
    let build: () -> Content

    var body: some View {
        build()
    }
}

public struct JchuSimpleExpandableText: View {
    private let description: String?
    private let config: JchuExpandableDescriptionConfig
    private let colors: JchuExpandableDescriptionColors
    @StateObject private var state: JchuExpandableDescriptionState

    public init(
        description: String?,
        config: JchuExpandableDescriptionConfig = .init(),
        colors: JchuExpandableDescriptionColors = .default(),
        onExpandedChange: ((Bool) -> Void)? = nil
    ) {
        self.description = description
        self.config = config
        self.colors = colors
        _state = StateObject(
            wrappedValue: JchuExpandableDescriptionState(
                defaultExpanded: config.defaultExpanded,
                onStateChange: onExpandedChange
            )
        )
    }

    public var body: some View {
        JchuEnhancedSummary(
            expanded: state.expanded,
            text: jchuProcessDescription(description),
            config: config,
            colors: colors
        )
        .onTapGesture { jchuExpandableTap(config: config, state: state) }
        .padding(15)
        .background(colors.containerColor, in: RoundedRectangle(cornerRadius: 15))
        .overlay(RoundedRectangle(cornerRadius: 15).stroke(colors.borderColor, lineWidth: 1))
    }
}

private struct JchuExpandableTitle: View {
    let title: String
    let colors: JchuExpandableDescriptionColors

    init(_ title: String, colors: JchuExpandableDescriptionColors) {
        self.title = title
        self.colors = colors
    }

    var body: some View {
        Text(title)
            .foregroundColor(colors.titleColor)
            .font(.custom("nintendoP_Humming-E_002pr", size: 14))
            .frame(maxWidth: .infinity, alignment: .leading)
    }
}

private struct JchuEnhancedSummary: View {
    let expanded: Bool
    let text: String
    let config: JchuExpandableDescriptionConfig
    let colors: JchuExpandableDescriptionColors

    private var needsExpansion: Bool { text.count > config.minCharactersForExpansion }

    var body: some View {
        ZStack(alignment: .bottom) {
            Text(text)
                .font(.custom("nintendoP_Humming-E_002pr", size: 12))
                .multilineTextAlignment(config.textAlign)
                .foregroundColor(colors.contentColor.opacity(expanded ? 0.83 : 0.7))
                .lineLimit(expanded ? nil : config.maxCollapsedLines)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.bottom, needsExpansion ? 36 : 0)
                .textSelection(.enabled)
            if needsExpansion {
                ZStack(alignment: .bottom) {
                    LinearGradient(
                        colors: [.clear, colors.gradientColor.opacity(expanded ? 0 : 0.9)],
                        startPoint: .top,
                        endPoint: .bottom
                    )
                    .frame(height: 48)
                    Image(systemName: "chevron.down")
                        .resizable()
                        .renderingMode(.template)
                        .foregroundColor(colors.iconColor)
                        .frame(width: 20, height: 12)
                        .rotationEffect(.degrees(expanded ? 180 : 0))
                        .padding(.bottom, 4)
                }
                .frame(maxWidth: .infinity)
                .allowsHitTesting(false)
            }
        }
        .animation(.easeInOut(duration: config.animationDuration), value: expanded)
        .contentShape(Rectangle())
    }
}

private struct JchuFixedHeightNetworkImage: View {
    let url: String
    let height: CGFloat
    let backgroundColor: Color

    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 10).fill(backgroundColor)
            AsyncImage(url: URL(string: url)) { phase in
                switch phase {
                case .success(let image):
                    image.resizable().scaledToFill().transition(.opacity.animation(.easeIn(duration: 0.25)))
                case .failure:
                    Image(systemName: "photo")
                        .frame(width: 32, height: 32)
                        .foregroundColor(.secondary)
                case .empty:
                    ProgressView()
                @unknown default:
                    EmptyView()
                }
            }
            .clipShape(RoundedRectangle(cornerRadius: 10))
        }
        .frame(maxWidth: .infinity)
        .frame(height: height)
        .clipShape(RoundedRectangle(cornerRadius: 10))
    }
}

private struct JchuExpandableImageGallery: View {
    let images: [String]
    let onImageClick: ((String, Int) -> Void)?

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 10) {
                ForEach(Array(images.enumerated()), id: \.offset) { index, image in
                    AsyncImage(url: URL(string: image)) { phase in
                        switch phase {
                        case .success(let image):
                            image.resizable().aspectRatio(contentMode: .fill)
                        case .failure:
                            Color.secondary.opacity(0.2)
                                .overlay(Image(systemName: "photo").foregroundColor(.secondary))
                        default:
                            Color.secondary.opacity(0.1).overlay(ProgressView())
                        }
                    }
                    .frame(width: 100, height: 100)
                    .clipShape(RoundedRectangle(cornerRadius: 16))
                    .onTapGesture { onImageClick?(image, index) }
                }
            }
            .padding(.horizontal, 15)
            .padding(.bottom, 15)
        }
    }
}

private func jchuProcessDescription(_ description: String?) -> String {
    guard let description, !description.isEmpty else { return "" }
    return description
        .replacingOccurrences(of: "[\r\n]{2,}", with: "\n", options: .regularExpression)
        .trimmingCharacters(in: .whitespacesAndNewlines)
}

@MainActor
private func jchuExpandableTap(
    config: JchuExpandableDescriptionConfig,
    state: JchuExpandableDescriptionState
) {
    if config.enableHapticFeedback {
        UIImpactFeedbackGenerator(style: .medium).impactOccurred()
    }
    withAnimation(.easeInOut(duration: config.animationDuration)) {
        state.toggle()
    }
}
