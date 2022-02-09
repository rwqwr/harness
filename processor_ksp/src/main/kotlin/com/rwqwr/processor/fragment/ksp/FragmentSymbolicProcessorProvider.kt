package com.rwqwr.processor.fragment.ksp

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

internal class FragmentSymbolicProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        environment.logger.warn(environment.options.toString())
        return FragmentSymbolicProcessor(environment.codeGenerator, environment.logger)
    }
}
