package com.example.fragmentfactory.di

import androidx.fragment.app.Fragment
import dagger.MapKey
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention
@MapKey
annotation class FragmentMapKey(val fragmentClass: KClass<out Fragment>)
