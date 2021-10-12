package com.rwqwr.processor.fragment

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.WildcardTypeName
import dagger.MapKey
import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

internal class JavaFragmentKeyAnnotationClassGenerator : ClassGenerator {

    override fun generate(
        packageName: String,
        originatingElement: TypeElement?,
        sourceVersion: SourceVersion,
        elementsUtils: Elements
    ): TypeSpec.Builder {
        return TypeSpec.annotationBuilder(ClassName.get(packageName, ANNOTATION_MAP_KEY_NAME))
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(
                AnnotationSpec.builder(java.lang.annotation.Retention::class.java)
                    .addMember("value", "\$T.\$L", RetentionPolicy::class.java, RetentionPolicy.RUNTIME)
                    .build()
            )
            .addAnnotation(
                AnnotationSpec.builder(java.lang.annotation.Target::class.java)
                    .addMember(
                        "value",
                        "{\$T.\$L, \$T.\$L}",
                        ElementType::class.java,
                        ElementType.METHOD,
                        ElementType::class.java,
                        ElementType.FIELD
                    )
                    .build()
            )
            .addAnnotation(MapKey::class.java)
            .addMethod(
                MethodSpec.methodBuilder("value")
                    .returns(
                        ParameterizedTypeName.get(
                            ClassName.get(Class::class.java),
                            WildcardTypeName.subtypeOf(fragmentClassName)
                        )
                    )
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .build()
            )
    }

    companion object {

        const val ANNOTATION_MAP_KEY_NAME = "FragmentMapKey"
    }
}
