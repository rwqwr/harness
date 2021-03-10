package com.example.mylibrary2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.fragment_processor_api.ProvideToFactory
import com.example.mylibrary2.databinding.FragmentSecondaryBinding
import javax.inject.Inject
import javax.inject.Named

@ProvideToFactory
internal class SecondaryFragment @Inject constructor(
    @Named("secondary") private val text: String
) : Fragment() {

    private var _viewBinding: FragmentSecondaryBinding? = null

    private val arg: SecondaryFragmentArgs by navArgs()

    private val viewBinding: FragmentSecondaryBinding
        get() = requireNotNull(_viewBinding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSecondaryBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textWithArgs = text + arg.arg.toString()
        viewBinding.text.text = textWithArgs
        viewBinding.button.setOnClickListener { activity?.onBackPressed() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}
