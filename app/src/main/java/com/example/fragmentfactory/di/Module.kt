package com.example.fragmentfactory.di

import androidx.fragment.app.Fragment
import com.example.fragmentfactory.secondary.SecondaryFragment
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
object Module {

    @Provides
    @JvmStatic
    @Named("secondary")
    fun provideSecondaryString(): String {
        return "secondary"
    }
}
