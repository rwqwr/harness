package com.example.fragment_processor

import com.squareup.kotlinpoet.*
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.lang.model.element.TypeElement

internal class KotlinProvideMethodGenerator : MethodGenerator<FunSpec.Builder> {

    override fun generate(
        element: TypeElement,
        block: FunSpec.Builder.() -> FunSpec.Builder
    ): FunSpec.Builder {
        return FunSpec.builder("provide${element.simpleName}")
            .addAnnotation(Provides::class)
            .addAnnotation(IntoMap::class)
            .addAnnotation(
                AnnotationSpec.builder(ClassKey::class)
                    .addMember("%T::class", element.asType().asTypeName())
                    .build()
            )
            .addAnnotation(JvmStatic::class)
            .addParameter(
                "fragment",
                element.asType().asTypeName()
            )
            .returns(
                ClassName(
                    "androidx.fragment.app",
                    "Fragment"
                )
            )
            .addStatement("return fragment")
    }
}
