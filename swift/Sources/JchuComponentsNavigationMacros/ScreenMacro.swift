import SwiftDiagnostics
import SwiftSyntax
import SwiftSyntaxBuilder
import SwiftSyntaxMacros

public struct ScreenMacro: PeerMacro, MemberMacro {
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
        let declaredArgumentNames = stringArrayArgument("arguments", from: arguments) ?? []
        let markedArgumentNames = routeArgumentNames(in: screen)
        let argumentNames = declaredArgumentNames + markedArgumentNames.filter {
            !declaredArgumentNames.contains($0)
        }

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

    private static func routeArgumentNames(in declaration: StructDeclSyntax) -> [String] {
        declaration.memberBlock.members.flatMap { member -> [String] in
            guard let variable = member.decl.as(VariableDeclSyntax.self),
                  variable.attributes.contains(where: isRouteArgumentAttribute)
            else { return [] }

            return variable.bindings.compactMap { binding in
                binding.pattern.as(IdentifierPatternSyntax.self)?.identifier.text
            }
        }
    }

    private static func isRouteArgumentAttribute(_ element: AttributeListSyntax.Element) -> Bool {
        guard let attribute = element.as(AttributeSyntax.self) else { return false }
        let name = attribute.attributeName.trimmedDescription
        return name == "RouteArgument" || name.hasSuffix(".RouteArgument")
    }

