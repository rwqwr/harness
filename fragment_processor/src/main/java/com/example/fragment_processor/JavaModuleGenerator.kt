package com.example.fragment_processor

import com.squareup.javapoet.*
import dagger.Binds
import dagger.Module
import javax.annotation.processing.Filer
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

internal class JavaModuleGenerator(
    private val className: String,
    private val factoryName: String,
    private val elements: List<Element>,
) : ClassGenerator {

    override fun generate(
        packageName: String,
        originatingElement: TypeElement?,
        sourceVersion: SourceVersion,
        elementsUtils: Elements
    ): TypeSpec.Builder {
        val factoryProvider = MethodSpec.methodBuilder("provideFactory")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addParameter(ClassName.get(packageName, factoryName), "factory")
            .returns(androidFactoryClassName)
            .addAnnotation(Binds::class.java)
            .build()

        return TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addAnnotation(Module::class.java)
            .also {
                createGeneratedAnnotation(
                    sourceVersion,
                    elementsUtils
                )?.let(it::addAnnotation)
            }
            .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build())
            .addMethod(factoryProvider)
            .also { originatingElement?.let(it::addOriginatingElement) }
            .generateMethods(elements, elementsUtils)
    }

    private fun TypeSpec.Builder.generateMethods(
        elements: List<Element>,
        elementsUtils: Elements
    ): TypeSpec.Builder {
        elements.forEach { element ->
            val typeElement = elementsUtils.getTypeElement(element.toString())

            addOriginatingElement(typeElement)

            val method = JavaProvideFragmentMethodGenerator
                .generate(typeElement)
                .build()
            addMethod(method)
        }
        return this
    }

    private fun createGeneratedAnnotation(
        sourceVersion: SourceVersion,
        elements: Elements
    ): AnnotationSpec? {
        val annotationTypeName = when {
            sourceVersion <= SourceVersion.RELEASE_8 -> "javax.annotation.Generated"
            else -> "javax.annotation.processing.Generated"
        }
        val generatedType = elements.getTypeElement(annotationTypeName)
            ?: return null

        return AnnotationSpec.builder(ClassName.get(generatedType))
            .addMember("value", "\$S", javaClass.name)
            .build()
    }
}
