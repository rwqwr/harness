package com.example.fragmentfactory.di

import com.example.fragmentfactory.MainActivity
import dagger.Component

@Component(
    modules = [Module::class]
)
interface DiComponent {

    fun inject(target: MainActivity)

    interface Initializer {
        companion object {
            fun init(): DiComponent {
                return DaggerDiComponent.builder()
                    .build()
            }
        }
    }
}
