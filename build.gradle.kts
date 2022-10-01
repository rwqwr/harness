@file:Suppress("UnstableApiUsage")

import com.redmadrobot.build.dsl.developer
import com.redmadrobot.build.dsl.mit
import com.redmadrobot.build.dsl.setGitHubProject

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.rmr.android.config)
    alias(libs.plugins.rmr.publish.config)
}

redmadrobot {
    android {
        minSdk.set(29)
        targetSdk.set(31)
        compileSdk.set("31")
    }
    publishing {
        signArtifacts.set(true) // Enables artifacts signing, required for publication to OSSRH
        useGpgAgent.set(true)

        pom {
            setGitHubProject("rwqwr/harness")

            description.set("Dagger Multibinding Utils")

            licenses {
                mit()
            }

            developers {
                developer(id = "rwqwr", name = "Roman Ivanov", email = "ivanov.roman.b@gmail.com")
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
