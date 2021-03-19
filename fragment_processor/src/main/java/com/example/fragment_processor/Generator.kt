package com.example.fragment_processor

import javax.annotation.processing.Filer
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

internal interface Generator {

    fun generate(
        packageName: String,
        originatingElement: TypeElement?,
        sourceVersion: SourceVersion,
        elementsUtils: Elements,
        filer: Filer
    )
}
