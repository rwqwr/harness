package com.rwqwr.processor.fragment.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.rwqwr.processor.api.FragmentsModule
import com.rwqwr.processor.api.ProvideToFactory

internal class FragmentSymbolicProcessor() : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val fragmentsAnnotatedClasses = resolver.getSymbolsWithAnnotation(
            annotationName = ProvideToFactory::class.qualifiedName.orEmpty()
        )
        val fragmentModuleClasses = resolver.getSymbolsWithAnnotation(
            annotationName = FragmentsModule::class.qualifiedName.orEmpty()
        )
        return (fragmentsAnnotatedClasses + fragmentModuleClasses).toList()
    }
}
