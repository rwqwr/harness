package com.example.fragmentfactory.secondary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fragment_processor_api.ProvideToFactory
import com.example.fragmentfactory.databinding.FragmentPrimaryBinding
import com.example.fragmentfactory.databinding.FragmentSecondaryBinding
import com.example.fragmentfactory.di.DiComponent
import javax.inject.Inject
import javax.inject.Named

@ProvideToFactory
class SecondaryFragment @Inject constructor(
    @Named("secondary") private val text: String
) : Fragment() {

    private var _viewBinding: FragmentSecondaryBinding? = null

    private val viewBinding: FragmentSecondaryBinding
        get() = requireNotNull(_viewBinding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        DiComponent.Initializer.init().inject(this)
    }

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
        viewBinding.text.text = text
        viewBinding.button.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}
