package com.example.fragmentfactory.primary.host

import android.os.Bundle
import androidx.fragment.app.FragmentFactory
import androidx.navigation.fragment.NavHostFragment
import com.example.fragmentfactory.MainFragmentFactory
import com.example.fragmentfactory.primary.di.DiPrimaryComponent
import javax.inject.Inject

class PrimaryNavHostFragment : NavHostFragment() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        DiPrimaryComponent.Initializer.init().inject(this)
        childFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
    }
}
