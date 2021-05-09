package com.example.buildsrc

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.*
import java.io.File
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

open class NavigationCoreGeneratorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val parent = target.rootProject
        val generatedDir = target.layout.buildDirectory.dir(GENERATED_PATH)

        parent.allprojects.forEach { child ->
            child.pluginManager.withPlugin("com.android.library") {
                val androidExtension = child.android()
                if (this == target || androidExtension == null) {
                    return@withPlugin
                }

                androidExtension.buildTypes.forEach { buildType ->
                    val navigationFolderFiles = androidExtension.findNavigationFolder()
                        ?.listFiles()
                        .orEmpty()

                    child.tasks.register<NavigationFileGenerateTask>(generateName(buildType.name)) {
                        navigationFiles?.setFrom(navigationFolderFiles)
                        outputDir?.set(
                            generatedDir
                                .get()
                                .dir("${buildType.name}/$NAVIGATION_FOLDER_NAME")
                        )
                    }

                    target.findGenerateResValuesTask(buildType.name)
                        ?.finalizedBy("${child.path}:${generateName(buildType.name)}")
                }
            }
        }

    }

    private fun generateName(buildType: String): String {
        return "generate${buildType.capitalize()}NavigationFiles"
    }

    private fun Project.findGenerateResValuesTask(buildTypeName: String): Task? {
        return tasks.findByPath("${path}:generate${buildTypeName.capitalize()}Resources")
    }

    private fun BaseExtension.findNavigationFolder(): File? {
        return sourceSets
            .getByName("main").res.srcDirs
            .first()
            .listFiles { _, name -> name == NAVIGATION_FOLDER_NAME }.orEmpty()
            .firstOrNull()
    }

    companion object {
        private const val GENERATED_PATH = "generated/res/resValues"
        private const val NAVIGATION_FOLDER_NAME = "navigation"
    }
}

fun Project.android(): BaseExtension? {
    return extensions.findByType()
}
