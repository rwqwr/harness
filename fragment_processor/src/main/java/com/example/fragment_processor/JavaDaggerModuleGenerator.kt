package com.example.fragment_processor

import com.squareup.javapoet.*
import dagger.Module
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

internal class JavaDaggerModuleGenerator(
    private val className: String,
    private val elements: List<Element>,
    private val additionalClassBuilder: TypeSpec.Builder.(packageName: String) -> TypeSpec.Builder = { this }
) : ClassGenerator {

    override fun generate(
        packageName: String,
        originatingElement: TypeElement?,
        sourceVersion: SourceVersion,
        elementsUtils: Elements
    ): TypeSpec.Builder {
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
            .also { originatingElement?.let(it::addOriginatingElement) }
            .generateMethods(packageName, elements, elementsUtils)
            .additionalClassBuilder(packageName)
    }

    private fun TypeSpec.Builder.generateMethods(
        packageName: String,
        elements: List<Element>,
        elementsUtils: Elements
    ): TypeSpec.Builder {
        elements.forEach { element ->
            val typeElement = elementsUtils.getTypeElement(element.toString())

            addOriginatingElement(typeElement)

            val method = JavaFragmentProviderMethodGenerator
                .generate(packageName, typeElement)
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
