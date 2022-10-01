package com.rwqwr.processor.fragment.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.rwqwr.processor.api.FactoryKey
import com.rwqwr.processor.api.FragmentsModule
import com.rwqwr.processor.api.ProvideToFactory
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.toClassName
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

        val customAnnotatedClasses = fragmentsAnnotatedClasses.filter {
            it.findAnnotation<FactoryKey>() != null
        }

        val classAnnotatedCustomKey = customAnnotatedClasses.flatMap { customAnnotatedClass ->
            val arguments = customAnnotatedClass.findArgumentsOfAnnotation<ProvideToFactory>()
            val factoryProvider = arguments?.findValue<KSType>(ProvideToFactory::factoryClass)
            val mapKey = arguments?.findValue<KSType>(ProvideToFactory::mapKey)

            resolver.getSymbolsWithAnnotation(customAnnotatedClass.toClassName().canonicalName)
                .filterIsInstance<KSClassDeclaration>()
                .map { annotatedClass ->
                    MarkedClass(
                        original = annotatedClass,
                        factoryProvider = requireNotNull(factoryProvider?.toClassName()),
                        mapKey = requireNotNull(mapKey?.toClassName())
                    )
                }
        }

        val classAnnotatedRaw = fragmentsAnnotatedClasses.toSet().subtract(customAnnotatedClasses.toSet())
            .map { annotatedClass ->
                val arguments = annotatedClass.findArgumentsOfAnnotation<ProvideToFactory>()
                val factoryProvider = arguments?.findValue<KSType>(ProvideToFactory::factoryClass)
                val mapKey = arguments?.findValue<KSType>(ProvideToFactory::mapKey)

                MarkedClass(
                    original = annotatedClass,
                    factoryProvider = requireNotNull(factoryProvider?.toClassName()),
                    mapKey = requireNotNull(mapKey?.toClassName())
                )
            }

        val allAnnotatedClasses = classAnnotatedCustomKey + classAnnotatedRaw

        if (allAnnotatedClasses.isEmpty()) {
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
                annotatedClasses = allAnnotatedClasses,
                fragmentModules = fragmentModuleClasses
            )
        } catch (e: Throwable) {
            logger.exception(e)
        }
        return emptyList()
    }

    private fun innerProcess(
        annotatedClasses: List<MarkedClass>,
        fragmentModules: List<Module>
    ) {
        val generators = fragmentModules.map { module ->
            module.process(annotatedClasses)
        }

        val fragmentsModuleClasses = fragmentModules.map { it.declaration }
        val annotatedClassesDeclarations = annotatedClasses.map { it.original }
        val originatingKSFiles = (fragmentsModuleClasses + annotatedClassesDeclarations).mapNotNull { it.containingFile }

        generators.forEach { generator ->
            val typeSpecBuilder = generator.generate()
            val fileSpec = FileSpec.get(generator.packageName, typeSpecBuilder.build())
            fileSpec.writeTo(codeGenerator, aggregating = false, originatingKSFiles = originatingKSFiles)
        }
    }
}