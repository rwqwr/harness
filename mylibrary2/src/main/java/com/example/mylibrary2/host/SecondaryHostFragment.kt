package com.example.mylibrary2.host

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentFactory
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mylibrary2.R
import com.example.mylibrary2.SecondaryFragmentArgs
import com.example.mylibrary2.di.DiSecondaryComponent
import javax.inject.Inject

internal class SecondaryHostFragment : NavHostFragment() {

    private val args: SecondaryFragmentArgs by navArgs()

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        DiSecondaryComponent.Initializer.init().inject(this)
        childFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().setGraph(R.navigation.nav_secondary_graph, args.toBundle())
    }
}
