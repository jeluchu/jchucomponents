import Foundation
import SwiftParser
import SwiftSyntax

@main
enum JchuComponentsNavigationGraphGenerator {
    static func main() throws {
        let arguments = CommandLine.arguments
        guard let outputPath = arguments.dropFirst().first else {
            throw GeneratorError("Expected an output path followed by Swift source paths.")
        }

        var screens: [ScreenDeclaration] = []
        var errors: [String] = []

        for sourcePath in arguments.dropFirst(2) {
            let sourceURL = URL(fileURLWithPath: sourcePath)
            let source = try String(contentsOf: sourceURL, encoding: .utf8)
            let collector = ScreenCollector(sourcePath: sourcePath)
            collector.walk(Parser.parse(source: source))
            screens.append(contentsOf: collector.screens)
            errors.append(contentsOf: collector.errors)
        }

        guard errors.isEmpty else {
            throw GeneratorError(errors.joined(separator: "\n"))
        }

        let groups = Dictionary(grouping: screens, by: \.graph)
        guard groups.keys.allSatisfy({ !pascalIdentifier(from: $0).isEmpty }) else {
            throw GeneratorError("Each @Screen graph name must contain at least one letter or digit.")
        }
        let routeTypesByName = Dictionary(grouping: groups.keys, by: graphRoutesTypeName)
        let routeTypeCollisions = routeTypesByName.filter { $0.value.count > 1 }
        guard routeTypeCollisions.isEmpty else {
            let names = routeTypeCollisions.keys.sorted().joined(separator: ", ")
            throw GeneratorError("Graph names produce duplicate route types: \(names).")
        }

        for (graph, graphScreens) in groups {
            let casesByName = Dictionary(grouping: graphScreens, by: \.routeCase)
            let duplicateCases = casesByName.filter { $0.value.count > 1 }
            guard duplicateCases.isEmpty else {
                let names = duplicateCases.keys.sorted().joined(separator: ", ")
                throw GeneratorError("Graph '\(graph)' contains duplicate route cases: \(names).")
            }
        }

        let methods = groups.keys.map { ($0, graphMethodName($0)) }
        let methodsByName = Dictionary(grouping: methods, by: { $0.1 })
        let collisions = methodsByName.filter { $0.value.count > 1 }
        guard collisions.isEmpty else {
            let names = collisions.keys.sorted().joined(separator: ", ")
            throw GeneratorError("Graph names produce duplicate destination modifiers: \(names).")
        }
        guard methodsByName["jchuNavigationDestinations"] == nil else {
            throw GeneratorError("The graph name 'Navigation' conflicts with the generated all-destinations modifier.")
        }

        let generatedSource = render(screens: screens)
        let outputURL = URL(fileURLWithPath: outputPath)
        try FileManager.default.createDirectory(
            at: outputURL.deletingLastPathComponent(),
            withIntermediateDirectories: true
        )
        try generatedSource.write(to: outputURL, atomically: true, encoding: .utf8)
    }

