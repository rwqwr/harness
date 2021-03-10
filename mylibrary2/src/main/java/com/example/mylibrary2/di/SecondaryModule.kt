package com.example.mylibrary2.di

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [FragmentsModule::class])
internal object SecondaryModule {

    @Provides
    @JvmStatic
    @Named("secondary")
    fun provideSecondaryString(): String {
        return "secondary"
    }
}
