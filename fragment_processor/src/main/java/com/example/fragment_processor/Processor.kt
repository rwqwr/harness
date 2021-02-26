package com.example.fragment_processor

import com.example.fragment_processor_api.FragmentsModule
import com.example.fragment_processor_api.ProvideToFactory
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.jvm.jvmSuppressWildcards
import com.squareup.kotlinpoet.jvm.jvmWildcard
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Provider
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@Suppress("DEPRECATION")
@KotlinPoetMetadataPreview
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.AGGREGATING)
@AutoService(Processor::class)
class Processor : AbstractProcessor() {

    private val fragmentClassName = ClassName(
        "androidx.fragment.app",
        "Fragment"
    )

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes() = setOf(
        ProvideToFactory::class.java.canonicalName,
        FragmentsModule::class.java.canonicalName
    )

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        if (roundEnvironment.processingOver() || annotations.isNullOrEmpty()) return false

        try {
            val elementUtils = processingEnv.elementUtils

            val typeElement = elementUtils.getTypeElement(
                ProvideToFactory::class.java.canonicalName
            )

            val moduleElement = elementUtils.getTypeElement(
                FragmentsModule::class.java.canonicalName
            )

            val annotatedClasses = roundEnvironment.getElementsAnnotatedWith(typeElement)

            val fragmentsProviderModule =
                roundEnvironment.getElementsAnnotatedWith(moduleElement).firstOrNull()

            val packageName = annotatedClasses
                ?.map { elementUtils.getPackageOf(it).toString().split('.').toSet() }
                ?.reduce { acc, element -> acc.intersect(element) }
                ?.joinToString(separator = ".")
                .orEmpty()

            createKotlinModule(packageName, fragmentsProviderModule) {
                annotatedClasses?.forEach { element ->
                    val classTypeElememt = elementUtils.getTypeElement(element.toString())

                    val funSpec = FunSpec.builder("provide${element.simpleName}")
                        .addAnnotation(Provides::class)
                        .addAnnotation(IntoMap::class)
                        .addAnnotation(
                            AnnotationSpec.builder(ClassKey::class)
                                .addMember("%T::class", classTypeElememt.asType().asTypeName())
                                .build()
                        )
                        .addAnnotation(JvmStatic::class)
                        .addParameter(
                            "fragment",
                            classTypeElememt.asType().asTypeName()
                        )
                        .returns(
                            ClassName(
                                "androidx.fragment.app",
                                "Fragment"
                            )
                        )
                        .addStatement("return fragment")
                        .build()

                    addFunction(funSpec)
                }
                this
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return false
    }

    private fun createKotlinModule(
        packageName: String,
        originatingElement: Element?,
        methodBuilder: TypeSpec.Builder.() -> TypeSpec.Builder
    ) {
        val className = "FragmentProviderModule"

        val factoryName = "FragmentFactory_Impl"

        val androidFactoryClassName = ClassName("androidx.fragment.app", "FragmentFactory")

        val factoryProvider = FunSpec.builder("provideFactory")
            .addParameter("factory", ClassName(packageName, factoryName))
            .returns(androidFactoryClassName)
            .addStatement("return factory")
            .addAnnotation(Provides::class)
            .addAnnotation(JvmStatic::class)
            .build()


        val typeSpec = TypeSpec.objectBuilder(className)
            .addAnnotation(Module::class)
            .methodBuilder()
            .addFunction(factoryProvider)
            .also { originatingElement?.let(it::addOriginatingElement) }
            .build()

        FileSpec.builder(packageName, className)
            .addType(typeSpec)
            .build()
            .writeTo(processingEnv.filer)

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
                    .addStatement("val fragmentKey = fragmentsClasses.keys.find { it.canonicalName == className }\n ?: return super.instantiate(classLoader, className)")
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
            .writeTo(processingEnv.filer)
    }
}
