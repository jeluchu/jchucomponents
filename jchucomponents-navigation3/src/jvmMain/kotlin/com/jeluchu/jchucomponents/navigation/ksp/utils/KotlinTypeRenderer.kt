package com.jeluchu.jchucomponents.navigation.ksp.utils

import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.google.devtools.ksp.symbol.KSTypeReference

internal fun KSTypeReference.asKotlinSourceType(): String = resolve().asKotlinSourceType()

internal fun KSType.asKotlinSourceType(): String {
    if (isFunctionType || isSuspendFunctionType) {
        val functionArguments = arguments.map(KSTypeArgument::asKotlinSourceType)
        val parameterTypes = functionArguments.dropLast(1)
        val returnType = functionArguments.lastOrNull() ?: "kotlin.Unit"
        val suspendPrefix = if (isSuspendFunctionType) "suspend " else ""
        val functionType = "$suspendPrefix${parameterTypes.joinToString(", ", prefix = "(", postfix = ")")} -> $returnType"
        return if (isMarkedNullable) "($functionType)?" else functionType
    }

    val typeName = declaration.qualifiedName?.asString() ?: declaration.simpleName.asString()
    val typeArguments = arguments
        .takeIf(List<KSTypeArgument>::isNotEmpty)
        ?.joinToString(", ", prefix = "<", postfix = ">") { it.asKotlinSourceType() }
        .orEmpty()
    val nullableSuffix = if (isMarkedNullable) "?" else ""
    return "$typeName$typeArguments$nullableSuffix"
}

internal fun KSTypeArgument.asKotlinSourceType(): String {
    val typeName = type?.asKotlinSourceType() ?: return "*"
    val variancePrefix = variance.label.takeIf(String::isNotBlank)?.plus(" ").orEmpty()
    return "$variancePrefix$typeName"
}

internal fun String.qualifiedTypeNames(): Set<String> =
    QUALIFIED_TYPE_PATTERN.findAll(this)
        .map { it.value }
        .filterNot { it.startsWith("kotlin.") }
        .toSet()

internal fun String.renderTypeReferences(typeReferences: Map<String, String>): String {
    var rendered = this
    val replacements = typeReferences + KOTLIN_DEFAULT_TYPE_NAMES
    replacements.entries
        .sortedByDescending { it.key.length }
        .forEach { (qualifiedName, referenceName) ->
            rendered = rendered.replace(qualifiedName, referenceName)
        }
    return rendered
}

private val QUALIFIED_TYPE_PATTERN =
    Regex("""[A-Za-z_][A-Za-z0-9_]*(?:\.[A-Za-z_][A-Za-z0-9_]*)+""")

private val KOTLIN_DEFAULT_TYPE_NAMES = mapOf(
    "kotlin.Any" to "Any",
    "kotlin.Array" to "Array",
    "kotlin.Boolean" to "Boolean",
    "kotlin.Byte" to "Byte",
    "kotlin.Char" to "Char",
    "kotlin.Double" to "Double",
    "kotlin.Float" to "Float",
    "kotlin.Int" to "Int",
    "kotlin.Long" to "Long",
    "kotlin.Nothing" to "Nothing",
    "kotlin.Pair" to "Pair",
    "kotlin.Result" to "Result",
    "kotlin.Short" to "Short",
    "kotlin.String" to "String",
    "kotlin.Throwable" to "Throwable",
    "kotlin.Triple" to "Triple",
    "kotlin.Unit" to "Unit",
    "kotlin.collections.Collection" to "Collection",
    "kotlin.collections.Iterable" to "Iterable",
    "kotlin.collections.List" to "List",
    "kotlin.collections.Map" to "Map",
    "kotlin.collections.MutableCollection" to "MutableCollection",
    "kotlin.collections.MutableList" to "MutableList",
    "kotlin.collections.MutableMap" to "MutableMap",
    "kotlin.collections.MutableSet" to "MutableSet",
    "kotlin.collections.Set" to "Set"
)
