/*
 * catalogCoverage: compares the library's public API with what the web catalog shows.
 *
 * It scans `jchucomponents-ui` and `jchucomponents-foundation` (commonMain) for public top-level
 * @Composable functions and extension functions, then looks for each name in the catalog sources
 * (samples/web/src). A name counts as covered when it appears there as a word, so a demo, a code
 * snippet or a search entry all count. Generic names can match by accident; the report is a guide, not a proof.
 *
 *  - coverage-ignore.txt   names that intentionally have no demo (every line needs a "# reason")
 *  - coverage-baseline.txt known gaps to burn down; the task only fails for NEW gaps
 *
 * ./gradlew :samples:web:catalogCoverage                        check, write build/reports/catalog-coverage.md
 * ./gradlew :samples:web:catalogCoverage -PupdateBaseline=true  rewrite the baseline from the current gaps
 */
class CoverageSymbol(val name: String, val kind: String, val path: String, val module: String = "", val pkg: String = "")

val coverageFunRegex =
    Regex(
        """^((?:(?:public|internal|private|inline|suspend|operator|infix|tailrec|expect|actual)\s+)*)""" +
            """fun\s+(?:<[^>]*>\s*)?([A-Za-z0-9_?<>,. *]+\.)?([A-Za-z0-9_]+)\s*\("""
    )

/** Package of a file, derived from its folder under src/commonMain/kotlin (the sources mirror their packages). */
fun packageOf(root: File, file: File): String = file.parentFile.relativeTo(File(root, "kotlin")).path.replace(File.separatorChar, '.')

/** Public top-level classes, objects and interfaces, which Dokka documents on their own page. */
fun scanTypes(roots: List<File>): List<CoverageSymbol> {
    val typeRegex = Regex("""^(?:(?:public|data|sealed|enum|abstract|open|value|annotation|fun|expect)\s+)*(?:class|object|interface)\s+([A-Z][A-Za-z0-9_]*)""")
    val found = mutableListOf<CoverageSymbol>()
    roots.forEach { root ->
        root.walkTopDown().filter { it.isFile && it.extension == "kt" }.forEach { file ->
            file.readLines().forEach { line ->
                if (line.startsWith("private") || line.startsWith("internal")) return@forEach
                val match = typeRegex.find(line) ?: return@forEach
                found += CoverageSymbol(match.groupValues[1], "type", file.path, root.parentFile.parentFile.name, packageOf(root, file))
            }
        }
    }
    return found
}

/** Dokka's page name: each capital becomes "-" plus its lowercase, so SelectableChip is -selectable-chip. */
fun dokkaPageName(name: String): String = name.map { if (it.isUpperCase()) "-" + it.lowercaseChar() else it.toString() }.joinToString("")

fun scanSymbols(roots: List<File>, projectDir: File, includePlain: Boolean = false): Map<String, CoverageSymbol> {
    val symbols = linkedMapOf<String, CoverageSymbol>()
    roots.forEach { root ->
        root.walkTopDown().filter { it.isFile && it.extension == "kt" }.forEach { file ->
            val lines = file.readLines()
            lines.forEachIndexed { index, line ->
                val match = coverageFunRegex.find(line) ?: return@forEachIndexed
                val modifiers = match.groupValues[1]
                if ("private" in modifiers || "internal" in modifiers) return@forEachIndexed
                val name = match.groupValues[3]
                val annotations =
                    lines
                        .take(index)
                        .takeLastWhile { it.isBlank() || it.trim().startsWith("@") }
                        .joinToString(" ") { it.trim() }
                if ("@Preview" in annotations || "Preview" in name) return@forEachIndexed
                val isExtension = match.groupValues[2].isNotEmpty()
                val kind =
                    when {
                        "@Composable" in annotations && !isExtension -> "composable"
                        isExtension -> "extension"
                        includePlain -> "function"
                        else -> return@forEachIndexed
                    }
                symbols.putIfAbsent(name, CoverageSymbol(name, kind, file.relativeTo(projectDir).path, root.parentFile.parentFile.name, packageOf(root, file)))
            }
        }
    }
    return symbols
}

fun catalogWords(dir: File): Set<String> =
    dir.walkTopDown().filter { it.isFile && it.extension == "kt" }
        .flatMap { Regex("[A-Za-z_][A-Za-z0-9_]*").findAll(it.readText()).map { m -> m.value } }
        .toSet()

fun readCoverageNames(file: File, requireReason: Boolean): Map<String, String> {
    if (!file.exists()) return emptyMap()
    val result = linkedMapOf<String, String>()
    file.readLines().map { it.trim() }.filter { it.isNotEmpty() && !it.startsWith("#") }.forEach { line ->
        val name = line.substringBefore("#").trim()
        val reason = line.substringAfter("#", "").trim()
        if (requireReason && reason.isEmpty()) throw GradleException("${file.name}: '$name' needs a '# reason'")
        result[name] = reason
    }
    return result
}

fun coverageSection(uncovered: List<CoverageSymbol>, kind: String): String {
    val items = uncovered.filter { it.kind == kind }.sortedBy { it.name }
    return if (items.isEmpty()) "_None._\n" else items.joinToString("\n") { "- `${it.name}` - ${it.path}" } + "\n"
}

