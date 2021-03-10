package com.example.fragment_processor

import com.example.fragment_processor_api.FragmentsModule
import com.example.fragment_processor_api.ProvideToFactory
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.AGGREGATING)
@AutoService(Processor::class)
class Processor : AbstractProcessor() {

    private val fragmentClassName = ClassName(
        "androidx.fragment.app",
        "Fragment"
    )

    private val methodGenerator = JavaProvideMethodGenerator()

    private val moduleGenerator: Generator<com.squareup.javapoet.TypeSpec.Builder>
        get() = JavaModuleGenerator(
            methodGenerator = methodGenerator,
            elementsUtils = processingEnv.elementUtils,
            filer = processingEnv.filer
        )


    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes() = setOf(
        ProvideToFactory::class.java.canonicalName,
        FragmentsModule::class.java.canonicalName
    )

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        if (roundEnvironment.processingOver() || annotations.isNullOrEmpty()) return false

        try {
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

            val packageName = annotatedClasses
                ?.map { elementUtils.getPackageOf(it).toString().split('.').toSet() }
                ?.reduce { acc, element -> acc.intersect(element) }
                ?.joinToString(separator = ".")
                .orEmpty()

            val originatingElement = fragmentsProviderModule?.toString()?.let(elementUtils::getTypeElement)

            moduleGenerator.generate(
                originatingElement?.asClassName()?.packageName ?: packageName,
                originatingElement,
                processingEnv.sourceVersion,
                annotatedClasses.orEmpty().toList()
            )
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return false
    }
}
