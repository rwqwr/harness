package com.rwqwr.processor.fragment.ksp.generator

import com.rwqwr.processor.api.SetKey
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet

internal object KotlinFragmentProviderMethodGenerator {

    fun generate(mapKey: ClassName, element: ClassName, supertype: ClassName): FunSpec.Builder {
        return FunSpec.builder("provide${element.simpleName}")
            .addAnnotation(Provides::class)
            .let { builder ->
                if (mapKey.canonicalName == SetKey::class.qualifiedName) {
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
                "instance",
                element
            )
            .returns(supertype)
            .addStatement("return instance")
    }
}
