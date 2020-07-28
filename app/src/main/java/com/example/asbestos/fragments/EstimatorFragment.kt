package com.example.asbestos.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.asbestos.R
import com.example.asbestos.database.EstimatorDatabase
import com.example.asbestos.databinding.FragmentEstimatorBinding
import com.example.asbestos.estimatorTools.RoomItemListAdapter
import com.example.asbestos.viewModels.EstimatorViewModel
import com.example.asbestos.viewModels.EstimatorViewModelFactory


class EstimatorFragment: Fragment() {

    private val args: EstimatorFragmentArgs by navArgs()

    lateinit var viewModel: EstimatorViewModel

    private lateinit var binding: FragmentEstimatorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val dao = EstimatorDatabase.getInstance(requireContext()).dao()
        val vm: EstimatorViewModel by activityViewModels{ EstimatorViewModelFactory(dao) }
        viewModel = vm

        binding = FragmentEstimatorBinding.inflate(inflater)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        if (args.newEstimate) {
            viewModel.clearData()
        }

        val spinner = binding.chooseRoomSpinner

        spinner.adapter = ArrayAdapter<String>(
            requireActivity(), R.layout.support_simple_spinner_dropdown_item, viewModel.roomTypes
        )

        /*viewModel.area.observe(this as LifecycleOwner, Observer {
            binding.areaView.text = viewModel.area.toString()
        })

        binding.ftSquared.text = Html.fromHtml(" ft<sup>2</sup>")*/

        val listAdapterClickListener = RoomItemListAdapter.RoomClickListener {
            findNavController().navigate(
                EstimatorFragmentDirections.
                actionEstimatorFragmentToRoomFragment(it.id)
            )
        }

        val listAdapter = RoomItemListAdapter(requireContext(),
            viewModel.rooms.value,
            viewModel,
            viewLifecycleOwner,
            listAdapterClickListener)

        viewModel.rooms.observe(viewLifecycleOwner, Observer {
            listAdapter.updateList(it)
        })

        binding.roomsList.adapter = listAdapter

        binding.addRoomBtn.setOnClickListener {
            viewModel.navToNewRoom(spinner.selectedItemPosition)
        }

        viewModel.roomId.observe(viewLifecycleOwner, Observer {
            if (it != 0L) {
                findNavController().navigate(
                    EstimatorFragmentDirections.actionEstimatorFragmentToRoomFragment(
                        it
                    )
                )
                viewModel.doneNavigating()
            }
        })

        binding.clearEstimatorBtn.setOnClickListener {
            viewModel.clearData()
        }

        binding.calculateBtn.setOnClickListener {
            viewModel.calcRawPrice()
        }

        viewModel.totPriceLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                viewModel.calcFinalPrice(it)
                navToResultFragment()
            }
        })

        binding.mainMenuBtn.setOnClickListener {
            findNavController().navigate(EstimatorFragmentDirections
                .actionEstimatorFragmentToMenuPage())
        }

        return binding.root
    }

    private fun navToResultFragment() {
        val action = EstimatorFragmentDirections.actionEstimatorFragmentToResultFragment()
        action.minValue = viewModel.minValue
        action.maxValue = viewModel.maxValue
        findNavController().navigate(action)
        viewModel.doneNavigating()
    }
}