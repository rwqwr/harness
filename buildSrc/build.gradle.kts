import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.jvm") version "1.4.30"
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.useIR = true
compileKotlin.kotlinOptions.jvmTarget = "1.8"

dependencies {

    implementation("com.android.tools.build:gradle:4.1.2")
    implementation(kotlin("gradle-plugin", version = "1.4.30"))
}

gradlePlugin {
    plugins {
        register("navigation-core") {
            id = "redmadrobot.navigation-core"
            implementationClass = "com.example.buildsrc.NavigationCoreGeneratorPlugin"
        }
    }
}
