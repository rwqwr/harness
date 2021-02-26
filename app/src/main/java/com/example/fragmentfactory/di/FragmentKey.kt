package com.example.fragmentfactory.di

import androidx.fragment.app.Fragment
import dagger.MapKey
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@MapKey
annotation class FragmentKey(val kClass: KClass<out Fragment>)
