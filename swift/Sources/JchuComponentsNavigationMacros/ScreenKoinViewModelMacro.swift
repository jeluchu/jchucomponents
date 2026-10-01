import SwiftDiagnostics
import SwiftSyntax
import SwiftSyntaxMacros

public struct ScreenKoinViewModelMacro: PeerMacro {
    public static func expansion(
        of node: AttributeSyntax,
        providingPeersOf declaration: some DeclSyntaxProtocol,
        in context: some MacroExpansionContext
    ) throws -> [DeclSyntax] {
        guard declaration.is(VariableDeclSyntax.self) else {
            context.diagnose(
                Diagnostic(
                    node: Syntax(node),
                    message: ScreenKoinViewModelDiagnostic()
                )
            )
            return []
        }
        return []
    }
}

private struct ScreenKoinViewModelDiagnostic: DiagnosticMessage {
    var message: String { "@ScreenKoinViewModel can only be applied to a stored ViewModel property." }
    var diagnosticID: MessageID {
        MessageID(domain: "JchuComponentsNavigation", id: "ScreenKoinViewModelTarget")
    }
    var severity: DiagnosticSeverity { .error }
}
