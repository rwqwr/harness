package com.rwqwr.processor.fragment.ksp

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.rwqwr.processor.fragment.ksp.generator.KotlinClassGenerator
import com.rwqwr.processor.fragment.ksp.generator.KotlinDaggerModuleGenerator
import com.squareup.kotlinpoet.ksp.toClassName

private const val MODULE_NAME = "Provider"

@JvmInline
internal value class Module(val declaration: KSClassDeclaration)

internal fun Module.process(annotatedClasses: List<MarkedClass>): KotlinClassGenerator {
    val classes = annotatedClasses
        .filter { it.factoryProvider == declaration.toClassName() }

    return processClasses(classes)
}

private fun Module.processClasses(
    fragments: List<MarkedClass>,
): KotlinClassGenerator {
    val packageName = declaration.packageName.asString()

    return KotlinDaggerModuleGenerator(
        packageName = packageName,
        className = declaration.simpleName.asString() + MODULE_NAME,
        elements = fragments
    )
}