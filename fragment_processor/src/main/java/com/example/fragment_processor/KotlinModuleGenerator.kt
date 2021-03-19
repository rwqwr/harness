package com.example.fragment_processor

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.jvm.jvmSuppressWildcards
import dagger.Module
import dagger.Provides
import javax.annotation.processing.Filer
import javax.inject.Inject
import javax.inject.Provider
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

internal class KotlinModuleGenerator(
    private val className: String = "FragmentProviderModule",
    private val factoryName: String = "FragmentFactory_Impl",
    private val methodGenerator: MethodGenerator<FunSpec.Builder>,
    private val elementsUtils: Elements,
    private val filer: Filer
) : Generator<TypeSpec.Builder> {

    private val fragmentClassName = ClassName(
        "androidx.fragment.app",
        "Fragment"
    )

    private val androidFactoryClassName = ClassName(
        "androidx.fragment.app",
        "FragmentFactory"
    )

    override fun generate(
        packageName: String,
        originatingElement: TypeElement?,
        sourceVersion: SourceVersion,
        elements: List<Element>
    ) {

        val factoryProvider = FunSpec.builder("provideFactory")
            .addParameter("factory", ClassName(packageName, factoryName))
            .returns(androidFactoryClassName)
            .addStatement("return factory")
            .addAnnotation(Provides::class)
            .addAnnotation(JvmStatic::class)
            .build()

        val typeSpec = TypeSpec.objectBuilder(className)
            .addModifiers(KModifier.INTERNAL)
            .addAnnotation(Module::class)
            .addFunction(factoryProvider)
            .also { originatingElement?.let(it::addOriginatingElement) }
            .generateElements(elements)
            .build()

        FileSpec.builder(packageName, className)
            .addType(typeSpec)
            .build()
            .writeTo(filer)

        val factoryTypeSpec = TypeSpec.classBuilder(factoryName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addAnnotation(Inject::class)
                    .addParameter(
                        ParameterSpec.builder(
                            "fragmentsClasses",
                            Map::class.asClassName().parameterizedBy(
                                Class::class.asClassName().parameterizedBy(
                                    WildcardTypeName.producerOf(
                                        Any::class.asClassName().copy(nullable = true)
                                    )
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
                        WildcardTypeName.producerOf(
                            Any::class.asClassName().copy(nullable = true)
                        )
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
            .build()

        FileSpec.builder(
            packageName, factoryName
        )
            .addType(factoryTypeSpec)
            .build()
            .writeTo(filer)
    }

    private fun TypeSpec.Builder.generateElements(elements: List<Element>): TypeSpec.Builder {
        elements.forEach { element ->
            val typeElement = elementsUtils.getTypeElement(element.toString())
            val method =  methodGenerator.generate(typeElement).build()
            addFunction(method)
        }
        return this
    }
}
