package com.jeluchu.jchucomponents.navigation.ksp.codegen

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.jeluchu.jchucomponents.navigation.ksp.constants.NavigationConstants
import com.jeluchu.jchucomponents.navigation.ksp.models.ParameterKind
import com.jeluchu.jchucomponents.navigation.ksp.models.ParameterModel
import com.jeluchu.jchucomponents.navigation.ksp.models.ScreenModel
import com.jeluchu.jchucomponents.navigation.ksp.utils.asKotlinIdentifier
import com.jeluchu.jchucomponents.navigation.ksp.utils.generatedPackage
import com.jeluchu.jchucomponents.navigation.ksp.utils.lowercaseFirst
import com.jeluchu.jchucomponents.navigation.ksp.utils.qualifiedTypeNames
import com.jeluchu.jchucomponents.navigation.ksp.utils.renderTypeReferences

internal object NavigationSourceGenerator {
    fun generateRoutes(
        codeGenerator: CodeGenerator,
        logger: KSPLogger,
        graph: String,
        screens: List<ScreenModel>,
    ) {
        val packageName = generatedPackage(screens)
        val sources = screens.mapNotNull(ScreenModel::sourceFile).distinct().toTypedArray()
        if (sources.isEmpty()) {
            logger.error("Could not determine source files for graph '$graph'.")
            return
        }

        val routesType = "${graph}Routes"
        val routeArgumentTypes = screens
            .flatMap { screen -> screen.parameters.filter { it.kind == ParameterKind.ARGUMENT } }
            .flatMap { it.type.qualifiedTypeNames() }
            .toSet()
        val typeReferences = createTypeReferences(
            typeNames = routeArgumentTypes,
            reservedNames = GENERATED_IMPORT_NAMES + routesType
        )
        val typeImports = typeReferences
            .filter { (qualifiedName, referenceName) ->
                referenceName == qualifiedName.substringAfterLast('.') &&
                    qualifiedName.substringBeforeLast('.') != packageName
            }
            .keys
        val imports = buildSet {
            add("androidx.navigation3.runtime.NavKey")
            add("kotlinx.serialization.Serializable")
            addAll(typeImports)
        }
        val file = codeGenerator.createNewFile(Dependencies(true, *sources), packageName, routesType)
        file.bufferedWriter().use { writer ->
            writer.write(
                buildString {
                    appendLine("package $packageName")
                    appendLine()
                    imports.sorted().forEach { appendLine("import $it") }
                    appendLine()
                    appendLine("@Serializable")
                    appendLine("sealed interface $routesType : NavKey {")
                    screens.forEachIndexed { index, screen ->
                        if (index > 0) appendLine()
                        val arguments = screen.parameters.filter { it.kind == ParameterKind.ARGUMENT }
                        appendLine("    @Serializable")
                        if (arguments.isEmpty()) {
                            appendLine("    data object ${screen.route} : $routesType")
                        } else {
                            appendLine("    data class ${screen.route}(")
                            arguments.forEach { argument ->
                                appendLine(
                                    "        val ${argument.name.asKotlinIdentifier()}: " +
                                        argument.type.renderTypeReferences(typeReferences) + ","
                                )
                            }
                            appendLine("    ) : $routesType")
                        }
                    }
                    appendLine("}")
                }
            )
        }
    }

    fun generateNavigation(
        codeGenerator: CodeGenerator,
        logger: KSPLogger,
        graph: String,
        screens: List<ScreenModel>,
        screensByGraph: Map<String, List<ScreenModel>>,
    ) {
        val packageName = generatedPackage(screens)
        val sources = screens.mapNotNull(ScreenModel::sourceFile).distinct().toTypedArray()
        if (sources.isEmpty()) {
            logger.error("Could not determine source files for graph '$graph'.")
            return
        }

        val routesType = "${graph}Routes"
        val file = codeGenerator.createNewFile(Dependencies(true, *sources), packageName, graph + "Navigation")
        file.bufferedWriter().use { writer ->
            writer.write(generateNavigationSource(packageName, graph, routesType, screens, screensByGraph))
        }
    }

