package com.example.fragmentfactory.di

import com.example.fragmentfactory.host.PrimaryHostFragment
import dagger.Component

@Component(
    modules = [PrimaryModule::class]
)
internal interface DiPrimaryComponent {

    fun inject(target: PrimaryHostFragment)

    interface Initializer {
        companion object {
            fun init(): DiPrimaryComponent {
                return DaggerDiPrimaryComponent.builder().build()
            }
        }
    }
}
