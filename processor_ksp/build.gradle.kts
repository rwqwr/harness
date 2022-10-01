import com.redmadrobot.build.dsl.kotlinCompile
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

kotlinCompile {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=kotlin.ExperimentalStdlibApi",
            "-opt-in=com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview",
        )
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    options { testLogging.events("skipped", "failed", "passed") }
}

publishing {
    repositories {
        ossrh()
    }
}

dependencies {
    implementation(projects.processorApi)

    implementation(platform(libs.kotlin.bom))
    implementation(libs.dagger)
    implementation(libs.ksp)

    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.kotlinpoet.metadata)

    testImplementation(libs.tschuchortdev.kotlin.testing)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}
