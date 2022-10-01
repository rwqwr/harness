package com.example.fragmentfactory

import androidx.fragment.app.Fragment
import javax.inject.Inject
import javax.inject.Provider
import androidx.fragment.app.FragmentFactory as OriginFragmentFactory

@Suppress("unused")
@JvmSuppressWildcards
class FragmentFactory @Inject constructor(
    private val fragments: Map<Class<out Fragment>, Provider<Fragment>>,
    private val fragmentsSet: Set<Fragment>
) : OriginFragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragment = fragments.entries.find { (key, _) -> key.canonicalName == className }?.value
        return fragment?.get() ?: super.instantiate(classLoader, className)
    }
}
