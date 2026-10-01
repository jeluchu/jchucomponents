import SwiftDiagnostics
import SwiftSyntax
import SwiftSyntaxMacros

public struct RouteArgumentMacro: PeerMacro {
    public static func expansion(
        of node: AttributeSyntax,
        providingPeersOf declaration: some DeclSyntaxProtocol,
        in context: some MacroExpansionContext
    ) throws -> [DeclSyntax] {
        guard declaration.is(VariableDeclSyntax.self) else {
            context.diagnose(
                Diagnostic(
                    node: Syntax(node),
                    message: RouteArgumentMacroDiagnostic()
                )
            )
            return []
        }

        return []
    }
}

private struct RouteArgumentMacroDiagnostic: DiagnosticMessage {
    var message: String { "@RouteArgument can only be applied to a stored property." }
    var diagnosticID: MessageID {
        MessageID(domain: "JchuComponentsNavigation", id: "route-argument-property-only")
    }
    var severity: DiagnosticSeverity { .error }
}
