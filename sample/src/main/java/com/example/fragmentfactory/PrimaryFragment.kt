package com.example.fragmentfactory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.fragmentfactory.databinding.FragmentPrimaryBinding
import com.rwqwr.processor.api.ProvideToFactory
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject
import javax.inject.Named

@ProvideToFactory
class PrimaryFragment @Inject constructor(
    @Named("primary") private val text:String
) : Fragment(R.layout.fragment_primary) {

    private val viewBinding: FragmentPrimaryBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.text.text = text
    }
}
