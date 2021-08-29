pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "redmadrobot") {
                useModule("com.redmadrobot.build:infrastructure:0.11")
                useModule("com.redmadrobot.build:infrastructure-android:0.11")
            }
        }
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
include(":core")
include(":mylibrary2")
include(":mylibrary1")
rootProject.name = "FragmentFactory"
