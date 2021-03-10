package com.example.fragmentfactory.di

import dagger.Module
import dagger.Provides
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