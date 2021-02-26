package com.example.fragmentfactory.primary.di

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
object PrimaryModule {

    @Provides
    @JvmStatic
    @Named("primary")
    fun providePrimaryString(): String {
        return "primary"
    }

    @Provides
    @JvmStatic
    @Named("secondary")
    fun provideSecondaryString(): String {
        return "secondary"
    }
}
