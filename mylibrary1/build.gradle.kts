import com.example.buildsrc.dependencies.*

plugins {
    id("com.redmadrobot.android-library")
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
    id("kotlin-kapt")
}

android {
    buildFeatures.androidResources = true

    val isNeedGeneratedSources = gradle.startParameter.taskNames.all { !it.contains("^detekt*".toRegex()) }
    if (isNeedGeneratedSources) {
        libraryVariants.all {
            val variantName = name.toLowerCase()
            sourceSets {
                getByName("main") {
                    java.srcDir(File("build/generated/ksp/$variantName/java"))
                    kotlin.srcDir(File("build/generated/ksp/$variantName/kotlin"))
                }
            }
        }
    }
}

dependencies {
    implementation(project(":fragment_processor_api"))
    ksp(project(":processor_ksp"))

    implementation(dependency(JetBrains.Kotlin.Std))
    implementation(dependency(Androidx.Core.Ktx))
    implementation(dependency(Androidx.AppCompat))
    implementation(dependency(Google.Material))
    implementation(dependency(Redmadrobot.Extensions.ViewBinding))

    implementation(dependency(Google.Dagger.Api))
    kapt(dependency(Google.Dagger.Compiler))
    kapt("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.3.0")

}
