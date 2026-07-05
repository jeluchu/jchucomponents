import SwiftUI

/// A stable identifier, localized title and SF Symbol for scaffold tabs.
public struct JchuScaffoldTabItem: Identifiable, Hashable {
    public let id: String
    public let title: LocalizedStringKey
    public let systemImage: String

    /// Creates a scaffold tab item.
    public init(
        id: String,
        title: LocalizedStringKey,
        systemImage: String
    ) {
        self.id = id
        self.title = title
        self.systemImage = systemImage
    }

    public static func == (lhs: JchuScaffoldTabItem, rhs: JchuScaffoldTabItem) -> Bool {
        lhs.id == rhs.id
    }

    public func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
}
