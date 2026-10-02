import JchuComponentsNavigation
import SwiftUI

@Screen(graph: "Catalog", route: "NavigationCodegen")
struct NavigationCodegenCatalogScreen: View {
    var body: some View {
        NavigationCodegenHomeScreen()
    }
}

@Screen(graph: "NavigationCodegen", route: "Home")
struct NavigationCodegenHomeScreen: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                Text("GENERATED NAVIGATION")
                    .font(.caption.weight(.semibold))
                    .tracking(1.4)
                    .foregroundStyle(.secondary)

                Text("One setup point")
                    .font(.largeTitle.bold())

                Text("Each @Screen contributes a case to its graph enum. The plugin generates the route switch and registers every graph on the shared root NavigationStack.")
                    .font(.body)
                    .foregroundStyle(.secondary)

                NavigationCodegenRouteCard(
                    title: "Route without arguments",
                    detail: "Use NavigationCodegenRoutes.simple with the shared root stack.",
                    route: NavigationCodegenRoutes.simple
                )

                NavigationCodegenRouteCard(
                    title: "Route with arguments",
                    detail: "The generated enum case carries both typed values.",
                    route: NavigationCodegenRoutes.details(
                        itemID: 42,
                        title: "Metro line details"
                    )
                )

                NavigationCodegenRouteCard(
                    title: "Route arguments into a ViewModel",
                    detail: "The generated screen initializer forwards two named route values into its ViewModel factory.",
                    route: NavigationCodegenRoutes.viewModel(
                        itemID: 42,
                        title: "Metro line details"
                    )
                )

                NavigationCodegenRouteCard(
                    title: "A second graph",
                    detail: "This route belongs to another graph but uses the same generated registration.",
                    route: AdditionalRoutes.sample
                )
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(20)
        }
        .navigationTitle("Navigation")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar(.visible, for: .navigationBar)
    }
}

@Screen(graph: "NavigationCodegen", route: "Simple")
struct NavigationCodegenSimpleScreen: View {
    var body: some View {
        NavigationCodegenDestinationCard(
            eyebrow: "NO ARGUMENTS",
            title: "Simple route",
            detail: "This screen was opened with a generated route that has no stored arguments."
        )
        .navigationTitle("Simple route")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar(.visible, for: .navigationBar)
    }
}

@Screen(graph: "NavigationCodegen", route: "Details")
struct NavigationCodegenDetailsScreen: View {
    @RouteArgument let itemID: Int
    @RouteArgument let title: String

    var body: some View {
        NavigationCodegenDestinationCard(
            eyebrow: "ROUTE ARGUMENTS",
            title: title,
            detail: "The generated route delivered itemID = \(itemID) and title = \(title)."
        )
        .navigationTitle("Details")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar(.visible, for: .navigationBar)
    }
}

@Screen(graph: "NavigationCodegen", route: "ViewModel")
struct NavigationCodegenViewModelScreen: View {
    @RouteArgument let itemID: Int
    @RouteArgument let title: String

    @ScreenKoinViewModel
    @State private var viewModel: NavigationCodegenViewModel

    var body: some View {
        NavigationCodegenDestinationCard(
            eyebrow: "GENERATED VIEWMODEL INIT",
            title: viewModel.title,
            detail: "The factory received itemID = \(viewModel.itemID) and title as named route arguments."
        )
        .navigationTitle("ViewModel arguments")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar(.visible, for: .navigationBar)
    }
}

private struct NavigationCodegenViewModel {
    let itemID: Int
    let title: String
}

private enum KoinWrapper {
    static func navigationCodegenForScreen(
        itemID: Int,
        title: String
    ) -> NavigationCodegenViewModel {
        NavigationCodegenViewModel(itemID: itemID, title: title)
    }
}

@Screen(graph: "Additional", route: "Sample")
struct NavigationCodegenAdditionalGraphScreen: View {
    var body: some View {
        NavigationCodegenDestinationCard(
            eyebrow: "ADDITIONAL GRAPH",
            title: "One generated registry",
            detail: "This case is generated from a different graph and is registered by the same root modifier."
        )
        .navigationTitle("Additional graph")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar(.visible, for: .navigationBar)
    }
}

private struct NavigationCodegenRouteCard<Route: Hashable>: View {
    let title: String
    let detail: String
    let route: Route

    var body: some View {
        NavigationLink(value: route) {
            HStack(spacing: 16) {
                VStack(alignment: .leading, spacing: 6) {
                    Text(title)
                        .font(.headline)
                        .foregroundStyle(.primary)

                    Text(detail)
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                        .fixedSize(horizontal: false, vertical: true)
                }

                Spacer(minLength: 0)

                Image(systemName: "arrow.right")
                    .font(.headline)
                    .foregroundStyle(.tint)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(18)
            .background(
                Color.secondary.opacity(0.12),
                in: RoundedRectangle(cornerRadius: 20, style: .continuous)
            )
        }
        .buttonStyle(.plain)
    }
}

private struct NavigationCodegenDestinationCard: View {
    let eyebrow: String
    let title: String
    let detail: String

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(eyebrow)
                .font(.caption.weight(.semibold))
                .tracking(1.4)
                .foregroundStyle(.secondary)

            Text(title)
                .font(.largeTitle.bold())

            Text(detail)
                .font(.body)
                .foregroundStyle(.secondary)

            Label("Destination resolved", systemImage: "checkmark.circle.fill")
                .font(.subheadline.weight(.medium))
                .foregroundStyle(.green)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(24)
        .background(
            Color.secondary.opacity(0.12),
            in: RoundedRectangle(cornerRadius: 24, style: .continuous)
        )
        .padding(20)
    }
}