    private val GENERATED_IMPORT_NAMES = setOf(
        "EntryProviderScope",
        "NavBackStack",
        "NavKey",
        "Serializable",
        "back",
        "inject",
        "injectByParams",
        "navigateTo"
    )

    private fun createTypeReferences(
        typeNames: Set<String>,
        reservedNames: Set<String>
    ): Map<String, String> {
        val namesBySimpleName = typeNames.groupBy { it.substringAfterLast('.') }
        return typeNames.associateWith { qualifiedName ->
            val simpleName = qualifiedName.substringAfterLast('.')
            if (namesBySimpleName.getValue(simpleName).size == 1 && simpleName !in reservedNames) {
                simpleName
            } else {
                qualifiedName
            }
        }
    }

    private fun generateNavigationSource(
        packageName: String,
        graph: String,
        routesType: String,
        screens: List<ScreenModel>,
        screensByGraph: Map<String, List<ScreenModel>>
    ): String {
        val parameterTypeNames = screens
            .flatMap(ScreenModel::parameters)
            .filter { it.kind != ParameterKind.KOIN_VIEW_MODEL }
            .flatMap { it.type.qualifiedTypeNames() }
            .toSet()
        val navigateRouteTypeNames = screens
            .flatMap(ScreenModel::parameters)
            .filter { it.kind == ParameterKind.NAVIGATE && it.targetGraph != graph }
            .mapNotNull { parameter ->
                val targetGraph = parameter.targetGraph ?: return@mapNotNull null
                screensByGraph[targetGraph]?.let { targetScreens ->
                    "${generatedPackage(targetScreens)}.${targetGraph}Routes"
                }
            }
            .toSet()
        val typeNames = parameterTypeNames + navigateRouteTypeNames
        val providerName = "${graph.lowercaseFirst()}Entries"
        val typeReferences = createTypeReferences(
            typeNames = typeNames,
            reservedNames = GENERATED_IMPORT_NAMES + routesType + providerName
        )
        val functionReferences = screens.associate { screen ->
            val functionName = screen.functionName.substringAfterLast('.')
            val functionIsUnambiguous = screens.count { it.functionName.substringAfterLast('.') == functionName } == 1
            val nameIsAvailable = functionName !in GENERATED_IMPORT_NAMES &&
                functionName !in typeNames.map { it.substringAfterLast('.') } &&
                functionName != routesType &&
                functionName != providerName
            val canUseSimpleName = functionIsUnambiguous && nameIsAvailable
            screen.functionName to
                if (canUseSimpleName) functionName else screen.functionName
        }
        val typeImports = typeReferences
            .filter { (qualifiedName, referenceName) ->
                referenceName == qualifiedName.substringAfterLast('.') &&
                    qualifiedName.substringBeforeLast('.') != packageName
            }
            .keys
        val functionImports = screens
            .filter { screen ->
                functionReferences[screen.functionName] == screen.functionName.substringAfterLast('.') &&
                    screen.packageName != packageName
            }
            .map(ScreenModel::functionName)
        val imports = buildSet {
            add("androidx.navigation3.runtime.EntryProviderScope")
            add("androidx.navigation3.runtime.NavBackStack")
            add("androidx.navigation3.runtime.NavKey")
            if (screens.any { screen -> screen.parameters.any { it.kind == ParameterKind.BACK } }) {
                add(NavigationConstants.BACK_EXTENSION)
            }
            if (screens.any { screen -> screen.parameters.any { it.kind == ParameterKind.NAVIGATE } }) {
                add(NavigationConstants.NAVIGATE_EXTENSION)
            }
            if (screens.any { screen -> screen.parameters.any { it.kind == ParameterKind.KOIN_VIEW_MODEL } }) {
                add(NavigationConstants.KOIN_INJECT_EXTENSION)
            }
            if (screens.any { screen ->
                    screen.parameters.any { it.kind == ParameterKind.KOIN_VIEW_MODEL } &&
                        screen.parameters.any { it.kind == ParameterKind.ARGUMENT }
                }
            ) {
                add(NavigationConstants.KOIN_INJECT_BY_PARAMS_EXTENSION)
            }
            addAll(typeImports)
            addAll(functionImports)
        }
        val customActions = screens.flatMap(ScreenModel::parameters)
            .filter { it.kind == ParameterKind.CUSTOM_ACTION }
            .distinctBy(ParameterModel::name)

        return buildString {
            appendLine("package $packageName")
            appendLine()
            imports.sorted().forEach { appendLine("import $it") }
            appendLine()
            appendLine("fun EntryProviderScope<NavKey>.$providerName(")
            appendLine("    backStack: NavBackStack<NavKey>,")
            customActions.forEach { action ->
                appendLine(
                    "    ${action.name.asKotlinIdentifier()}: " +
                        action.type.renderTypeReferences(typeReferences) + ","
                )
            }
            appendLine(") {")
            screens.forEachIndexed { index, screen ->
                if (index > 0) appendLine()
                val hasRouteArguments = screen.parameters.any { it.kind == ParameterKind.ARGUMENT }
                val entryParameter = if (hasRouteArguments) "key" else "_"
                appendLine("    entry<$routesType.${screen.route}> { $entryParameter ->")
                appendLine("        ${functionReferences.getValue(screen.functionName)}(")
                screen.parameters.forEach { parameter ->
                    val parameterName = parameter.name.asKotlinIdentifier()
                    when (parameter.kind) {
                        ParameterKind.ARGUMENT ->
                            appendLine("            $parameterName = key.$parameterName,")
                        ParameterKind.BACK ->
                            appendLine("            $parameterName = { backStack.back() },")
                        ParameterKind.CUSTOM_ACTION ->
                            appendLine("            $parameterName = $parameterName,")
                        ParameterKind.KOIN_VIEW_MODEL -> {
                            val injection = if (hasRouteArguments) "injectByParams(key)" else "inject()"
                            appendLine("            $parameterName = $injection,")
                        }
                        ParameterKind.NAVIGATE -> {
                            val targetGraph = parameter.targetGraph!!
                            val destination = screensByGraph.getValue(targetGraph)
                                .first { it.route == parameter.targetRoute }
                            val destinationRoute = if (targetGraph == graph) {
                                "$routesType.${destination.route}"
                            } else {
                                val destinationRoutesType =
                                    "${generatedPackage(screensByGraph.getValue(targetGraph))}." +
                                        "${targetGraph}Routes"
                                "${typeReferences.getValue(destinationRoutesType)}.${destination.route}"
                            }
                            val destinationArguments = destination.parameters
                                .filter { it.kind == ParameterKind.ARGUMENT }
                            val usedCallbackNames = mutableSetOf<String>()
                            val callbackParameters = destinationArguments.mapIndexed { index, argument ->
                                val baseName = if (argument.name == "backStack") {
                                    "navigationBackStack"
                                } else {
                                    argument.name.asKotlinIdentifier()
                                }
                                var callbackName = baseName
                                var suffix = 0
                                while (!usedCallbackNames.add(callbackName)) {
                                    callbackName = "routeArgument${index}_${suffix++}"
                                }
                                callbackName
                            }
                            val callbackHeader = callbackParameters
                                .takeIf(List<String>::isNotEmpty)
                                ?.joinToString(", ", prefix = "$parameterName = { ", postfix = " ->")
                                ?: "$parameterName = {"
                            appendLine("            $callbackHeader")
                            appendLine("                backStack.navigateTo(")
                            if (destinationArguments.isEmpty()) {
                                appendLine("                    screen = $destinationRoute,")
                            } else {
                                appendLine("                    screen = $destinationRoute(")
                                destinationArguments.forEachIndexed { argumentIndex, argument ->
                                    appendLine(
                                        "                        ${argument.name.asKotlinIdentifier()} = " +
                                            "${callbackParameters[argumentIndex]},"
                                    )
                                }
                                appendLine("                    ),")
                            }
                            appendLine("                )")
                            appendLine("            },")
                        }
                    }
                }
                appendLine("        )")
                appendLine("    }")
            }
            appendLine("}")
        }
    }
}
