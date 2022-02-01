package com.rwqwr.processor.fragment.ksp.generator

import com.rwqwr.processor.fragment.ksp.fragmentClassName
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asClassName
import dagger.MapKey
import kotlin.reflect.KClass

internal const val ANNOTATION_MAP_KEY_NAME = "FragmentMapKey"

internal class KotlinFragmentKeyAnnotationClassGenerator : KotlinClassGenerator {

    override fun generate(
        packageName: String,
    ): TypeSpec.Builder {
        return TypeSpec.annotationBuilder(ClassName(packageName, ANNOTATION_MAP_KEY_NAME))
            .addModifiers(KModifier.PUBLIC)
            .addAnnotation(
                AnnotationSpec.builder(Retention::class.asClassName()).build()
            )
            .addAnnotation(
                AnnotationSpec.builder(Target::class.asClassName())
                    .addMember(
                        "%T.%L",
                        AnnotationTarget::class.java,
                        AnnotationTarget.FUNCTION,
                    )
                    .build()
            )
            .addAnnotation(MapKey::class.asClassName())
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(
                        "fragments",
                        KClass::class.asClassName().parameterizedBy(WildcardTypeName.producerOf(fragmentClassName))
                    )
                    .build()
            )

            .addProperty(
                PropertySpec.builder(
                    "fragments",
                    KClass::class.asClassName().parameterizedBy(WildcardTypeName.producerOf(fragmentClassName))
                )
                    .initializer("fragments")
                    .build()
            )
    }
}
