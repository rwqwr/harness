package com.example.fragmentfactory.di

import com.rwqwr.processor.api.FactoryModule
import dagger.Module

@FactoryModule
@Module(includes = [FragmentModuleProvider::class])
internal interface FragmentModule