tasks.register("catalogCoverage") {
    group = "verification"
    description = "Fails when a public composable or extension has no demo in the web catalog and is not in the baseline."

    val libraryRoots = listOf("jchucomponents-ui", "jchucomponents-foundation").map { rootProject.file("$it/src/commonMain") }
    val catalogSources = layout.projectDirectory.dir("src").asFile
    val ignoreFile = layout.projectDirectory.file("coverage-ignore.txt").asFile
    val baselineFile = layout.projectDirectory.file("coverage-baseline.txt").asFile
    val reportFile = layout.buildDirectory.file("reports/catalog-coverage.md").get().asFile
    val updateBaseline = providers.gradleProperty("updateBaseline").orNull == "true"
    val projectDir = rootProject.projectDir

    inputs.files(libraryRoots.map { fileTree(it) { include("**/*.kt") } })
    inputs.files(fileTree(catalogSources) { include("**/*.kt") })
    inputs.files(ignoreFile, baselineFile)
    outputs.file(reportFile)

    doLast {
        val symbols = scanSymbols(libraryRoots, projectDir)
        val words = catalogWords(catalogSources)
        val ignored = readCoverageNames(ignoreFile, requireReason = true)
        val baseline = readCoverageNames(baselineFile, requireReason = false)

        val relevant = symbols.values.filter { it.name !in ignored }
        val uncovered = relevant.filter { it.name !in words }
        val covered = relevant.size - uncovered.size
        val newGaps = uncovered.filter { it.name !in baseline }
        val staleBaseline = baseline.keys.filter { name -> symbols[name] == null || name in words || name in ignored }
        val percent = if (relevant.isEmpty()) 100 else covered * 100 / relevant.size

        reportFile.parentFile.mkdirs()
        reportFile.writeText(
            "# Web catalog coverage\n\n" +
                "**$covered of ${relevant.size}** public composables and extensions are shown ($percent%). ${ignored.size} are ignored on purpose.\n\n" +
                "## Composables without a demo\n" + coverageSection(uncovered, "composable") + "\n" +
                "## Extensions without a demo\n" + coverageSection(uncovered, "extension")
        )

        println("Catalog coverage: $covered/${relevant.size} ($percent%), ${uncovered.size} gaps, ${ignored.size} ignored")
        println("Report: ${reportFile.relativeTo(projectDir)}")

        if (updateBaseline) {
            baselineFile.writeText(
                "# Known gaps: public API without a demo in the web catalog. Burn this list down; do not grow it.\n" +
                    "# Regenerate with: ./gradlew :samples:web:catalogCoverage -PupdateBaseline=true\n" +
                    uncovered.map { it.name }.sorted().joinToString("\n") + "\n"
            )
            println("Baseline rewritten with ${uncovered.size} entries")
        } else {
            if (staleBaseline.isNotEmpty()) {
                println("Now covered or gone, remove from coverage-baseline.txt: ${staleBaseline.sorted().joinToString()}")
            }
            if (newGaps.isNotEmpty()) {
                throw GradleException(
                    "These public symbols have no demo in the web catalog:\n" +
                        newGaps.sortedBy { it.name }.joinToString("\n") { "  - ${it.name} (${it.kind}) ${it.path}" } +
                        "\nAdd a demo in samples/web, or list the name in coverage-ignore.txt with a '# reason'."
                )
            }
        }
    }
}

val generateApiLinks =
    tasks.register("generateApiLinks") {
        group = "documentation"
        description = "Generates ApiLinks.kt: the Dokka page of every public symbol, so catalog links never go stale."

        val libraryRoots = listOf("jchucomponents-ui", "jchucomponents-foundation").map { rootProject.file("$it/src/commonMain") }
        val outputDir = layout.buildDirectory.dir("generated/apiLinks")
        val projectDir = rootProject.projectDir

        inputs.files(libraryRoots.map { fileTree(it) { include("**/*.kt") } })
        outputs.dir(outputDir)

        doLast {
            val pages = linkedMapOf<String, String>()
            scanTypes(libraryRoots).forEach { pages.putIfAbsent(it.name, "${it.module}/${it.pkg}/${dokkaPageName(it.name)}/index.html") }
            scanSymbols(libraryRoots, projectDir, includePlain = true).values.forEach { pages.putIfAbsent(it.name, "${it.module}/${it.pkg}/${dokkaPageName(it.name)}.html") }
            val file = outputDir.get().file("com/jeluchu/catalog/ApiLinks.kt").asFile
            file.parentFile.mkdirs()
            file.writeText(
                "// Generated by :samples:web:generateApiLinks. Do not edit.\n" +
                    "package com.jeluchu.catalog\n\n" +
                    "/** Dokka page of each public symbol, relative to the api/ folder. */\n" +
                    "object ApiLinks {\n    val pages: Map<String, String> =\n        mapOf(\n" +
                    pages.entries.sortedBy { it.key }.joinToString(",\n") { "            \"${it.key}\" to \"${it.value}\"" } +
                    "\n        )\n}\n"
            )
            println("ApiLinks: ${pages.size} symbols")
        }
    }
