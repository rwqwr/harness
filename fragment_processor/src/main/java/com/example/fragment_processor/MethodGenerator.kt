package com.example.fragment_processor

import com.squareup.javapoet.MethodSpec
import javax.lang.model.element.TypeElement

interface MethodGenerator<T> {

    fun generate(element: TypeElement, block: T.() -> T = { this }): T
}