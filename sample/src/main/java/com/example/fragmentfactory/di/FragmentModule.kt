package com.example.fragmentfactory.di

import com.rwqwr.processor.api.FragmentsModule
import dagger.Module

@FragmentsModule(generateFactory = true)
@Module(includes = [FragmentProviderModule::class])
internal interface FragmentModule
