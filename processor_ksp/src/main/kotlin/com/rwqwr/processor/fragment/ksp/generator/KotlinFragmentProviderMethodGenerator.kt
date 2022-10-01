package com.rwqwr.processor.fragment.ksp.generator

import com.rwqwr.processor.api.ProvideToFactory
import com.rwqwr.processor.api.setKeyQualifiedName
import com.rwqwr.processor.fragment.ksp.fragmentClassName
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet

internal object KotlinFragmentProviderMethodGenerator {

    fun generate(mapKey: ClassName, element: ClassName): FunSpec.Builder {
        return FunSpec.builder("provide${element.simpleName}")
            .addAnnotation(Provides::class)
            .let { builder ->
                if (mapKey.canonicalName == ProvideToFactory.Companion.setKeyQualifiedName) {
                    builder.addAnnotation(IntoSet::class)
                } else {
                    builder.addAnnotation(IntoMap::class)
                        .addAnnotation(
                            AnnotationSpec.builder(mapKey)
                                .addMember("%T::class", element)
                                .build()
                        )
                }
            }
            .addAnnotation(JvmStatic::class)
            .addParameter(
                "fragment",
                element
            )
            .returns(fragmentClassName)
            .addStatement("return fragment")
    }
}
