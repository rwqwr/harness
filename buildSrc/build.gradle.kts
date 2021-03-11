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
compileKotlin.kotlinOptions.jvmTarget = "1.8"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:4.1.2")
}

gradlePlugin {
    plugins {
        register("navigation-core") {
            id = "redmadrobot.navigation-core"
            implementationClass = "com.example.buildsrc.NavigationCoreGeneratorPlugin"
        }
    }
}
