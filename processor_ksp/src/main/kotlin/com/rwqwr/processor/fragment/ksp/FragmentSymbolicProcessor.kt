package com.rwqwr.processor.fragment.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.rwqwr.processor.api.FragmentsModule
import com.rwqwr.processor.api.ProvideToFactory
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo

internal class FragmentSymbolicProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotatedFiles = resolver.getSymbolsWithAnnotation(
            annotationName = ProvideToFactory::class.qualifiedName.orEmpty()
        )

        val fragmentsAnnotatedClasses = annotatedFiles
            .filterIsInstance<KSClassDeclaration>()
            .toList()

        if (fragmentsAnnotatedClasses.isEmpty()) {
            return emptyList()
        }

        val fragmentModuleClasses = resolver.getSymbolsWithAnnotation(
            annotationName = FragmentsModule::class.qualifiedName.orEmpty()
        )
            .toList()
            .filterIsInstance<KSClassDeclaration>()
            .map(::Module)

        try {
            innerProcess(
                fragmentsAnnotatedClasses = fragmentsAnnotatedClasses,
                fragmentModules = fragmentModuleClasses
            )
        } catch (e: Throwable) {
            logger.exception(e)
        }
        return emptyList()
    }

    private fun innerProcess(
        fragmentsAnnotatedClasses: List<KSClassDeclaration>,
        fragmentModules: List<Module>
    ) {
        val generators = fragmentModules.map { module ->
           module.process(fragmentsAnnotatedClasses)
        }

        val fragmentsModuleClasses = fragmentModules.map { it.declaration }
        val originatingKSFiles = (fragmentsModuleClasses + fragmentsAnnotatedClasses).mapNotNull { it.containingFile }

        generators.forEach { generator ->
            val typeSpecBuilder = generator.generate()
            val fileSpec = FileSpec.get(generator.packageName, typeSpecBuilder.build())
            fileSpec.writeTo(codeGenerator, aggregating = false, originatingKSFiles = originatingKSFiles)
        }
    }
}