buildscript {
    val kotlin_version by extra( "1.4.30")

    repositories {
        mavenCentral()
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")

        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.3")

    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}