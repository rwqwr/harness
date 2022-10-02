package com.example.fragmentfactory.di

import androidx.fragment.app.Fragment
import com.rwqwr.processor.api.FactoryKey
import com.rwqwr.processor.api.ProvideToFactory
import com.rwqwr.processor.api.SetKey

@FactoryKey
@ProvideToFactory(
    factoryClass = SecondFragmentModule::class,
    supertype = Fragment::class,
    mapKey = SetKey::class,
)
annotation class FragmentFactoryKey