package com.rwqwr.processor.fragment.ksp.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import dagger.Module

internal class KotlinDaggerModuleGenerator(
    private val className: String,
    private val elements: List<ClassName>,
    private val additionalClassBuilder: TypeSpec.Builder.(packageName: String) -> TypeSpec.Builder = { this }
) : KotlinClassGenerator {

    override fun generate(
        packageName: String,
    ): TypeSpec.Builder {
        return TypeSpec.objectBuilder(className)
            .addModifiers(KModifier.PUBLIC)
            .addAnnotation(Module::class)
            .generateMethods(packageName, elements)
            .additionalClassBuilder(packageName)
    }

    private fun TypeSpec.Builder.generateMethods(
        packageName: String,
        elements: List<ClassName>,
    ): TypeSpec.Builder {
        elements.forEach { element ->
            val method = KotlinFragmentProviderMethodGenerator.generate(packageName, element).build()
            addFunction(method)
        }
        return this
    }
}
