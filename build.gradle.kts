buildscript {
    val kotlinVersion: String by extra("1.5.31")

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath(kotlin("gradle-plugin", version = kotlinVersion))

        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")
    }
}

plugins {
    id( "redmadrobot.root-project")
}

redmadrobot {
//    android {
//        minSdk = 29
//        targetSdk = 30
//    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
