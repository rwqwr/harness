include(":core")
include(":mylibrary2")
include(":mylibrary1")
dependencyResolutionManagement {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}
include(":app")
include(":fragment_processor_api")
include(":fragment_processor")
rootProject.name = "FragmentFactory"