    private static func render(screens: [ScreenDeclaration]) -> String {
        let groups = Dictionary(grouping: screens, by: \.graph)
            .sorted { $0.key < $1.key }

        guard !groups.isEmpty else {
            return "// No @Screen declarations were found in this target.\n"
        }

        var lines = [
            "import JchuComponentsNavigation",
            "import SwiftUI",
            ""
        ]

        for (graph, graphScreens) in groups {
            let routeType = graphRoutesTypeName(graph)
            let orderedScreens = graphScreens.sorted { $0.routeCase < $1.routeCase }
            lines.append("enum \(routeType): JchuNavigationRoute {")
            for screen in orderedScreens {
                let fields = screen.arguments.map { "\($0.name): \($0.type)" }.joined(separator: ", ")
                let associatedValues = fields.isEmpty ? "" : "(\(fields))"
                lines.append("    case \(screen.routeCase)\(associatedValues)")
            }
            lines.append("")
            lines.append("    typealias Destination = AnyView")
            lines.append("")
            lines.append("    @MainActor")
            lines.append("    func destination() -> AnyView {")
            lines.append("        switch self {")
            for screen in orderedScreens {
                let bindings = screen.arguments.map { "let \($0.name)" }.joined(separator: ", ")
                let pattern = bindings.isEmpty ? ".\(screen.routeCase)" : ".\(screen.routeCase)(\(bindings))"
                let arguments = screen.arguments.map { "\($0.name): \($0.name)" }.joined(separator: ", ")
                lines.append("        case \(pattern):")
                lines.append("            AnyView(\(screen.screenName)(\(arguments)))")
            }
            lines.append("        }")
            lines.append("    }")
            lines.append("")
            lines.append("    @MainActor")
            lines.append("    static func route(to destination: Self) -> AnyView {")
            lines.append("        destination.destination()")
            lines.append("    }")
            lines.append("}")
            lines.append("")
        }

        lines.append("extension View {")
        for (graph, graphScreens) in groups {
            let routeTypes = [graphRoutesTypeName(graph)] + graphScreens
                .sorted { $0.legacyRouteType < $1.legacyRouteType }
                .map(\.legacyRouteType)

            lines.append("    @MainActor")
            lines.append("    @ViewBuilder")
            lines.append("    func \(graphMethodName(graph))() -> some View {")
            for (index, routeType) in routeTypes.enumerated() {
                let prefix = index == 0 ? "        self" : "            "
                lines.append("\(prefix).jchuNavigationDestination(for: \(routeType).self)")
            }
            lines.append("    }")
            lines.append("")
        }

        lines.append("    @MainActor")
        lines.append("    func jchuNavigationDestinations() -> AnyView {")
        lines.append("        var destinations = AnyView(self)")
        for (graph, _) in groups {
            lines.append("        destinations = AnyView(destinations.\(graphMethodName(graph))())")
        }
        lines.append("        return destinations")
        lines.append("    }")
        lines.append("")
        lines.append("}")
        return lines.joined(separator: "\n") + "\n"
    }
}

private func graphMethodName(_ graph: String) -> String {
    "jchu\(pascalIdentifier(from: graph))Destinations"
}

private func graphRoutesTypeName(_ graph: String) -> String {
    "\(pascalIdentifier(from: graph))Routes"
}

private func pascalIdentifier(from name: String) -> String {
    let words = name.split { character in
        !character.isLetter && !character.isNumber && character != "_"
    }
    var identifier = words
        .map { word in
            String(word.prefix(1)).uppercased() + String(word.dropFirst())
        }
        .joined()
    if identifier.first?.isNumber == true {
        identifier = "Graph\(identifier)"
    }
    return identifier
}

private func routeCaseName(from name: String) -> String? {
    let words = name.split { character in
        !character.isLetter && !character.isNumber && character != "_"
    }
    guard !words.isEmpty else { return nil }

    var identifier: String
    if words.count == 1 {
        let word = String(words[0])
        identifier = String(word.prefix(1)).lowercased() + String(word.dropFirst())
    } else {
        identifier = words.enumerated().map { element in
            let value = String(element.element)
            if element.offset == 0 {
                return String(value.prefix(1)).lowercased() + String(value.dropFirst())
            }
            return String(value.prefix(1)).uppercased() + String(value.dropFirst())
        }.joined()
    }

    if identifier.first?.isNumber == true {
        identifier = "route\(identifier.prefix(1).uppercased())\(identifier.dropFirst())"
    }

    let reservedWords: Set<String> = [
        "associatedtype", "class", "deinit", "enum", "extension", "fileprivate", "func", "import",
        "init", "inout", "internal", "let", "open", "operator", "private", "protocol", "public",
        "static", "struct", "subscript", "typealias", "var", "break", "case", "continue", "default",
        "defer", "do", "else", "fallthrough", "for", "guard", "if", "in", "repeat", "return",
        "throw", "switch", "where", "while", "as", "Any", "catch", "false", "is", "nil",
        "rethrows", "super", "self", "Self", "throws", "true", "try", "any", "some"
    ]
    if reservedWords.contains(identifier) {
        identifier += "Route"
    }
    return identifier
}

