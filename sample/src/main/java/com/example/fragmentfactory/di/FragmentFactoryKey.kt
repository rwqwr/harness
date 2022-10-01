package com.example.fragmentfactory.di

import com.rwqwr.processor.api.FactoryKey
import com.rwqwr.processor.api.ProvideToFactory

@FactoryKey
@ProvideToFactory(
    factoryClass = SecondFragmentModule::class,
)
annotation class FragmentFactoryKey