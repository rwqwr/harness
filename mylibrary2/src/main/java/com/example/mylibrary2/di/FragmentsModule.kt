package com.example.mylibrary2.di

import com.example.fragment_processor_api.FragmentsModule
import com.example.mylibrary2.FragmentProviderModule
import dagger.Module

@FragmentsModule
@Module(includes = [FragmentProviderModule::class])
internal class FragmentsModule
