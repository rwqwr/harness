package com.example.fragment_processor

import com.example.fragment_processor_api.FragmentsModule
import com.example.fragment_processor_api.ProvideToFactory
import com.google.auto.service.AutoService
import com.squareup.javapoet.JavaFile
import com.squareup.kotlinpoet.asClassName
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@Suppress("DEPRECATION")
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.AGGREGATING)
@AutoService(Processor::class)
internal class Processor : AbstractProcessor() {

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes() = setOf(
        ProvideToFactory::class.java.canonicalName,
        FragmentsModule::class.java.canonicalName
    )

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        if (roundEnvironment.processingOver() || annotations.isNullOrEmpty()) {
            return false
        }

        try {
            processClasses(roundEnvironment)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return false
    }

    private fun processClasses(roundEnvironment: RoundEnvironment) {
        val elementUtils = processingEnv.elementUtils

        val typeElement = elementUtils.getTypeElement(
            ProvideToFactory::class.java.canonicalName
        )

        val moduleElement = elementUtils.getTypeElement(
            FragmentsModule::class.java.canonicalName
        )

        val annotatedClasses = roundEnvironment.getElementsAnnotatedWith(typeElement)

        val fragmentsProviderModule =
            roundEnvironment.getElementsAnnotatedWith(moduleElement).firstOrNull()

        val originatingElement =
            fragmentsProviderModule?.toString()?.let(elementUtils::getTypeElement)

        val generators = createGenerators(annotatedClasses.orEmpty().toList())

        generators.forEach { generator ->
            val packageName = originatingElement?.asClassName()?.packageName.orEmpty()
            val typeSpecBuilder = generator.generate(
                packageName,
                originatingElement,
                processingEnv.sourceVersion,
                processingEnv.elementUtils
            )

            JavaFile.builder(packageName, typeSpecBuilder.build())
                .build()
                .writeTo(processingEnv.filer)
        }
    }

    private fun createGenerators(elements: List<Element>): List<ClassGenerator> {
        return listOf(
            JavaModuleGenerator(
                className = MODULE_NAME,
                factoryName = FACTORY_NAME,
                elements = elements
            ),
            FragmentFactoryGenerator(
                factoryName = FACTORY_NAME
            )
        )
    }

    companion object {

        private const val MODULE_NAME = "FragmentProviderModule"
        private const val FACTORY_NAME = "FragmentFactory_Impl"
    }
}
