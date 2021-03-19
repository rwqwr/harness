package com.example.buildsrc.dependencies

import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import com.example.buildsrc.extensions.EMPTY
import org.gradle.api.artifacts.ModuleDependency as ModuleDependencyArtifact

class Group(
    private val groupName: String,
    override val version: String? = null
) : Dependency by DefaultGroupDependency(groupName, version)

class Subgroup(
    private val group: Dependency,
    private val subgroupName: String,
    override val version: String? = null
) : Dependency by DefaultGroupDependency("${group.path}.$subgroupName", version)

class Module(
    override val group: Dependency,
    private val name: String,
    private val versionProvider: Dependency.(String) -> String?
) : ModuleDependency by DefaultModuleDependency(group, name, versionProvider)

interface Dependency {

    val path: String

    val version: String?

    fun create(): String
}

interface ModuleDependency : Dependency {

    val group: Dependency
}

class DefaultGroupDependency(
    private val name: String,
    override val version: String? = null
) : Dependency {

    override val path: String
        get() = name

    override fun create(): String {
        return name
    }
}

class DefaultModuleDependency(
    override val group: Dependency,
    private val name: String,
    private val versionProvider: Dependency.(String) -> String?
) : ModuleDependency {

    override val path: String
        get() = "${group.path}:$name"

    override val version: String?
        get() = versionProvider.invoke(group, name)

    override fun create(): String {
        val version = versionProvider.invoke(group, name)
        return "${group.create()}:$name:$version"
    }
}
