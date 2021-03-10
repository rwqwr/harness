package com.example.fragment_processor

import com.squareup.javapoet.*
import dagger.Binds
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class JavaProvideMethodGenerator : MethodGenerator<MethodSpec.Builder> {

    override fun generate(element: TypeElement, block: MethodSpec.Builder.() -> MethodSpec.Builder): MethodSpec.Builder {
        return MethodSpec.methodBuilder("provide${element.simpleName}")
            .addModifiers(Modifier.ABSTRACT)
            .addAnnotation(Binds::class.java)
            .addAnnotation(IntoMap::class.java)
            .addAnnotation(
                AnnotationSpec.builder(ClassKey::class.java)
                    .addMember("value","\$T.class", TypeName.get(element.asType()))
                    .build()
            )
            .addParameter(
                TypeName.get(element.asType()),
                "fragment"
            )
            .returns(
                ClassName.get(
                    "androidx.fragment.app",
                    "Fragment"
                )
            )
            .block()
    }
}
