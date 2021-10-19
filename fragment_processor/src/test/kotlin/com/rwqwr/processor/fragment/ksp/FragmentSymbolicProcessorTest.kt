package com.rwqwr.processor.fragment.ksp

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class FragmentSymbolicProcessorTest {

    @Test
    fun test() {
        val kotlinSource = SourceFile.kotlin(
            "KClass.kt", """
                package test
                
                @com.rwqwr.processor.api.ProvideToFactory
                @com.rwqwr.processor.api.FragmentsModule
                class PrimaryFragment
            """.trimIndent()
        )

        val compilation = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            symbolProcessorProviders = listOf(FragmentSymbolicProcessorProvider())
        }
        val result = compilation.compile()
    }
}
