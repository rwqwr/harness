package com.example.fragmentfactory.di

import com.rwqwr.processor.api.FragmentsModule
import dagger.Module

@FragmentsModule(generateFactory = true)
@Module(includes = [FragmentModuleProvider::class])
internal interface FragmentModule
