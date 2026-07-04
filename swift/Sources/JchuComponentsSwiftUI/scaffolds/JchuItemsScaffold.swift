import SwiftUI

public struct JchuScaffoldTabItem: Identifiable, Hashable {
    public let id: String
    public let title: LocalizedStringKey
    public let systemImage: String

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
