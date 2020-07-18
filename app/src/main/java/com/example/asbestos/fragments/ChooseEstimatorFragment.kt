package com.example.asbestos.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.asbestos.database.EstimatorDatabase
import com.example.asbestos.databinding.FragmentChooseEstimatorBinding
import com.example.asbestos.viewModels.EstimatorViewModel
import com.example.asbestos.viewModels.EstimatorViewModelFactory

class ChooseEstimatorFragment(): Fragment() {

    private lateinit var binding: FragmentChooseEstimatorBinding
    private lateinit var viewModel: EstimatorViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChooseEstimatorBinding.inflate(inflater)

        val dao = EstimatorDatabase.getInstance(context!!).dao()
        val viewModelFactory = EstimatorViewModelFactory(dao)
        viewModel = ViewModelProviders.of(
            activity!!, viewModelFactory
        ).get(EstimatorViewModel::class.java)

        viewModel.roomCount.observe(viewLifecycleOwner, Observer {
            val button = binding.continueEstimateBtn
            val isEmpty = it == 0
            button.isEnabled = !isEmpty
            button.isVisible = !isEmpty
        })

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