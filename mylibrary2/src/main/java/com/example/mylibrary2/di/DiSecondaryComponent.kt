package com.example.mylibrary2.di

import com.example.mylibrary2.FragmentProviderModule
import com.example.mylibrary2.host.SecondaryHostFragment
import dagger.Component

@Component(
    modules = [SecondaryModule::class]
)
internal interface DiSecondaryComponent {

    fun inject(target: SecondaryHostFragment)

    interface Initializer {
        companion object {
            fun init(): DiSecondaryComponent {
                return DaggerDiSecondaryComponent.builder()
                    .build()
            }
        }
    }
}
