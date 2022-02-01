package com.rwqwr.processor.fragment.ksp.generator

import com.squareup.kotlinpoet.TypeSpec

internal interface KotlinClassGenerator {

    fun generate(
        packageName: String,
    ): TypeSpec.Builder
}
