import SwiftCompilerPlugin
import SwiftSyntaxMacros

@main
struct JchuComponentsNavigationPlugin: CompilerPlugin {
    let providingMacros: [Macro.Type] = [
        ScreenMacro.self,
        RouteArgumentMacro.self,
        ScreenKoinViewModelMacro.self
    ]
}
