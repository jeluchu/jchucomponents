package com.jeluchu.jchucomponents.navigation.ksp

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.jeluchu.jchucomponents.navigation.ksp.processor.NavigationProcessor

/** KSP entry point discovered through META-INF/services. */
class NavigationProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        NavigationProcessor(environment.codeGenerator, environment.logger)
}
