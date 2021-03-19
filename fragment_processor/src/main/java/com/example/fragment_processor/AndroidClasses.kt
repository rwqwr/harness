package com.example.fragment_processor

import com.squareup.javapoet.ClassName

internal val fragmentClassName: ClassName
    get() = ClassName.get(
        "androidx.fragment.app",
        "Fragment"
    )

internal val androidFactoryClassName: ClassName
    get() = ClassName.get(
        "androidx.fragment.app",
        "FragmentFactory"
    )
