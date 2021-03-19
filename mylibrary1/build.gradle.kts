import com.example.buildsrc.dependencies.*

plugins {
    id("redmadrobot.android-library")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    buildFeatures.androidResources = true
}

dependencies {
    implementation(project(":core"))

    implementation(project(":fragment_processor_api"))
    kapt(project(":fragment_processor"))

    implementation(dependency(JetBrains.Kotlin.Std))
    implementation(dependency(Androidx.Core.Ktx))
    implementation(dependency(Androidx.AppCompat))
    implementation(dependency(Google.Material))
    implementation(dependency(Androidx.Navigation.FragmentKtx))
    implementation(dependency(Androidx.Navigation.UiKtx))
    implementation(dependency(Redmadrobot.Extensions.ViewBinding))

    implementation(dependency(Google.Dagger.Api))
    kapt(dependency(Google.Dagger.Compiler))

}
