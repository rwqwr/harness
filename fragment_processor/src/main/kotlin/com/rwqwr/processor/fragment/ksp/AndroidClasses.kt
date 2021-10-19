package com.rwqwr.processor.fragment.ksp

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

internal val fragmentClassName: ClassName
    get() = ClassName(
        "androidx.fragment.app",
        "Fragment"
    )

internal val androidFactoryClassName: ClassName
    get() = ClassName(
        "androidx.fragment.app",
        "FragmentFactory"
    )
