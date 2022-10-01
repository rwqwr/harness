package com.example.fragmentfactory.di

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [FragmentModule::class, SecondFragmentModule::class])
internal object PrimaryModule {

    @Provides
    @JvmStatic
    @Named("primary")
    fun providePrimaryString(): String {
        return "primary"
    }
}
