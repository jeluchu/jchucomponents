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

        let graphs = Set(screens.map(\.graph))
        guard graphs.allSatisfy({ graphMethodName($0) != "jchuDestinations" }) else {
            throw GeneratorError("Each @Screen graph name must contain at least one letter or digit.")
        }

        let methods = graphs.map { ($0, graphMethodName($0)) }
        let methodsByName = Dictionary(grouping: methods, by: { $0.1 })
        let collisions = methodsByName.filter { $0.value.count > 1 }
        guard collisions.isEmpty else {
            let names = collisions.keys.sorted().joined(separator: ", ")
            throw GeneratorError("Graph names produce duplicate destination modifiers: \(names).")
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
            "",
            "extension View {"
        ]

        for (graph, graphScreens) in groups {
            lines.append("    @MainActor")
            lines.append("    @ViewBuilder")
            lines.append("    func \(graphMethodName(graph))() -> some View {")

            let orderedScreens = graphScreens.sorted { $0.routeType < $1.routeType }
            for (index, screen) in orderedScreens.enumerated() {
                let prefix = index == 0 ? "        self" : "            "
                lines.append("\(prefix).jchuNavigationDestination(for: \(screen.routeType).self)")
            }

            lines.append("    }")
            lines.append("")
        }

        lines.append("}")
        return lines.joined(separator: "\n") + "\n"
    }

    private static func graphMethodName(_ graph: String) -> String {
        let words = graph.split { character in
            !character.isLetter && !character.isNumber && character != "_"
        }
        let suffix = words
            .map { word in
                String(word.prefix(1)).uppercased() + String(word.dropFirst())
            }
            .joined()
        return "jchu\(suffix)Destinations"
    }
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

            guard let graph = graphName(from: attribute), !graph.isEmpty else {
                errors.append("\(sourcePath): @Screen requires a non-empty string literal for graph.")
                continue
            }

            screens.append(
                ScreenDeclaration(
                    graph: graph,
                    routeType: "\(node.name.text)Route"
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

    private func graphName(from attribute: AttributeSyntax) -> String? {
        guard case let .argumentList(arguments) = attribute.arguments,
              let expression = arguments.first(where: { $0.label?.text == "graph" })?.expression,
              let literal = expression.as(StringLiteralExprSyntax.self)
        else { return nil }
        return literal.representedLiteralValue
    }
}

private struct ScreenDeclaration {
    let graph: String
    let routeType: String
}

private struct GeneratorError: Error, CustomStringConvertible {
    let description: String

    init(_ description: String) {
        self.description = description
    }
}
