package com.rwqwr.processor.fragment.ksp.generator

import com.squareup.kotlinpoet.TypeSpec

internal interface KotlinClassGenerator {

    val packageName: String

    fun generate(): TypeSpec.Builder
}
