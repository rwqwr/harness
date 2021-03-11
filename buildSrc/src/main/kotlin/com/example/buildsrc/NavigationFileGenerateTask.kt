package com.example.buildsrc

import groovy.util.IndentPrinter
import groovy.util.XmlParser
import groovy.util.XmlSlurper
import groovy.xml.MarkupBuilder
import groovy.xml.QName
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.tasks.*
import java.io.File

open class NavigationFileGenerateTask : DefaultTask() {

    @InputFiles
    val navigationFiles: ConfigurableFileCollection? = project.objects.fileCollection()

    @OutputDirectories
    val outputDir: ConfigurableFileCollection? = project.objects.fileCollection()

    @TaskAction
    fun generate() {
        if (outputDir == null) {
            return
        }

        navigationFiles?.forEach { file: File ->
            val outputDirFiles: Set<File> = outputDir.files


            val xmlFile = XmlParser().parse(file)

            val id = xmlFile.attributes().toList()
                .filterIsInstance<Pair<QName, String>>()
                .find { it.first.localPart == "id" }

            val navigationText = """
                <?xml version="1.0" encoding="utf-8"?>
                <navigation xmlns:android="http://schemas.android.com/apk/res/android"
                    android:id="${id?.second}"/>
            """.trimIndent()

            outputDirFiles.forEach { outputDirFile ->
                outputDirFile.mkdirs()

                val copyFile = File(outputDirFile, file.name)

                if (!copyFile.exists()) {
                    copyFile.createNewFile()
                }
                copyFile.writeText(navigationText)
            }
        }
    }
}
