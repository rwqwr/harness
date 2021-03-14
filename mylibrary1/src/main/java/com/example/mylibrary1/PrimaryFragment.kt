package com.example.mylibrary1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fragment_processor_api.ProvideToFactory
import com.example.mylibrary1.databinding.FragmentPrimaryBinding
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject
import javax.inject.Named

@ProvideToFactory
internal class PrimaryFragment @Inject constructor(
    @Named("primary") private val text:String
) : Fragment(R.layout.fragment_primary) {

    private val viewBinding: FragmentPrimaryBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.text.text = text
        viewBinding.button.setOnClickListener {
            findNavController()
                .navigate(PrimaryFragmentDirections.actionPrimaryToSecondary(TEST_VALUE))
        }
    }

    companion object {

        private const val TEST_VALUE = 12
    }
}
