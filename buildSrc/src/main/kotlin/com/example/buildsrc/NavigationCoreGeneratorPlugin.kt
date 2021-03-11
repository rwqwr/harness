package com.example.buildsrc

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import java.io.File

open class NavigationCoreGeneratorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val parent = target.rootProject

        val extension = target.extensions.create<NavigationFileGeneratorExtension>("navigation")

        val taskName = "generateNavigationFiles"
        val task = with(target) {
            tasks.register<NavigationFileGenerateTask>(taskName) {
                val generatedDir = target.layout
                    .buildDirectory
                    .dir("generated/res/resValues")

                navigationFiles?.setFrom(extension.navigationFiles)

                val outputs = extension.buildTypes
                    .map { buildType -> generatedDir.get().dir("${buildType}/navigation") }

                outputDir?.setFrom(outputs)
            }
        }

        parent.allprojects
            .forEachIndexed { index, child ->
                child.afterEvaluate {
                    if (this == target) {
                        return@afterEvaluate
                    }

                    val androidProject = android() ?: return@afterEvaluate
                    androidProject.buildTypes.forEach { buildType ->
                        with(extension) {
                            val navigationFolderFiles = androidProject.sourceSets
                                .getByName("main").res.srcDirs
                                .first()
                                .listFiles { _, name -> name == "navigation" }.orEmpty()
                                .first()
                                .listFiles().orEmpty()

                            navigationFiles.addAll(navigationFolderFiles)

                            buildTypes.add(buildType.name)
                        }
                    }

                    if (index == parent.allprojects.size - 1) {
                        extension.buildTypes.forEach { buildType ->
                            tasks.findByPath("${target.path}:generate${buildType.capitalize()}ResValues")?.let { parentTask ->
                                parentTask.finalizedBy(":core:generateNavigationFiles")
                            }
                        }

                    }
                }
            }

    }
}

fun Project.isAndroidModule(): Boolean {
    return extensions.findByName("android") != null
}

fun Project.android(): BaseExtension? = extensions.findByType()

private val BaseExtension.variants: DomainObjectSet<out BaseVariant>?
    get() = when (this) {
        is AppExtension -> applicationVariants
        is LibraryExtension -> libraryVariants
        else -> null
    }
