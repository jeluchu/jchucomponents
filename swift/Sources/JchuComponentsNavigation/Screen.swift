import SwiftUI

/// A typed route generated for a SwiftUI screen annotated with `@Screen`.
public protocol JchuScreenRoute: Hashable {
    associatedtype Destination: View

    static var graph: String { get }
    static var name: String { get }

    @MainActor
    func destination() -> Destination
}

/// Marks a SwiftUI screen and generates a typed, hashable route for its arguments.
@attached(peer, names: arbitrary)
public macro Screen(
    graph: String,
    route: String = "",
    arguments: [String] = []
) = #externalMacro(
    module: "JchuComponentsNavigationMacros",
    type: "ScreenMacro"
)

public extension View {
    /// Registers the generated route and its screen as a SwiftUI navigation destination.
    @MainActor
    func jchuNavigationDestination<Route: JchuScreenRoute>(
        for route: Route.Type
    ) -> some View {
        navigationDestination(for: route) { value in
            value.destination()
        }
    }
}
