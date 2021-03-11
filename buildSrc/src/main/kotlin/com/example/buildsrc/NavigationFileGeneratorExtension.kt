package com.example.buildsrc

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import java.io.File

open class NavigationFileGeneratorExtension {

    val navigationFiles: MutableSet<File> = mutableSetOf()

    val buildTypes: MutableSet<String> = mutableSetOf()
}
