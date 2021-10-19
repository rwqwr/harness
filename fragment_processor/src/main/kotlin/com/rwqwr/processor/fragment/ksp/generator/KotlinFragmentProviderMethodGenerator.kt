package com.rwqwr.processor.fragment.ksp.generator

import com.rwqwr.processor.fragment.kapt.generator.JavaFragmentKeyAnnotationClassGenerator.Companion.ANNOTATION_MAP_KEY_NAME
import com.rwqwr.processor.fragment.ksp.fragmentClassName
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.lang.model.element.TypeElement

internal object KotlinFragmentProviderMethodGenerator {

    fun generate(packageName: String, element: ClassName): FunSpec.Builder {
        return FunSpec.builder("provide${element.simpleName}")
            .addAnnotation(Provides::class)
            .addAnnotation(IntoMap::class)
            .addAnnotation(
                AnnotationSpec.builder(ClassName(packageName,
                    ANNOTATION_MAP_KEY_NAME
                ))
                    .addMember("%T::class", element)
                    .build()
            )
            .addAnnotation(JvmStatic::class)
            .addParameter(
                "fragment",
                element
            )
            .returns(fragmentClassName)
            .addStatement("return fragment")
    }
}
