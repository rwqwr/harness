buildscript {
    val kotlinVersion: String by extra("1.4.31")

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.4")
    }
}

plugins {
    id( "redmadrobot.root-project")
}

redmadrobot {
    android {
        minSdk = 29
        targetSdk = 30
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
