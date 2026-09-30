package com.jeluchu.jchucomponents.navigation.ksp.models

import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSNode

internal enum class ParameterKind {
    ARGUMENT,
    BACK,
    NAVIGATE,
    CUSTOM_ACTION,
    KOIN_VIEW_MODEL
}

internal data class ParameterModel(
    val name: String,
    val type: String,
    val kind: ParameterKind,
    val targetRoute: String? = null,
    val targetGraph: String? = null,
    val sourceNode: KSNode? = null
)

internal data class ScreenModel(
    val graph: String,
    val route: String,
    val packageName: String,
    val functionName: String,
    val parameters: List<ParameterModel>,
    val sourceFile: KSFile?,
    val sourceNode: KSNode? = null
)
