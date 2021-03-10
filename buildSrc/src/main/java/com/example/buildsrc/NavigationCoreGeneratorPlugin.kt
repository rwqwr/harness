package com.example.buildsrc

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

open class NavigationCoreGeneratorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val parent = target.rootProject
        target.afterEvaluate {
            parent.allprojects
                .filter(Project::isAndroidModule)
                .map { it.resources }
        }
    }
}

fun Project.isAndroidModule() : Boolean {
    return extensions.findByName("android") != null
}

fun Project.android(): BaseExtension? = extensions.findByType()