private final class ScreenCollector: SyntaxVisitor {
    private let sourcePath: String
    private var nestingDepth = 0

    private(set) var screens: [ScreenDeclaration] = []
    private(set) var errors: [String] = []

    init(sourcePath: String) {
        self.sourcePath = sourcePath
        super.init(viewMode: .sourceAccurate)
    }

    override func visit(_ node: StructDeclSyntax) -> SyntaxVisitorContinueKind {
        let screenAttributes = node.attributes.compactMap { element -> AttributeSyntax? in
            guard let attribute = element.as(AttributeSyntax.self) else { return nil }
            let name = attribute.attributeName.trimmedDescription
            return name == "Screen" || name.hasSuffix(".Screen") ? attribute : nil
        }

        for attribute in screenAttributes {
            guard nestingDepth == 0 else {
                errors.append("\(sourcePath): @Screen views must be top-level structs for graph registration.")
                continue
            }

            guard let graph = stringArgument("graph", from: attribute), !graph.isEmpty else {
                errors.append("\(sourcePath): @Screen requires a non-empty string literal for graph.")
                continue
            }

            if node.modifiers.contains(where: { $0.name.text == "private" || $0.name.text == "fileprivate" }) {
                errors.append("\(sourcePath): @Screen view '\(node.name.text)' cannot be private because generated navigation code is emitted in a separate source file.")
                continue
            }

            let arguments = argumentList(from: attribute)
            let declaredRoute: String
            if let routeExpression = arguments?.first(where: { $0.label?.text == "route" })?.expression {
                guard let literal = routeExpression.as(StringLiteralExprSyntax.self),
                      let route = literal.representedLiteralValue
                else {
                    errors.append("\(sourcePath): @Screen route must be a string literal.")
                    continue
                }
                declaredRoute = route.isEmpty ? node.name.text : route
            } else {
                declaredRoute = node.name.text
            }

            guard let routeCase = routeCaseName(from: declaredRoute) else {
                errors.append("\(sourcePath): @Screen route '\(declaredRoute)' does not produce a valid route case name.")
                continue
            }

            guard let declaredArgumentNames = stringArrayArgument("arguments", from: arguments) else {
                errors.append("\(sourcePath): @Screen arguments must be an array of string literals.")
                continue
            }
            guard Set(declaredArgumentNames).count == declaredArgumentNames.count else {
                errors.append("\(sourcePath): @Screen arguments must not contain duplicate names.")
                continue
            }
            let markedArgumentNames = routeArgumentNames(in: node)
            let argumentNames = declaredArgumentNames + markedArgumentNames.filter {
                !declaredArgumentNames.contains($0)
            }

            let storedProperties = storedProperties(in: node)
            var fields: [RouteField] = []
            var hasInvalidArgument = false
            for name in argumentNames {
                guard let type = storedProperties[name] else {
                    errors.append("\(sourcePath): @Screen argument '\(name)' must match an unwrapped stored property with an explicit type.")
                    hasInvalidArgument = true
                    continue
                }
                fields.append(RouteField(name: name, type: type))
            }
            guard !hasInvalidArgument else { continue }

            screens.append(
                ScreenDeclaration(
                    graph: graph,
                    screenName: node.name.text,
                    routeCase: routeCase,
                    arguments: fields,
                    legacyRouteType: "\(node.name.text)Route"
                )
            )
        }

        nestingDepth += 1
        return .visitChildren
    }

    override func visitPost(_ node: StructDeclSyntax) {
        nestingDepth -= 1
    }

    override func visit(_ node: ClassDeclSyntax) -> SyntaxVisitorContinueKind {
        enterScope()
    }

    override func visitPost(_ node: ClassDeclSyntax) {
        leaveScope()
    }

