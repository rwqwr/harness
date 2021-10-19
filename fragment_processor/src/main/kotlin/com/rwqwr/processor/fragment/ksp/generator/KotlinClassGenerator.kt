package com.rwqwr.processor.fragment.ksp.generator

import com.squareup.kotlinpoet.TypeSpec
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

internal interface KotlinClassGenerator {

    fun generate(
        packageName: String,
    ): TypeSpec.Builder
}
