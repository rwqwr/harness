package com.example.mylibrary1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fragment_processor_api.ProvideToFactory
import com.example.mylibrary1.databinding.FragmentPrimaryBinding
import javax.inject.Inject
import javax.inject.Named

@ProvideToFactory
internal class PrimaryFragment @Inject constructor(
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
        viewBinding.button.setOnClickListener {
            findNavController()
                .navigate(PrimaryFragmentDirections.actionPrimaryToSecondary(TEST_VALUE))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    companion object {

        private const val TEST_VALUE = 12
    }
}
