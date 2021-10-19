import com.example.buildsrc.dependencies.*

plugins {
    id("redmadrobot.kotlin-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + listOf("-Xopt-in=kotlin.ExperimentalStdlibApi")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    options { testLogging.events("skipped", "failed", "passed") }
}

dependencies {
    implementation(dependency(JetBrains.Kotlin.Std))
    implementation(dependency(Google.Dagger.Api))

    implementation("com.google.devtools.ksp:symbol-processing-api:1.5.31-1.0.0")

    implementation("com.squareup:kotlinpoet:1.10.1")
    implementation("com.squareup:kotlinpoet-ksp:1.10.1")
    implementation("com.squareup:kotlinpoet-metadata:1.10.1")
    implementation("com.squareup:javapoet:1.11.1")
    implementation(project(":fragment_processor_api"))

    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.4.5")
    testImplementation(platform("org.junit:junit-bom:5.8.1"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
