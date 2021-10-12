package com.example.mylibrary2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rwqwr.processor.api.ProvideToFactory
import com.example.mylibrary2.databinding.FragmentSecondaryBinding
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject
import javax.inject.Named

@ProvideToFactory
internal class SecondaryFragment @Inject constructor(
    @Named("secondary") private val text: String
) : Fragment(R.layout.fragment_secondary) {

    private val arg: SecondaryFragmentArgs by navArgs()

    private val viewBinding: FragmentSecondaryBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textWithArgs = text + arg.arg.toString()
        viewBinding.text.text = textWithArgs
        viewBinding.button.setOnClickListener { activity?.onBackPressed() }
    }
}
