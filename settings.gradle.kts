pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        jcenter()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "redmadrobot") {
                useModule("com.redmadrobot.build:infrastructure:0.8.1")
            }
        }
    }
}
dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        jcenter()
    }
}
include(":app")
include(":fragment_processor_api")
include(":fragment_processor")
include(":core")
include(":mylibrary2")
include(":mylibrary1")
rootProject.name = "FragmentFactory"
