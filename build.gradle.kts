buildscript {
    val kotlinVersion: String by extra("1.6.10")

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0")
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.rmr.android.config)
    alias(libs.plugins.rmr.publish.config)
}

redmadrobot {
    android {
        minSdk.set(29)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
