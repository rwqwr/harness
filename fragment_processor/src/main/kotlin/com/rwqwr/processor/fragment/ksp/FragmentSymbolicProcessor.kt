package com.rwqwr.processor.fragment.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.rwqwr.processor.api.FragmentsModule
import com.rwqwr.processor.api.ProvideToFactory
import com.rwqwr.processor.fragment.ksp.generator.KotlinDaggerModuleGenerator
import com.rwqwr.processor.fragment.ksp.generator.KotlinFragmentKeyAnnotationClassGenerator
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

internal class FragmentSymbolicProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("Run KSP Processor")
        val fragmentsAnnotatedClasses = resolver.getSymbolsWithAnnotation(
            annotationName = ProvideToFactory::class.qualifiedName.orEmpty()
        )
            .toList()
            .filterIsInstance<KSClassDeclaration>()

        val fragmentModuleClasses = resolver.getSymbolsWithAnnotation(
            annotationName = FragmentsModule::class.qualifiedName.orEmpty()
        )
            .toList()
            .filterIsInstance<KSClassDeclaration>()

        logger.warn("fragmentsAnnotatedClasses $fragmentsAnnotatedClasses")
        logger.warn("fragmentModuleClasses $fragmentModuleClasses")

        try {
            processClasses(fragmentsAnnotatedClasses, fragmentModuleClasses)
        } catch (e: Throwable) {
            logger.exception(e)
        }

        return emptyList()
    }

    @OptIn(KspExperimental::class, com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview::class)
    private fun processClasses(fragments: List<KSClassDeclaration>, fragmentsModules: List<KSClassDeclaration>) {
        val fragmentsProviderModule = fragmentsModules.firstOrNull()
            ?: return

        val needCreateFactory =
            fragmentsProviderModule.getAnnotationsByType(FragmentsModule::class).first().generateFactory

        val generators = buildList {
            add(KotlinFragmentKeyAnnotationClassGenerator())
            add(KotlinDaggerModuleGenerator(MODULE_NAME, fragments.map { it.toClassName() }))
        }

        generators.forEach { generator ->
            val packageName = fragmentsProviderModule.packageName.asString()
            val typeSpecBuilder = generator.generate(packageName)

            logger.warn(packageName)

            val fileSpec = FileSpec.get(packageName, typeSpecBuilder.build())

            logger.warn(fileSpec.toString())

            fileSpec.writeTo(codeGenerator, aggregating = true)

            logger.warn(codeGenerator.generatedFile.joinToString { it.path + ',' })
        }
    }

    companion object {

        private const val MODULE_NAME = "FragmentProviderModule"
        private const val FACTORY_NAME = "FragmentFactory_Impl"
    }
}
