package com.example.fragmentfactory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.fragmentfactory.databinding.FragmentPrimaryBinding
import com.example.fragmentfactory.di.FragmentMapKey
import com.example.fragmentfactory.di.FragmentModule
import com.example.fragmentfactory.di.SecondFragmentModule
import com.redmadrobot.extensions.viewbinding.viewBinding
import com.rwqwr.processor.api.ProvideToFactory
import javax.inject.Inject
import javax.inject.Named

@ProvideToFactory(FragmentModule::class, FragmentMapKey::class)
class PrimaryFragment @Inject constructor(
    @Named("primary") private val text:String
) : Fragment(R.layout.fragment_primary) {

    private val viewBinding: FragmentPrimaryBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.text.text = text
    }
}

@ProvideToFactory(SecondFragmentModule::class, FragmentMapKey::class)
class SecondaryFragment @Inject constructor(
    @Named("primary") private val text:String
) : Fragment(R.layout.fragment_primary) {

    private val viewBinding: FragmentPrimaryBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.text.text = text
    }
}