    override func visit(_ node: EnumDeclSyntax) -> SyntaxVisitorContinueKind {
        enterScope()
    }

    override func visitPost(_ node: EnumDeclSyntax) {
        leaveScope()
    }

    override func visit(_ node: ProtocolDeclSyntax) -> SyntaxVisitorContinueKind {
        enterScope()
    }

    override func visitPost(_ node: ProtocolDeclSyntax) {
        leaveScope()
    }

    override func visit(_ node: ActorDeclSyntax) -> SyntaxVisitorContinueKind {
        enterScope()
    }

    override func visitPost(_ node: ActorDeclSyntax) {
        leaveScope()
    }

    override func visit(_ node: ExtensionDeclSyntax) -> SyntaxVisitorContinueKind {
        enterScope()
    }

    override func visitPost(_ node: ExtensionDeclSyntax) {
        leaveScope()
    }

    override func visit(_ node: FunctionDeclSyntax) -> SyntaxVisitorContinueKind {
        enterScope()
    }

    override func visitPost(_ node: FunctionDeclSyntax) {
        leaveScope()
    }

    override func visit(_ node: InitializerDeclSyntax) -> SyntaxVisitorContinueKind {
        enterScope()
    }

    override func visitPost(_ node: InitializerDeclSyntax) {
        leaveScope()
    }

    override func visit(_ node: SubscriptDeclSyntax) -> SyntaxVisitorContinueKind {
        enterScope()
    }

    override func visitPost(_ node: SubscriptDeclSyntax) {
        leaveScope()
    }

    private func enterScope() -> SyntaxVisitorContinueKind {
        nestingDepth += 1
        return .visitChildren
    }

    private func leaveScope() {
        nestingDepth -= 1
    }

    private func argumentList(from attribute: AttributeSyntax) -> LabeledExprListSyntax? {
        guard case let .argumentList(arguments) = attribute.arguments else { return nil }
        return arguments
    }

    private func stringArgument(_ name: String, from attribute: AttributeSyntax) -> String? {
        guard let expression = argumentList(from: attribute)?.first(where: { $0.label?.text == name })?.expression,
              let literal = expression.as(StringLiteralExprSyntax.self)
        else { return nil }
        return literal.representedLiteralValue
    }

    private func stringArrayArgument(_ name: String, from arguments: LabeledExprListSyntax?) -> [String]? {
        guard let expression = arguments?.first(where: { $0.label?.text == name })?.expression else {
            return []
        }
        guard let array = expression.as(ArrayExprSyntax.self) else { return nil }

        var values: [String] = []
        for element in array.elements {
            guard let value = element.expression.as(StringLiteralExprSyntax.self)?.representedLiteralValue else {
                return nil
            }
            values.append(value)
        }
        return values
    }

    private func routeArgumentNames(in declaration: StructDeclSyntax) -> [String] {
        declaration.memberBlock.members.flatMap { member -> [String] in
            guard let variable = member.decl.as(VariableDeclSyntax.self),
                  variable.attributes.contains(where: isRouteArgumentAttribute)
            else { return [] }

            return variable.bindings.compactMap { binding in
                binding.pattern.as(IdentifierPatternSyntax.self)?.identifier.text
            }
        }
    }

    private func isRouteArgumentAttribute(_ element: AttributeListSyntax.Element) -> Bool {
        guard let attribute = element.as(AttributeSyntax.self) else { return false }
        let name = attribute.attributeName.trimmedDescription
        return name == "RouteArgument" || name.hasSuffix(".RouteArgument")
    }

    private func storedProperties(in declaration: StructDeclSyntax) -> [String: String] {
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
}

private struct ScreenDeclaration {
    let graph: String
    let screenName: String
    let routeCase: String
    let arguments: [RouteField]
    let legacyRouteType: String
}

private struct RouteField {
    let name: String
    let type: String
}

private struct GeneratorError: Error, CustomStringConvertible {
    let description: String

    init(_ description: String) {
        self.description = description
    }
}
