package com.example.fragment_processor_api

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ProvideToFactory(val factoryClass: KClass<*> = Any::class)
