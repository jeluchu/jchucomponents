package com.jeluchu.jchucomponents.navigation.ksp.utils

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType

internal fun KSAnnotation.qualifiedName(): String? =
    annotationType.resolve().declaration.qualifiedName?.asString()

internal fun KSAnnotation.stringArgument(name: String): String? =
    arguments.firstOrNull { it.name?.asString() == name }?.value as? String

internal fun KSAnnotation.classArgument(name: String): KSType? =
    arguments.firstOrNull { it.name?.asString() == name }?.value as? KSType
