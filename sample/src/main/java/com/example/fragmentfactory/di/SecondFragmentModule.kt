package com.example.fragmentfactory.di

import com.rwqwr.processor.api.FactoryModule
import dagger.Module

@FactoryModule
@Module(includes = [SecondFragmentModuleProvider::class])
internal interface SecondFragmentModule
