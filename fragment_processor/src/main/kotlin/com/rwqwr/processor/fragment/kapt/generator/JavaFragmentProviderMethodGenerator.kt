package com.rwqwr.processor.fragment.kapt.generator

import com.rwqwr.processor.fragment.fragmentClassName
import com.squareup.javapoet.*
import dagger.Binds
import dagger.multibindings.IntoMap
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

internal object JavaFragmentProviderMethodGenerator {

    fun generate(packageName: String, element: TypeElement): MethodSpec.Builder {
        return MethodSpec.methodBuilder("provide${element.simpleName}")
            .addModifiers(Modifier.ABSTRACT)
            .addAnnotation(Binds::class.java)
            .addAnnotation(IntoMap::class.java)
            .addAnnotation(
                AnnotationSpec.builder(ClassName.get(packageName,
                    JavaFragmentKeyAnnotationClassGenerator.ANNOTATION_MAP_KEY_NAME
                ))
                    .addMember("value","\$T.class", TypeName.get(element.asType()))
                    .build()
            )
            .addParameter(
                TypeName.get(element.asType()),
                "fragment"
            )
            .returns(
                fragmentClassName
            )
    }
}
