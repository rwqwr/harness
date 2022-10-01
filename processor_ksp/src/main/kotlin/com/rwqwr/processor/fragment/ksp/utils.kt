package com.rwqwr.processor.fragment.ksp

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueArgument
import kotlin.reflect.KProperty


internal inline fun <reified T> KSClassDeclaration.findArgumentsOfAnnotation(): List<KSValueArgument>? {
    return annotations
        .find { it.shortName.getShortName() == T::class.simpleName }
        ?.arguments
}

internal inline fun <reified T> List<KSValueArgument>.findValue(property: KProperty<*>): T? {
    return find { it.name?.asString().orEmpty() == property.name }
        ?.value as? T
}

