package com.example.fragmentfactory.di

import com.rwqwr.processor.api.FragmentsModule
import dagger.Module

@FragmentsModule
@Module(includes = [SecondFragmentModuleProvider::class])
internal interface SecondFragmentModule
