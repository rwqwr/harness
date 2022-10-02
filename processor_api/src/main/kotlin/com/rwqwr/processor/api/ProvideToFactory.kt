package com.rwqwr.processor.api

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
public annotation class ProvideToFactory(
    val factoryClass: KClass<*>,
    val supertype: KClass<*>,
    val mapKey: KClass<*>,
) {

    public companion object
}
