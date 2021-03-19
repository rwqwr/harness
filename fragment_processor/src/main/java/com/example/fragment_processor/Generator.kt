package com.example.fragment_processor

import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

internal interface Generator<T> {

    fun generate(
        packageName: String,
        originatingElement: TypeElement?,
        sourceVersion: SourceVersion,
        elements: List<Element>
    )
}
