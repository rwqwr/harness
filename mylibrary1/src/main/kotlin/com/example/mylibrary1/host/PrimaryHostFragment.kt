package com.example.mylibrary1.host

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentFactory
import androidx.navigation.fragment.NavHostFragment
import com.example.mylibrary1.R
import com.example.mylibrary1.di.DiPrimaryComponent
import javax.inject.Inject

internal class PrimaryHostFragment : NavHostFragment() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        DiPrimaryComponent.Initializer.init().inject(this)
        childFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController(this).setGraph(R.navigation.nav_primary_graph)
    }
}
