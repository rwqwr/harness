package com.example.fragmentfactory.primary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fragment_processor_api.ProvideToFactory
import com.example.fragmentfactory.R
import com.example.fragmentfactory.databinding.FragmentPrimaryBinding
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import javax.inject.Inject
import javax.inject.Named

@ProvideToFactory
class PrimaryFragment @Inject constructor(
    @Named("primary") private val text:String
) : Fragment() {

    private var _viewBinding: FragmentPrimaryBinding? = null

    private val viewBinding: FragmentPrimaryBinding
        get() = requireNotNull(_viewBinding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentPrimaryBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.text.text = text
        viewBinding.button.setOnClickListener { findNavController().navigate(R.id.action_primary_to_secondary) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}
