package com.example.fragment_processor

import com.squareup.javapoet.*
import javax.annotation.processing.Filer
import javax.inject.Inject
import javax.inject.Provider
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

internal class FragmentFactoryGenerator(
    private val factoryName: String
) : ClassGenerator {

    override fun generate(
        packageName: String,
        originatingElement: TypeElement?,
        sourceVersion: SourceVersion,
        elementsUtils: Elements
    ): TypeSpec.Builder {
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

        return TypeSpec.classBuilder(factoryName)
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
                                if (fragment.getCanonicalName().equals(className)) {
                                    return fragmentsClasses.get(fragment).get();
                                }
                            }
                            
                            return super.instantiate(classLoader, className);
                        """.trimIndent()
                    )
                    .build()
            )
            .superclass(androidFactoryClassName)
    }
}
