import SwiftUI

private let scaffoldPreviewItems = ["Villagers", "Furniture", "Fossils", "Music"]

#Preview("JchuScaffold") {
    JchuScaffold(
        topBar: {
            JchuScaffoldTopBar("Scaffold")
        }
    ) {
        JchuPreviewContent("Base scaffold")
    }
    .jchuTheme(.standard)
}

#Preview("JchuScrollableScaffold") {
    JchuScrollableScaffold("Scrollable") {
        VStack(alignment: .leading, spacing: 10) {
            ForEach(scaffoldPreviewItems, id: \.self) { item in
                JchuPreviewCard(item)
            }
        }
    }
    .jchuTheme(.standard)
}

#Preview("JchuStateScaffold") {
    JchuStateScaffold(
        "State",
        isLoading: false,
        isEmpty: false
    ) {
        JchuPreviewContent("Loaded state")
    }
    .jchuTheme(.standard)
}

#Preview("JchuDetailsScaffold") {
    JchuDetailsScaffold(
        "Details",
        details: "A reusable detail scaffold"
    ) { value in
        VStack(alignment: .leading, spacing: 10) {
            JchuPreviewCard(value)
            JchuPreviewCard("Extra detail")
        }
    }
    .jchuTheme(.standard)
}

#Preview("JchuPurchaseElementsScaffold") {
    JchuPurchaseElementsScaffoldHost()
        .jchuTheme(.standard)
}

#Preview("JchuPurchaseTabItemsScaffold") {
    JchuPurchaseTabItemsScaffoldHost()
        .jchuTheme(.standard)
}

#Preview("JchuShareScaffold") {
    JchuShareScaffold(
        "Share",
        config: JchuAppColorThemes.avatarCreator.toShareScaffoldConfig(),
        onShare: {},
        onDownload: {}
    ) {
        JchuPreviewContent("Shareable content")
    }
    .jchuTheme(.standard)
}

#Preview("JchuSettingsScaffold") {
    JchuSettingsScaffold("Settings") {
        VStack(alignment: .leading, spacing: 10) {
            JchuPreviewCard("Profile")
            JchuPreviewCard("Preferences")
            JchuPreviewCard("About")
        }
    }
    .jchuTheme(.standard)
}

private struct JchuPurchaseElementsScaffoldHost: View {
    @State private var query = ""

    var body: some View {
        JchuPurchaseElementsScaffold(
            "Purchase Items",
            items: scaffoldPreviewItems,
            isLoading: false,
            id: \.self,
            config: JchuAppColorThemes.nooksCranny.toPurchaseScaffoldConfig(query: $query)
        ) { type, item in
            JchuPreviewCard("\(item) \(String(describing: type))")
        }
    }
}

private struct JchuPurchaseTabItemsScaffoldHost: View {
    @State private var selectedTabID = "one"
    @State private var query = ""

    var body: some View {
        JchuPurchaseTabItemsScaffold(
            "Purchase Tabs",
            tabs: previewTabs,
            selectedTabID: $selectedTabID,
            isLoading: false,
            config: JchuAppColorThemes.fossils.toPurchaseTabScaffoldConfig(query: $query)
        ) {
            TabView(selection: $selectedTabID) {
                ForEach(previewTabs) { tab in
                    JchuPreviewContent("Selected \(tab.id)")
                        .tag(tab.id)
                }
            }
            .tabViewStyle(.page(indexDisplayMode: .never))
        }
    }
}

private var previewTabs: [JchuScaffoldTabItem] {
    [
        JchuScaffoldTabItem(id: "one", title: "One", systemImage: "circle"),
        JchuScaffoldTabItem(id: "two", title: "Two", systemImage: "square"),
        JchuScaffoldTabItem(id: "three", title: "Three", systemImage: "triangle")
    ]
}

private struct JchuPreviewContent: View {
    let title: String

    init(_ title: String) {
        self.title = title
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text(title)
                .font(.headline)
            JchuPreviewCard("Preview row")
        }
        .padding()
    }
}

private struct JchuPreviewCard: View {
    let text: String

    init(_ text: String) {
        self.text = text
    }

    var body: some View {
        Text(text)
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding()
            .background(.fill.quaternary, in: .rect(cornerRadius: 12))
    }
}
