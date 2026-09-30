import SwiftDiagnostics
import SwiftSyntax
import SwiftSyntaxBuilder
import SwiftSyntaxMacros

public struct ScreenMacro: PeerMacro {
    public static func expansion(
        of node: AttributeSyntax,
        providingPeersOf declaration: some DeclSyntaxProtocol,
        in context: some MacroExpansionContext
    ) throws -> [DeclSyntax] {
        guard let screen = declaration.as(StructDeclSyntax.self) else {
            diagnose("@Screen can only be applied to a SwiftUI View struct.", at: declaration, in: context)
            return []
        }

        let arguments = argumentList(from: node)
        let graph = stringArgument("graph", from: arguments) ?? ""
        let route = stringArgument("route", from: arguments)
        let routeName = route.flatMap { $0.isEmpty ? nil : $0 } ?? screen.name.text
        let argumentNames = stringArrayArgument("arguments", from: arguments) ?? []

        guard !graph.isEmpty else {
            diagnose("@Screen requires a non-empty graph name.", at: node, in: context)
            return []
        }

        guard Set(argumentNames).count == argumentNames.count else {
            diagnose("@Screen arguments must not contain duplicate names.", at: node, in: context)
            return []
        }

        let storedProperties = storedProperties(in: screen)
        let fields = argumentNames.compactMap { name -> RouteField? in
            guard let type = storedProperties[name] else {
                diagnose("@Screen argument '\(name)' must match an unwrapped stored property with an explicit type.", at: screen, in: context)
                return nil
            }
            return RouteField(name: name, type: type)
        }

        guard fields.count == argumentNames.count else { return [] }

        let screenName = screen.name.text
        let routeTypeName = "\(screenName)Route"
        let isPublic = screen.modifiers.contains { $0.name.text == "public" || $0.name.text == "open" }
        let access = isPublic ? "public " : ""
        let properties = fields.map { "    \(access)let \($0.name): \($0.type)" }.joined(separator: "\n")
        let initializerParameters = fields
            .map { "\($0.name): \($0.type)" }
            .joined(separator: ", ")
        let initializerAssignments = fields
            .map { "        self.\($0.name) = \($0.name)" }
            .joined(separator: "\n")
        let destinationArguments = fields
            .map { "\($0.name): self.\($0.name)" }
            .joined(separator: ", ")
        let graphLiteral = String(reflecting: graph)
        let routeLiteral = String(reflecting: routeName)

        let generated = """
        \(access)struct \(routeTypeName): JchuScreenRoute {
        \(properties.isEmpty ? "" : "\(properties)\n")
            \(access)init(\(initializerParameters)) {
        \(initializerAssignments.isEmpty ? "" : "\(initializerAssignments)\n")
            }

            \(access)static let graph = \(graphLiteral)
            \(access)static let name = \(routeLiteral)

            @MainActor
            \(access)func destination() -> \(screenName) {
                \(screenName)(\(destinationArguments))
            }
        }
        """

        return [DeclSyntax(stringLiteral: generated)]
    }

    private static func argumentList(from node: AttributeSyntax) -> LabeledExprListSyntax? {
        guard case let .argumentList(arguments) = node.arguments else { return nil }
        return arguments
    }

    private static func stringArgument(
        _ name: String,
        from arguments: LabeledExprListSyntax?
    ) -> String? {
        guard let expression = arguments?.first(where: { $0.label?.text == name })?.expression,
              let literal = expression.as(StringLiteralExprSyntax.self)
        else { return nil }
        return literal.representedLiteralValue
    }

    private static func stringArrayArgument(
        _ name: String,
        from arguments: LabeledExprListSyntax?
    ) -> [String]? {
        guard let expression = arguments?.first(where: { $0.label?.text == name })?.expression,
              let array = expression.as(ArrayExprSyntax.self)
        else { return nil }

        return array.elements.compactMap { element in
            element.expression.as(StringLiteralExprSyntax.self)?.representedLiteralValue
        }
    }

    private static func storedProperties(in declaration: StructDeclSyntax) -> [String: String] {
        var properties: [String: String] = [:]

        for member in declaration.memberBlock.members {
            guard let variable = member.decl.as(VariableDeclSyntax.self),
                  variable.attributes.isEmpty,
                  !variable.modifiers.contains(where: { $0.name.text == "static" || $0.name.text == "class" })
            else { continue }

            for binding in variable.bindings {
                guard binding.accessorBlock == nil,
                      let name = binding.pattern.as(IdentifierPatternSyntax.self)?.identifier.text,
                      let type = binding.typeAnnotation?.type.trimmedDescription
                else { continue }
                properties[name] = type
            }
        }

        return properties
    }

    private static func diagnose(
        _ message: String,
        at node: some SyntaxProtocol,
        in context: some MacroExpansionContext
    ) {
        context.diagnose(
            Diagnostic(
                node: Syntax(node),
                message: ScreenMacroDiagnostic(message: message)
            )
        )
    }
}

private struct RouteField {
    let name: String
    let type: String
}

private struct ScreenMacroDiagnostic: DiagnosticMessage {
    let message: String

    var diagnosticID: MessageID {
        MessageID(domain: "JchuComponentsNavigation", id: message)
    }

    var severity: DiagnosticSeverity { .error }
}
