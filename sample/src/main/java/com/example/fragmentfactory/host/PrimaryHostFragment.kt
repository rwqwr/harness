package com.example.fragmentfactory.host

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.fragmentfactory.di.DiPrimaryComponent
import javax.inject.Inject

internal class PrimaryHostFragment : Fragment() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        DiPrimaryComponent.Initializer.init().inject(this)
        childFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
    }
}
