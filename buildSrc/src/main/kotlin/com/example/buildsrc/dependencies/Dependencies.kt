package com.example.buildsrc.dependencies

object JetBrains : Dependency by Group("org.jetbrains") {
    object Kotlin : Dependency by Subgroup(JetBrains, "kotlin", "1.4.31") {
        object Std : Dependency by Module(Kotlin, "kotlin-stdlib", { version })
    }
}

object Androidx : Dependency by Group("androidx") {
    object Core : Dependency by Subgroup(Androidx, "core") {
        object Ktx : Dependency by Module(Core, "core-ktx", { "1.3.2" })
    }

    object AppCompat : Dependency by Module(Subgroup(Androidx, "appcompat"), "appcompat", { "1.2.0" })

    object Navigation : Dependency by Subgroup(Androidx, "navigation", "2.3.3") {
        object FragmentKtx : Dependency by Module(Navigation, "navigation-fragment-ktx", { version })
        object UiKtx : Dependency by Module(Navigation, "navigation-ui-ktx", { version })
    }
}

object Google : Dependency by Group("com.google") {
    object Material : ModuleDependency by Module(Subgroup(Google, "android.material"), "material", { "1.3.0" })
    object Dagger : Dependency by Subgroup(Google, "dagger", "2.32") {
        object Api : Dependency by Module(Dagger, "dagger", { version })
        object Compiler : Dependency by Module(Dagger, "dagger-compiler", { version })
    }
}

object Redmadrobot : Dependency by Group("com.redmadrobot") {
    object Extensions : Dependency by Subgroup(Redmadrobot, "extensions") {
        object ViewBinding : Dependency by Module(Extensions, "viewbinding-ktx", { "4.1.2-2" })
    }
}
