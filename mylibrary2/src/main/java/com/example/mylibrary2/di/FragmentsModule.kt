package com.example.mylibrary2.di

import com.example.fragment_processor_api.FragmentsModule
import dagger.Module

@FragmentsModule(generateFactory = true)
@Module(includes = [FragmentProviderModule::class])
internal class FragmentsModule