    private static func storedProperties(in declaration: StructDeclSyntax) -> [String: String] {
        var properties: [String: String] = [:]

        for member in declaration.memberBlock.members {
            guard let variable = member.decl.as(VariableDeclSyntax.self),
                  variable.attributes.allSatisfy(isRouteArgumentAttribute),
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

extension ScreenMacro {
    public static func expansion(
        of node: AttributeSyntax,
        providingMembersOf declaration: some DeclGroupSyntax,
        conformingTo protocols: [TypeSyntax],
        in context: some MacroExpansionContext
    ) throws -> [DeclSyntax] {
        guard let screen = declaration.as(StructDeclSyntax.self) else { return [] }
        let viewModelProperties = screen.memberBlock.members.compactMap { member -> VariableDeclSyntax? in
            guard let variable = member.decl.as(VariableDeclSyntax.self),
                  variable.attributes.contains(where: isScreenKoinViewModelAttribute)
            else { return nil }
            return variable
        }
        guard !viewModelProperties.isEmpty else { return [] }

        if screen.memberBlock.members.contains(where: { $0.decl.is(InitializerDeclSyntax.self) }) {
            diagnose(
                "Remove the handwritten initializer when using @ScreenKoinViewModel; @Screen generates it.",
                at: screen,
                in: context
            )
            return []
        }

        let declaredArgumentNames = stringArrayArgument(
            "arguments",
            from: argumentList(from: node)
        ) ?? []
        let markedArgumentNames = routeArgumentNames(in: screen)
        let argumentNames = declaredArgumentNames + markedArgumentNames.filter {
            !declaredArgumentNames.contains($0)
        }
        let propertyTypes = storedProperties(in: screen)
        let routeFields = argumentNames.compactMap { name -> RouteField? in
            guard let type = propertyTypes[name] else {
                diagnose(
                    "@RouteArgument '\(name)' must be a stored property with an explicit type.",
                    at: screen,
                    in: context
                )
                return nil
            }
            return RouteField(name: name, type: type)
        }
        guard routeFields.count == argumentNames.count else { return [] }

        var viewModelAssignments: [String] = []
        for variable in viewModelProperties {
            guard variable.bindings.count == 1,
                  let binding = variable.bindings.first,
                  let name = binding.pattern.as(IdentifierPatternSyntax.self)?.identifier.text,
                  binding.accessorBlock == nil
            else {
                diagnose(
                    "@ScreenKoinViewModel must be applied to one stored property at a time.",
                    at: variable,
                    in: context
                )
                return []
            }

            guard let annotationElement = variable.attributes.first(where: isScreenKoinViewModelAttribute),
                  let annotation = annotationElement.as(AttributeSyntax.self)
            else {
                diagnose("@ScreenKoinViewModel could not read its attribute.", at: variable, in: context)
                return []
            }

            let viewModelArguments = argumentList(from: annotation)
            let factoryExpression: String
            if let factory = viewModelArguments?.first(where: { $0.label?.text == "factory" })?.expression,
               !factory.is(NilLiteralExprSyntax.self) {
                factoryExpression = factory.trimmedDescription
            } else {
                guard let inferredFactory = inferredFactoryExpression(
                    for: variable,
                    binding: binding,
                    in: context
                ) else { return [] }
                factoryExpression = inferredFactory
            }

            let explicitArguments = viewModelArguments?.first(where: { $0.label?.text == "arguments" })
            let mappedArguments: [String]
            if explicitArguments == nil {
                mappedArguments = routeFields.map(\.name)
            } else if let parsed = stringArrayArgument("arguments", from: viewModelArguments) {
                mappedArguments = parsed
            } else {
                diagnose("@ScreenKoinViewModel arguments must be an array of string property names.", at: annotation, in: context)
                return []
            }

            guard Set(mappedArguments).count == mappedArguments.count else {
                diagnose("@ScreenKoinViewModel arguments must not contain duplicate names.", at: annotation, in: context)
                return []
            }
            let routeFieldNames = Set(routeFields.map(\.name))
            if let missing = mappedArguments.first(where: { !routeFieldNames.contains($0) }) {
                diagnose(
                    "@ScreenKoinViewModel argument '\(missing)' must be declared with @RouteArgument on this screen.",
                    at: annotation,
                    in: context
                )
                return []
            }

            let wrapperName = variable.attributes
                .compactMap { $0.as(AttributeSyntax.self) }
                .map { attribute in
                    attribute.attributeName.trimmedDescription
                        .split(separator: ".")
                        .last
                        .map(String.init) ?? attribute.attributeName.trimmedDescription
                }
                .first { $0 == "StateViewModel" || $0 == "ObservedViewModel" || $0 == "State" }
            guard let wrapper = wrapperName else {
                diagnose(
                    "@ScreenKoinViewModel requires a state property wrapper with an init(wrappedValue:) initializer.",
                    at: variable,
                    in: context
                )
                return []
            }

            let factoryArguments = mappedArguments
                .map { "\($0): \($0)" }
                .joined(separator: ", ")
            let factoryCall = "\(factoryExpression)(\(factoryArguments))"
            viewModelAssignments.append("        _\(name) = \(wrapper)(wrappedValue: \(factoryCall))")
        }

        let access = screen.modifiers.contains { $0.name.text == "public" || $0.name.text == "open" }
            ? "public "
            : ""
        let initializerParameters = routeFields
            .map { "\($0.name): \($0.type)" }
            .joined(separator: ", ")
        let routeAssignments = routeFields.map { "        self.\($0.name) = \($0.name)" }
        let body = (routeAssignments + viewModelAssignments).joined(separator: "\n")
        return [DeclSyntax(stringLiteral: "\(access)init(\(initializerParameters)) {\n\(body)\n}")]
    }

    private static func isScreenKoinViewModelAttribute(_ element: AttributeListSyntax.Element) -> Bool {
        guard let attribute = element.as(AttributeSyntax.self) else { return false }
        let name = attribute.attributeName.trimmedDescription
        return name == "ScreenKoinViewModel" || name.hasSuffix(".ScreenKoinViewModel")
    }

    private static func inferredFactoryExpression(
        for variable: VariableDeclSyntax,
        binding: PatternBindingSyntax,
        in context: some MacroExpansionContext
    ) -> String? {
        guard let type = binding.typeAnnotation?.type else {
            diagnose(
                "@ScreenKoinViewModel needs an explicit ViewModel type to infer its factory.",
                at: variable,
                in: context
            )
            return nil
        }

        let declaredType = type.trimmedDescription.split(separator: ".").last.map(String.init)
            ?? type.trimmedDescription
        let suffix = "ViewModel"
        guard declaredType.hasSuffix(suffix), declaredType.count > suffix.count else {
            diagnose(
                "Unable to infer @ScreenKoinViewModel factory for '\(declaredType)'. Use a *ViewModel type or pass factory: explicitly.",
                at: variable,
                in: context
            )
            return nil
        }

        let baseName = String(declaredType.dropLast(suffix.count))
        let lowerCamelName = String(baseName.prefix(1)).lowercased() + String(baseName.dropFirst())
        return "KoinWrapper.\(lowerCamelName)ForScreen"
    }
}
