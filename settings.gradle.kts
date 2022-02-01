pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}
include(":processor_api")
include(":processor_ksp")
include(":app")
include(":mylibrary1")

rootProject.name = "FragmentFactory"
