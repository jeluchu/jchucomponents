import JchuComponentsNavigation
import SwiftUI

struct NavigationCodegenCatalogScreen: View {
    var body: some View {
        NavigationStack {
            NavigationCodegenHomeScreen()
                .jchuNavigationCodegenDestinations()
        }
        .navigationTitle("Navigation Codegen")
        .navigationBarTitleDisplayMode(.inline)
    }
}

@Screen(graph: "NavigationCodegen", route: "Home")
struct NavigationCodegenHomeScreen: View {
    var body: some View {
        NavigationCodegenPage(
            eyebrow: "GENERATED ROUTES",
            title: "One screen annotation",
            description: "@Screen creates a typed route and its destination builder from the view declaration.",
            actionTitle: "Open mobility",
            route: NavigationCodegenCategoryScreenRoute(category: "Madrid Mobility")
        )
    }
}

@Screen(
    graph: "NavigationCodegen",
    route: "Category",
    arguments: ["category"]
)
struct NavigationCodegenCategoryScreen: View {
    let category: String

    var body: some View {
        NavigationCodegenPage(
            eyebrow: "\(category.uppercased())",
            title: "Choose a route",
            description: "The category value is carried by the generated Hashable route.",
            actionTitle: "Open line details",
            route: NavigationCodegenDetailsScreenRoute(
                itemID: 42,
                title: "Metro line details"
            )
        )
    }
}

@Screen(
    graph: "NavigationCodegen",
    route: "Details",
    arguments: ["itemID", "title"]
)
struct NavigationCodegenDetailsScreen: View {
    let itemID: Int
    let title: String

    var body: some View {
        VStack(alignment: .leading, spacing: 18) {
            Text("DETAILS")
                .font(.caption.weight(.semibold))
                .tracking(1.4)
                .foregroundStyle(.secondary)

            Text(title)
                .font(.largeTitle.bold())

            Label("Route argument: \(itemID)", systemImage: "point.topleft.down.to.point.bottomright.curvepath")
                .font(.body)
                .foregroundStyle(.secondary)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(24)
        .background(Color(uiColor: .secondarySystemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 24, style: .continuous))
        .padding(20)
        .navigationTitle("Details")
        .navigationBarTitleDisplayMode(.inline)
    }
}

private struct NavigationCodegenPage<Route: Hashable>: View {
    let eyebrow: String
    let title: String
    let description: String
    let actionTitle: String
    let route: Route

    var body: some View {
        VStack(alignment: .leading, spacing: 18) {
            Text(eyebrow)
                .font(.caption.weight(.semibold))
                .tracking(1.4)
                .foregroundStyle(.secondary)

            Text(title)
                .font(.largeTitle.bold())

            Text(description)
                .font(.body)
                .foregroundStyle(.secondary)

            NavigationLink(value: route) {
                Label(actionTitle, systemImage: "arrow.right")
                    .font(.headline)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 16)
            }
            .buttonStyle(.borderedProminent)
            .padding(.top, 8)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(24)
        .background(Color(uiColor: .secondarySystemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 24, style: .continuous))
        .padding(20)
    }
}
