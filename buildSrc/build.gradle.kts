import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.useIR = true
compileKotlin.kotlinOptions.jvmTarget = "11"

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.0.0")
}

gradlePlugin {
    plugins {
        register("navigation-core") {
            id = "navigation-core"
            implementationClass = "com.example.buildsrc.NavigationCoreGeneratorPlugin"
        }
    }
}
