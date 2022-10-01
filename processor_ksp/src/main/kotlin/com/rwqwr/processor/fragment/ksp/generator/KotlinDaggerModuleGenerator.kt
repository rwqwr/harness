package com.rwqwr.processor.fragment.ksp.generator

import com.rwqwr.processor.fragment.ksp.MarkedClass
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import dagger.Module

internal class KotlinDaggerModuleGenerator(
    override val packageName: String,
    private val className: String,
    private val elements: List<MarkedClass>,
    private val additionalClassBuilder: TypeSpec.Builder.(packageName: String) -> TypeSpec.Builder = { this }
) : KotlinClassGenerator {

    override fun generate(): TypeSpec.Builder {
        return TypeSpec.objectBuilder(className)
            .addModifiers(KModifier.PUBLIC)
            .addAnnotation(Module::class)
            .generateMethods(elements)
            .additionalClassBuilder(packageName)
    }

    private fun TypeSpec.Builder.generateMethods(
        elements: List<MarkedClass>,
    ): TypeSpec.Builder {
        elements.forEach { element ->
            val method = KotlinFragmentProviderMethodGenerator.generate(
                element.mapKey,
                element.original.toClassName()
            )
            addFunction(method.build())
        }
        return this
    }
}
