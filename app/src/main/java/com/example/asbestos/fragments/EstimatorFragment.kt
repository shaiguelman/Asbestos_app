package com.example.asbestos.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val dao = EstimatorDatabase.getInstance(context!!).dao()
        val application = requireNotNull(this.activity).application
        val binding = FragmentEstimatorBinding.inflate(inflater)

        val viewModelFactory = EstimatorViewModelFactory(dao, application)
        val viewModel = ViewModelProviders.of(
            this, viewModelFactory
        ).get(EstimatorViewModel::class.java)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        if (args.newEstimate) {
            viewModel.clearData()
        }

        val spinner = binding.chooseRoomSpinner

        spinner.adapter = ArrayAdapter<String>(
            activity!!, R.layout.support_simple_spinner_dropdown_item, viewModel.roomTypes
        )

        /*viewModel.area.observe(this as LifecycleOwner, Observer {
            binding.areaView.text = viewModel.area.toString()
        })

        binding.ftSquared.text = Html.fromHtml(" ft<sup>2</sup>")*/

        val listAdapter = RoomItemListAdapter(this.context!!, viewModel.rooms.value, viewModel, viewLifecycleOwner)

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
                val action = EstimatorFragmentDirections.actionEstimatorFragmentToResultFragment()
                action.minValue = viewModel.minValue
                action.maxValue = viewModel.maxValue
                findNavController().navigate(action)
            }
        })

        binding.mainMenuBtn.setOnClickListener {
            findNavController().navigate(EstimatorFragmentDirections
                .actionEstimatorFragmentToMenuPage())
        }

        return binding.root
    }
}