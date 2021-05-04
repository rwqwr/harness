import com.example.buildsrc.dependencies.*

plugins {
    id("redmadrobot.android-library")
    id("androidx.navigation.safeargs.kotlin")
    id("redmadrobot.navigation-core")
}

android {
    buildFeatures {
        androidResources = true
        viewBinding = false
    }
}

dependencies {

    implementation(dependency(JetBrains.Kotlin.Std))
    implementation(dependency(Androidx.Core.Ktx))
    implementation(dependency(Androidx.AppCompat))
    implementation(dependency(Google.Material))
    implementation(dependency(Androidx.Navigation.FragmentKtx))
    implementation(dependency(Androidx.Navigation.UiKtx))
}
