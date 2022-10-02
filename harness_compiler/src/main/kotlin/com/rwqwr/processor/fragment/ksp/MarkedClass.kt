package com.rwqwr.processor.fragment.ksp

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName

internal data class MarkedClass(
    val original: KSClassDeclaration,
    val factoryProvider: ClassName,
    val supertype: ClassName,
    val mapKey: ClassName
)