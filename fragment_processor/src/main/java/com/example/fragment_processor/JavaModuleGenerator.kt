package com.example.fragment_processor

import com.squareup.javapoet.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.annotation.Generated
import javax.annotation.processing.Filer
import javax.inject.Inject
import javax.inject.Provider
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

class JavaModuleGenerator(
    private val className: String = "FragmentProviderModule",
    private val factoryName: String = "FragmentFactory_Impl",
    private val methodGenerator: MethodGenerator<MethodSpec.Builder>,
    private val elementsUtils: Elements,
    private val filer: Filer
) : Generator<TypeSpec.Builder> {
    private val fragmentClassName = ClassName.get(
        "androidx.fragment.app",
        "Fragment"
    )

    private val androidFactoryClassName = ClassName.get(
        "androidx.fragment.app",
        "FragmentFactory"
    )

    override fun generate(
        packageName: String,
        originatingElement: TypeElement?,
        sourceVersion: SourceVersion,
        elements: List<Element>
    ) {

        val factoryProvider = MethodSpec.methodBuilder("provideFactory")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addParameter(ClassName.get(packageName, factoryName), "factory")
            .returns(androidFactoryClassName)
//            .addStatement("return factory")
            .addAnnotation(Binds::class.java)
            .build()

        val typeSpec = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addAnnotation(Module::class.java)
            .also {
                createGeneratedAnnotation(
                    sourceVersion,
                    elementsUtils
                )?.let(it::addAnnotation)
            }
            .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build())
            .addMethod(factoryProvider)
            .also { originatingElement?.let(it::addOriginatingElement) }
            .generateElements(originatingElement, elements)
            .build()

        JavaFile.builder(packageName, typeSpec)
            .build()
            .writeTo(filer)

        val fragmentsMapType = ParameterizedTypeName.get(
            ClassName.get(Map::class.java),
            ParameterizedTypeName.get(
                ClassName.get(Class::class.java),
                WildcardTypeName.subtypeOf(Any::class.java)
            ),
            ParameterizedTypeName.get(
                ClassName.get(Provider::class.java),
                fragmentClassName
            )
        )

        val factoryTypeSpec = TypeSpec.classBuilder(factoryName)
            .addMethod(
                MethodSpec.constructorBuilder()
                    .addAnnotation(Inject::class.java)
                    .addParameter(
                        ParameterSpec.builder(
                            fragmentsMapType,
                            "fragmentsClasses"
                        )
                            .build()
                    )
                    .addStatement("this.fragmentsClasses = fragmentsClasses")
                    .build()
            )
            .addField(
                FieldSpec.builder(fragmentsMapType, "fragmentsClasses")
                    .addModifiers(Modifier.PRIVATE)
                    .build()
            )
            .addMethod(
                MethodSpec.methodBuilder("instantiate")
                    .addAnnotation(Override::class.java)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ClassName.get(ClassLoader::class.java), "classLoader")
                    .addParameter(ClassName.get(String::class.java), "className")
                    .returns(fragmentClassName)
                    .addCode(
                        """
                            for (Class fragment: fragmentsClasses.keySet()) {
                                if (fragment.getCanonicalName() == className) {
                                    return fragmentsClasses.get(fragment).get();
                                }
                            }
                            
                            return super.instantiate(classLoader, className);
                        """.trimIndent()
                    )
                    .build()
            )
            .superclass(androidFactoryClassName)
            .build()

        JavaFile.builder(packageName, factoryTypeSpec)
            .build()
            .writeTo(filer)
    }

    private fun TypeSpec.Builder.generateElements(
        originatingElement: TypeElement?,
        elements: List<Element>
    ): TypeSpec.Builder {
        elements.forEach { element ->
            val typeElement = elementsUtils.getTypeElement(element.toString())

            addOriginatingElement(typeElement)

            val method = methodGenerator
                .generate(typeElement)
                .build()
            addMethod(method)
        }
        return this
    }

    private fun createGeneratedAnnotation(
        sourceVersion: SourceVersion,
        elements: Elements
    ): AnnotationSpec? {
        val annotationTypeName = when {
            sourceVersion <= SourceVersion.RELEASE_8 -> "javax.annotation.Generated"
            else -> "javax.annotation.processing.Generated"
        }
        val generatedType = elements.getTypeElement(annotationTypeName)
            ?: return null
        return AnnotationSpec.builder(ClassName.get(generatedType))
            .addMember("value", "\$S", javaClass.name)
            .addMember("comments", "\$S", "https://github.com/square/AssistedInject")
            .build()
    }
}
