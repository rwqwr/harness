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
include(":app")
include(":fragment_processor_api")
include(":fragment_processor")
include(":processor_ksp")
include(":mylibrary1")

rootProject.name = "FragmentFactory"
