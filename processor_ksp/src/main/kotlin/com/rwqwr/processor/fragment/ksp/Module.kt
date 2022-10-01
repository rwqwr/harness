package com.rwqwr.processor.fragment.ksp

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.rwqwr.processor.api.ProvideToFactory
import com.rwqwr.processor.fragment.ksp.generator.KotlinClassGenerator
import com.rwqwr.processor.fragment.ksp.generator.KotlinDaggerModuleGenerator
import com.squareup.kotlinpoet.ksp.toClassName

private const val MODULE_NAME = "Provider"

@JvmInline
internal value class Module(val declaration: KSClassDeclaration)

internal fun Module.process(annotatedClasses: List<KSClassDeclaration>): KotlinClassGenerator {
    val classes = annotatedClasses
        .map { classes -> classes to classes.findArgumentsOfAnnotation<ProvideToFactory>() }
        .filter { (_, annotationArguments) ->
            val factoryClass = annotationArguments?.findValue<KSType>(ProvideToFactory::factoryClass)
            factoryClass?.toClassName() == declaration.toClassName()
        }
        .map { (annotatedClass, annotationArguments) ->
            val mapKey = annotationArguments?.findValue<KSType>(ProvideToFactory::mapKey)
            MarkedClass(
                original = annotatedClass,
                mapKey = requireNotNull(mapKey?.toClassName())
            )
        }

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