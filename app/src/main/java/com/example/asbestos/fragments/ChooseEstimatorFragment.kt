package com.example.asbestos.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.asbestos.databinding.FragmentChooseEstimatorBinding

class ChooseEstimatorFragment(): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentChooseEstimatorBinding.inflate(inflater)

        binding.mainMenuBtn.setOnClickListener {
            findNavController().navigate(
                ChooseEstimatorFragmentDirections
                    .actionChooseEstimatorFragmentToMenuPage()
            )
        }

        binding.continueEstimateBtn.setOnClickListener {
            findNavController().navigate(
                ChooseEstimatorFragmentDirections
                    .actionChooseEstimatorFragmentToEstimatorFragment()
            )
        }

        binding.newEstimateBtn.setOnClickListener {
            val action = ChooseEstimatorFragmentDirections
                .actionChooseEstimatorFragmentToEstimatorFragment()
            action.newEstimate = true

            findNavController().navigate(
                action
            )
        }

        return binding.root
    }
}