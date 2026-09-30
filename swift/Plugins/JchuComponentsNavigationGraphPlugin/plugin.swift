import PackagePlugin

@main
struct JchuComponentsNavigationGraphPlugin: BuildToolPlugin {
    func createBuildCommands(
        context: PluginContext,
        target: Target
    ) throws -> [Command] {
        guard let sourceTarget = target as? SourceModuleTarget else { return [] }

        let swiftFiles = sourceTarget
            .sourceFiles(withSuffix: "swift")
            .map(\.path)
            .sorted { $0.string < $1.string }
        guard !swiftFiles.isEmpty else { return [] }

        let output = context.pluginWorkDirectory.appending("JchuNavigationGraphDestinations.swift")
        let generator = try context.tool(named: "JchuComponentsNavigationGraphGenerator")

        return [
            .buildCommand(
                displayName: "Generate JchuComponents SwiftUI navigation destinations",
                executable: generator.path,
                arguments: [output.string] + swiftFiles.map(\.string),
                inputFiles: swiftFiles,
                outputFiles: [output]
            )
        ]
    }
}
