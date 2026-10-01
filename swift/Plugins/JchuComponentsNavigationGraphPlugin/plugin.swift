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
            .map(\.url)
            .sorted { $0.path < $1.path }
        guard !swiftFiles.isEmpty else { return [] }

        let output = context.pluginWorkDirectoryURL.appending(
            path: "JchuNavigationGraphDestinations.swift"
        )
        let generator = try context.tool(named: "JchuComponentsNavigationGraphGenerator")

        return [
            .buildCommand(
                displayName: "Generate JchuComponents SwiftUI navigation destinations",
                executable: generator.url,
                arguments: [output.path] + swiftFiles.map(\.path),
                inputFiles: swiftFiles,
                outputFiles: [output]
            )
        ]
    }
}

#if canImport(XcodeProjectPlugin)
import XcodeProjectPlugin

extension JchuComponentsNavigationGraphPlugin: XcodeBuildToolPlugin {
    func createBuildCommands(
        context: XcodePluginContext,
        target: XcodeTarget
    ) throws -> [Command] {
        let swiftFiles = target.inputFiles
            .map(\.url)
            .filter { $0.pathExtension == "swift" }
            .sorted { $0.path < $1.path }
        guard !swiftFiles.isEmpty else { return [] }

        let output = context.pluginWorkDirectoryURL.appending(
            path: "JchuNavigationGraphDestinations.swift"
        )
        let generator = try context.tool(named: "JchuComponentsNavigationGraphGenerator")

        return [
            .buildCommand(
                displayName: "Generate JchuComponents SwiftUI navigation destinations",
                executable: generator.url,
                arguments: [output.path] + swiftFiles.map(\.path),
                inputFiles: swiftFiles,
                outputFiles: [output]
            )
        ]
    }
}
#endif
