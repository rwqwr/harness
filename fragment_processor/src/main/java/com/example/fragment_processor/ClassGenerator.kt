package com.example.fragment_processor

import com.squareup.javapoet.TypeSpec
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

internal interface ClassGenerator {

    fun generate(
        packageName: String,
        originatingElement: TypeElement?,
        sourceVersion: SourceVersion,
        elementsUtils: Elements
    ): TypeSpec.Builder
}
