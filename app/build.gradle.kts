import com.example.buildsrc.dependencies.*

plugins {
    id("redmadrobot.application")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    defaultConfig {
        applicationId = "com.example.fragmentfactory"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildFeatures.viewBinding = true
    }
}

dependencies {

    implementation(project(":mylibrary1"))
    implementation(project(":mylibrary2"))

    implementation(dependency(JetBrains.Kotlin.Std))
    implementation(dependency(Androidx.Core.Ktx))
    implementation(dependency(Androidx.AppCompat))
    implementation(dependency(Google.Material))
    implementation(dependency(Androidx.Navigation.FragmentKtx))
    implementation(dependency(Androidx.Navigation.UiKtx))
}
