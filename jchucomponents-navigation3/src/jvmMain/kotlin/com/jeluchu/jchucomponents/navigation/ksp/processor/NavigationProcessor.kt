package com.jeluchu.jchucomponents.navigation.ksp.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.validate
import com.jeluchu.jchucomponents.navigation.ksp.codegen.NavigationSourceGenerator
import com.jeluchu.jchucomponents.navigation.ksp.constants.NavigationConstants
import com.jeluchu.jchucomponents.navigation.ksp.models.ParameterKind
import com.jeluchu.jchucomponents.navigation.ksp.models.ParameterModel
import com.jeluchu.jchucomponents.navigation.ksp.models.ScreenModel
import com.jeluchu.jchucomponents.navigation.ksp.utils.asKotlinSourceType
import com.jeluchu.jchucomponents.navigation.ksp.utils.classArgument
import com.jeluchu.jchucomponents.navigation.ksp.utils.isTypeIdentifier
import com.jeluchu.jchucomponents.navigation.ksp.utils.qualifiedName
import com.jeluchu.jchucomponents.navigation.ksp.utils.stringArgument

internal class NavigationProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    private val screensByGraph = linkedMapOf<String, MutableMap<String, ScreenModel>>()
    private val generatedRouteGraphs = mutableSetOf<String>()
    private val generatedNavigationGraphs = mutableSetOf<String>()
    private val reportedUnresolvedTargets = mutableSetOf<String>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotated = resolver.getSymbolsWithAnnotation(NavigationConstants.SCREEN_ANNOTATION).toList()
        val deferred = annotated.filterNot { it.validate() }.toMutableList()
        val functions = annotated.filterIsInstance<KSFunctionDeclaration>()
        annotated.filterNot { it is KSFunctionDeclaration }.forEach {
            logger.error("@Screen can only be applied to a top-level composable function.", it)
        }

        functions
            .mapNotNull(::readScreen)
            .forEach { screen ->
                screensByGraph.getOrPut(screen.graph) { linkedMapOf() }[screen.functionName] = screen
            }

        screensByGraph.forEach { (graph, screensByFunction) ->
            val screens = screensByFunction.values.toList()
            if (!validateRouteDeclarations(graph, screens)) return@forEach

            val routesWereGeneratedBeforeThisRound = graph in generatedRouteGraphs
            if (!routesWereGeneratedBeforeThisRound) {
                NavigationSourceGenerator.generateRoutes(
                    codeGenerator = codeGenerator,
                    logger = logger,
                    graph = graph,
                    screens = screens,
                )
                generatedRouteGraphs += graph
            }

            val unresolvedTargets = screens
                .flatMap(ScreenModel::parameters)
                .filter { it.kind == ParameterKind.NAVIGATE && it.targetRoute == null }
            if (unresolvedTargets.isNotEmpty()) {
                if (routesWereGeneratedBeforeThisRound) {
                    unresolvedTargets.forEach { parameter ->
                        val key = "$graph:${parameter.name}:${parameter.sourceNode}"
                        if (reportedUnresolvedTargets.add(key)) {
                            logger.error(
                                "@NavigateTo route could not be resolved. Use " +
                                    "<Graph>Routes.<Route>::class and declare the target with @Screen in this KSP module.",
                                parameter.sourceNode
                            )
                        }
                    }
                } else {
                    screens
                        .filter { screen ->
                            screen.parameters.any {
                                it.kind == ParameterKind.NAVIGATE && it.targetRoute == null
                            }
                        }
                        .mapNotNull { it.sourceNode as? KSAnnotated }
                        .forEach { if (it !in deferred) deferred += it }
                }
                return@forEach
            }

            if (graph in generatedNavigationGraphs) return@forEach
            if (!validateGraph(graph, screens, screensByGraph)) return@forEach

            NavigationSourceGenerator.generateNavigation(
                codeGenerator = codeGenerator,
                logger = logger,
                graph = graph,
                screens = screens,
                screensByGraph = screensByGraph.mapValues { (_, graphScreens) -> graphScreens.values.toList() },
            )
            generatedNavigationGraphs += graph
        }

        return deferred.distinct()
    }

    private fun readScreen(function: KSFunctionDeclaration): ScreenModel? {
        val screenAnnotation = function.annotations.firstOrNull { it.qualifiedName() == NavigationConstants.SCREEN_ANNOTATION }
            ?: return null
        var valid = true

        if (function.parentDeclaration != null) {
            logger.error("@Screen functions must be top-level.", function)
            valid = false
        }
        if (function.annotations.none { it.qualifiedName() == NavigationConstants.COMPOSABLE_ANNOTATION }) {
            logger.error("@Screen must be applied to a function annotated with @Composable.", function)
            valid = false
        }
        if (function.modifiers.any { it.name == "PRIVATE" }) {
            logger.error("Generated navigation entries cannot call a private @Screen function.", function)
            valid = false
        }

        val graph = screenAnnotation.stringArgument("graph").orEmpty().trim()
        val routeArgument = screenAnnotation.stringArgument("route").orEmpty().trim()
        val route = routeArgument.ifBlank { function.simpleName.asString() }
        if (!isTypeIdentifier(graph)) {
            logger.error("The @Screen graph must be a PascalCase Kotlin identifier.", function)
            valid = false
        }
        if (!isTypeIdentifier(route)) {
            logger.error("The generated route name must be a PascalCase Kotlin identifier.", function)
            valid = false
        }

        val parameters = function.parameters.mapNotNull { parameter ->
            readParameter(parameter).also { if (it == null) valid = false }
        }
        if (!valid) return null

        val packageName = function.packageName.asString()
        val qualifiedName = function.qualifiedName?.asString()
            ?: listOf(packageName, function.simpleName.asString()).filter(String::isNotBlank).joinToString(".")
        return ScreenModel(
            graph = graph,
            route = route,
            packageName = packageName,
            functionName = qualifiedName,
            parameters = parameters,
            sourceFile = function.containingFile,
            sourceNode = function
        )
    }

    private fun readParameter(parameter: KSValueParameter): ParameterModel? {
        val backAnnotation = parameter.annotations.firstOrNull { it.qualifiedName() == NavigationConstants.BACK_ANNOTATION }
        val navigateAnnotation = parameter.annotations.firstOrNull { it.qualifiedName() == NavigationConstants.NAVIGATE_ANNOTATION }
        val customActionAnnotation = parameter.annotations.firstOrNull { it.qualifiedName() == NavigationConstants.CUSTOM_ACTION_ANNOTATION }
        val koinViewModelAnnotation = parameter.annotations.firstOrNull { it.qualifiedName() == NavigationConstants.SCREEN_KOIN_VIEW_MODEL_ANNOTATION }
        val navigationAnnotations = listOfNotNull(
            backAnnotation,
            navigateAnnotation,
            customActionAnnotation,
            koinViewModelAnnotation
        )
        if (navigationAnnotations.size > 1) {
            logger.error("A parameter can use only one navigation parameter annotation.", parameter)
            return null
        }

        val name = parameter.name?.asString()
        if (name.isNullOrBlank()) {
            logger.error("Navigation screen parameters must have names.", parameter)
            return null
        }
        val type = parameter.type.asKotlinSourceType()

        return when {
            backAnnotation != null -> ParameterModel(name, type, ParameterKind.BACK)
            navigateAnnotation != null -> {
                val targetType = navigateAnnotation.classArgument("route")
                val targetDeclaration = targetType
                    ?.takeIf { !it.isError }
                    ?.declaration as? KSClassDeclaration
                val targetTypeParts = targetDeclaration?.qualifiedName?.asString()?.split('.').orEmpty()
                val targetRouteName = targetTypeParts.lastOrNull()
                val targetGraphName = targetTypeParts.dropLast(1).lastOrNull()?.removeSuffix("Routes")
                ParameterModel(
                    name = name,
                    type = type,
                    kind = ParameterKind.NAVIGATE,
                    targetRoute = targetRouteName,
                    targetGraph = targetGraphName,
                    sourceNode = parameter
                )
            }
            customActionAnnotation != null ->
                ParameterModel(name, type, ParameterKind.CUSTOM_ACTION, sourceNode = parameter)
            koinViewModelAnnotation != null ->
                ParameterModel(name, type, ParameterKind.KOIN_VIEW_MODEL, sourceNode = parameter)
            else -> ParameterModel(name, type, ParameterKind.ARGUMENT)
        }
    }

    private fun validateRouteDeclarations(graph: String, screens: List<ScreenModel>): Boolean {
        var valid = true
        val byRoute = screens.groupBy(ScreenModel::route)
        byRoute.filterValues { it.size > 1 }.forEach { (route, duplicates) ->
            duplicates.forEach { screen ->
                logger.error("Route '$route' is declared more than once in graph '$graph'.", screen.sourceFile)
            }
            valid = false
        }

        val routesType = "${graph}Routes"
        screens.filter { it.route == routesType }.forEach { screen ->
            logger.error("Route '${screen.route}' conflicts with generated type '$routesType'.", screen.sourceFile)
            valid = false
        }
        return valid
    }

    private fun validateGraph(
        graph: String,
        screens: List<ScreenModel>,
        screensByGraph: Map<String, Map<String, ScreenModel>>
    ): Boolean {
        var valid = validateRouteDeclarations(graph, screens)
        val customActions = screens.flatMap(ScreenModel::parameters)
            .filter { it.kind == ParameterKind.CUSTOM_ACTION }
            .groupBy(ParameterModel::name)
        customActions.forEach { (name, actions) ->
            if (name == "backStack") {
                actions.forEach { action ->
                    logger.error("@CustomAction parameter '$name' conflicts with a generated entry-provider parameter.", action.sourceNode)
                }
                valid = false
            }
            if (actions.map(ParameterModel::type).distinct().size > 1) {
                actions.forEach { action ->
                    logger.error(
                        "@CustomAction parameter '$name' must use the same callback type throughout graph '${graph}'.",
                        action.sourceNode
                    )
                }
                valid = false
            }
        }
        screens.forEach { screen ->
            screen.parameters
                .filter { it.kind == ParameterKind.NAVIGATE }
                .forEach { parameter ->
                    val targetGraph = parameter.targetGraph
                    val targetRoute = parameter.targetRoute
                    val targetExists = targetGraph != null && targetRoute != null &&
                        screensByGraph[targetGraph]?.values?.any { it.route == targetRoute } == true
                    if (!targetExists) {
                        val targetName = listOfNotNull(targetGraph, targetRoute).joinToString("Routes.")
                        logger.error(
                            "@NavigateTo target '$targetName' is not declared with @Screen in this KSP module.",
                            parameter.sourceNode
                        )
                        valid = false
                    }
                }
        }
        return valid
    }
}
