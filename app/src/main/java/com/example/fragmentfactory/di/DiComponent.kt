package com.example.fragmentfactory.di

import com.example.fragmentfactory.InjectedNavHostFragment
import com.example.fragmentfactory.MainActivity
import com.example.fragmentfactory.primary.PrimaryFragment
import com.example.fragmentfactory.secondary.SecondaryFragment
import dagger.Component

@Component(
    modules = [Module::class]
)
interface DiComponent {

    fun inject(target: MainActivity)
    fun inject(target: InjectedNavHostFragment)
    fun inject(target: PrimaryFragment)
    fun inject(target: SecondaryFragment)

    interface Initializer {
        companion object {
            fun init(): DiComponent {
                return DaggerDiComponent.builder()
                    .build()
            }
        }
    }
}
