package com.rwqwr.processor.fragment

import com.rwqwr.processor.api.FragmentsModule
import com.rwqwr.processor.api.ProvideToFactory
import com.squareup.javapoet.JavaFile
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@Suppress("DEPRECATION")
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

        val needCreateFactory = fragmentsProviderModule?.getAnnotation(FragmentsModule::class.java)?.generateFactory ?: false
        val markedClasses = annotatedClasses.orEmpty().toList()
        val generators = buildList {
            if (needCreateFactory) {
                add(JavaFragmentFactoryGenerator(FACTORY_NAME))
            }

            add(JavaFragmentKeyAnnotationClassGenerator())

            add(JavaDaggerModuleGenerator(MODULE_NAME, markedClasses) { packageName ->
                if (needCreateFactory) {
                    val factoryProvider = JavaFragmentFactoryGenerator.provideDaggerModuleMethod(packageName, FACTORY_NAME)
                    addMethod(factoryProvider)
                } else {
                    this
                }
            })
        }

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

    companion object {

        private const val MODULE_NAME = "FragmentProviderModule"
        private const val FACTORY_NAME = "FragmentFactory_Impl"
    }
}
