package com.example.asbestos.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.asbestos.databinding.FragmentResultBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultFragment : Fragment() {

    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val minValue = args.minValue
        val maxValue = args.maxValue

        val binding = FragmentResultBinding.inflate(inflater)
        val textView = binding.resultText
        val resultString = "Removing the asbestos will cost between $$minValue and $$maxValue dollars!"
        textView.text = resultString

        binding.goBackBtn.setOnClickListener {
            findNavController().navigate(
                ResultFragmentDirections.actionResultFragmentToEstimatorFragment()
            )
        }

        binding.newEstimateBtn.setOnClickListener {
            val action = ResultFragmentDirections.actionResultFragmentToEstimatorFragment()
            action.newEstimate = true
            findNavController().navigate(
                action
            )
        }

        return binding.root
    }
}
