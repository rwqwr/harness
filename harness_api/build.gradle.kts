import com.redmadrobot.build.dsl.ossrh

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.rmr.kotlin.library.get().pluginId)
    id(libs.plugins.rmr.publish.library.get().pluginId)
}

java {
    group = requireNotNull(properties["project.groupId"])
    version = requireNotNull(properties["project.version"])

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        ossrh()
    }
}
