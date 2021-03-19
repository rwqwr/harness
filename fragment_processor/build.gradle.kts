import com.example.buildsrc.dependencies.*

plugins {
    id("redmadrobot.kotlin-library")
    kotlin("kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        useIR = true
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(dependency(JetBrains.Kotlin.Std))
    implementation(dependency(Google.Dagger.Api))

    implementation("com.squareup:kotlinpoet:1.7.2")
    implementation("com.squareup:javapoet:1.11.1")
    implementation("com.squareup:kotlinpoet-metadata:1.7.2")
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.2.0")
    implementation(project(":fragment_processor_api"))
    implementation(files("${System.getProperty("java.home")}/../lib/tools.jar"))

    compileOnly("net.ltgt.gradle.incap:incap:0.3")
    kapt("net.ltgt.gradle.incap:incap-processor:0.3")

    compileOnly("com.google.auto.service:auto-service:1.0-rc4")
    kapt("com.google.auto.service:auto-service:1.0-rc4")
}
