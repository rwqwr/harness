package com.example.buildsrc.dependencies

import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.dependency(dependency: Dependency): String {
    return dependency.create()
}
