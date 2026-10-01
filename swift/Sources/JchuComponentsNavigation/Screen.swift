import SwiftUI

/// A typed route generated for a SwiftUI screen or navigation graph.
public protocol JchuNavigationRoute: Hashable {
    associatedtype Destination: View

    @MainActor
    func destination() -> Destination
}

/// A typed route generated for a SwiftUI screen annotated with `@Screen`.
public protocol JchuScreenRoute: JchuNavigationRoute {
    static var graph: String { get }
    static var name: String { get }
}

/// Marks a SwiftUI screen and generates a typed, hashable route for its arguments.
@attached(peer, names: suffixed(Route))
@attached(member, names: named(init))
public macro Screen(
    graph: String,
    route: String = "",
    arguments: [String] = []
) = #externalMacro(
    module: "JchuComponentsNavigationMacros",
    type: "ScreenMacro"
)

/// Marks a stored property as an associated value of its screen's generated route.
@attached(peer, names: arbitrary)
public macro RouteArgument() = #externalMacro(
    module: "JchuComponentsNavigationMacros",
    type: "RouteArgumentMacro"
)

/// Initializes a KMP ViewModel property from a typed factory while generating the screen initializer.
///
/// The factory can receive route arguments by name. If `arguments` is omitted, all `@RouteArgument`
/// properties are passed. Provide an explicit subset, including `[]`, when the ViewModel needs fewer
/// values than the screen route.
@attached(peer, names: arbitrary)
public macro ScreenKoinViewModel(
    factory: Any,
    arguments: [String] = []
) = #externalMacro(
    module: "JchuComponentsNavigationMacros",
    type: "ScreenKoinViewModelMacro"
)

public extension View {
    /// Registers the generated route and its screen as a SwiftUI navigation destination.
    @MainActor
    func jchuNavigationDestination<Route: JchuNavigationRoute>(
        for route: Route.Type
    ) -> some View {
        navigationDestination(for: route) { value in
            value.destination()
        }
    }
}
