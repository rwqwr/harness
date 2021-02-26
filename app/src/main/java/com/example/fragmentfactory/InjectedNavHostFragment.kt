package com.example.fragmentfactory

import android.content.Context
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.fragmentfactory.di.DiComponent
import javax.inject.Inject

class InjectedNavHostFragment : NavHostFragment() {

//    @Inject
//    lateinit var fragmentFactory: MainFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
//        DiComponent.Initializer.init().inject(this)
//        childFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
    }
}
