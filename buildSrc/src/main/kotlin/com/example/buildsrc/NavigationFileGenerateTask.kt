package com.example.buildsrc

import groovy.util.XmlParser
import groovy.util.Node
import groovy.xml.QName
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.*
import java.io.File

@Suppress("UnstableApiUsage")
open class NavigationFileGenerateTask : DefaultTask() {

    @InputFiles
    val navigationFiles: ConfigurableFileCollection? = project.objects.fileCollection()

    @OutputDirectories
    val outputDir: DirectoryProperty? = project.objects.directoryProperty()

    @TaskAction
    fun generate() {
        if (outputDir == null) {
            return
        }

        navigationFiles?.forEach { file: File ->
            val outputDirFile: File = outputDir.asFile.get()
            outputDirFile.mkdirs()

            val id = XmlParser().parse(file).findId()

            val copyFile = File(outputDirFile, file.name)
            copyFile.writeText(generateXmlText(id.orEmpty()))
        }
    }

    private fun Node.findId(): String? {
        return attributes().toList()
            .filterIsInstance<Pair<QName, String>>()
            .find { it.first.localPart == "id" }
            ?.second
    }

    private fun generateXmlText(id: String): String {
        return """
                <?xml version="1.0" encoding="utf-8"?>
                <navigation xmlns:android="http://schemas.android.com/apk/res/android"
                    android:id="$id"/>
            """.trimIndent()
    }
}
