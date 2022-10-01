package com.rwqwr.processor.api

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
public annotation class ProvideToFactory(
    val factoryClass: KClass<*>,
    val mapKey: KClass<*> = SetKey::class
) {

    public companion object
}

internal annotation class SetKey

public val ProvideToFactory.Companion.setKeyQualifiedName: String? get() = SetKey::class.qualifiedName