package com.example.mylibrary1.di

import com.example.fragment_processor_api.FragmentsModule
import dagger.Module

@FragmentsModule
@Module(includes = [FragmentProviderModule::class])
internal class FragmentModule
