package com.example.fragmentfactory.primary.di

import com.example.fragmentfactory.FragmentProviderModule
import com.example.fragmentfactory.primary.host.PrimaryNavHostFragment
import dagger.Component

@Component(
    modules = [PrimaryModule::class, FragmentProviderModule::class]
)
interface DiPrimaryComponent {

    fun inject(target: PrimaryNavHostFragment)

    interface Initializer {
        companion object {
            fun init(): DiPrimaryComponent {
                return DaggerDiPrimaryComponent.builder()
                    .build()
            }
        }
    }
}
