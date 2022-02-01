package com.rwqwr.processor.fragment.ksp.generator

import com.rwqwr.processor.fragment.ksp.androidFactoryClassName
import com.rwqwr.processor.fragment.ksp.fragmentClassName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.jvm.jvmSuppressWildcards
import dagger.Provides
import javax.inject.Inject
import javax.inject.Provider

internal class KotlinFragmentFactoryGenerator(
    private val factoryName: String
) : KotlinClassGenerator {

    override fun generate(
        packageName: String,
    ): TypeSpec.Builder {
        return TypeSpec.classBuilder(factoryName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addAnnotation(Inject::class)
                    .addParameter(
                        ParameterSpec.builder(
                            "fragmentsClasses",
                            Map::class.asClassName().parameterizedBy(
                                Class::class.asClassName().parameterizedBy(
                                    WildcardTypeName.producerOf(fragmentClassName)
                                ),
                                Provider::class.asClassName()
                                    .parameterizedBy(fragmentClassName)
                                    .jvmSuppressWildcards()
                            )
                        )
                            .addModifiers(KModifier.PRIVATE)
                            .build()
                    )
                    .build()
            )
            .addProperty(
                PropertySpec.builder("fragmentsClasses", Map::class.asClassName().parameterizedBy(
                    Class::class.asClassName().parameterizedBy(
                        WildcardTypeName.producerOf(fragmentClassName)
                    ),
                    Provider::class.asClassName().parameterizedBy(fragmentClassName)
                        .jvmSuppressWildcards()
                ))
                    .initializer("fragmentsClasses")
                    .build()
            )
            .addFunction(
                FunSpec.builder("instantiate")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("classLoader", ClassLoader::class)
                    .addParameter("className", String::class)
                    .returns(fragmentClassName)
                    .addStatement("val fragmentKey = fragmentsClasses.keys.find { " +
                            "it.canonicalName == className " +
                            "}\n " +
                            "?: return super.instantiate(classLoader, className)"
                    )
                    .addStatement("return fragmentsClasses.getValue(fragmentKey).get()")
                    .build()
            )
            .superclass(androidFactoryClassName)
    }

    companion object {

        fun provideDaggerModuleMethod(packageName: String, fragmentFactoryName: String): FunSpec {
            return FunSpec.builder("provideFactory")
                .addParameter("factory", ClassName(packageName, fragmentFactoryName))
                .returns(androidFactoryClassName)
                .addStatement("return factory")
                .addAnnotation(Provides::class)
                .addAnnotation(JvmStatic::class)
                .build()
        }
    }
}
