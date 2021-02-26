package com.example.fragmentfactory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject
import javax.inject.Provider

class MainFragmentFactory @Inject constructor(
    private val fragmentsClasses: @JvmSuppressWildcards Map<Class<*>, Provider<Fragment>>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentKey = fragmentsClasses.keys.find { it.canonicalName == className }
            ?: return super.instantiate(classLoader, className)

       return fragmentsClasses.getValue(fragmentKey).get()
    }
}
