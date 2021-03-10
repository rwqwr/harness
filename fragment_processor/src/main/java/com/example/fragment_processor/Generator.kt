package com.example.fragment_processor

import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.Filer
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

interface Generator<T> {

    fun generate(
        packageName: String,
        originatingElement: TypeElement?,
        sourceVersion: SourceVersion,
        elements: List<Element>
    )
}
