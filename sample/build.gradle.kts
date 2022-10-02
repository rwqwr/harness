@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.rmr.android.application.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    alias(libs.plugins.ksp)
}

android {
    defaultConfig {
        applicationId = "com.example.fragmentfactory"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildFeatures.viewBinding = true
    }

    val isNeedGeneratedSources = gradle.startParameter.taskNames.all { !it.contains("^detekt*".toRegex()) }
    if (isNeedGeneratedSources) {
        applicationVariants.all {
            val variantName = name.toLowerCase()
            sourceSets {
                getByName("main") {
                    kotlin.srcDir(File("build/generated/ksp/$variantName/kotlin"))
                }
            }
        }
    }
}

dependencies {
    implementation(libs.kotlin.bom)
    implementation(libs.android.appcompat)
    implementation(libs.material)

    implementation(projects.harnessApi)
    ksp(projects.harnessCompiler)

    implementation(libs.rmr.ktx.viewbinding)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
    kapt(libs.kotlin.metadata)
}
