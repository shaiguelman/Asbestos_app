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
import com.example.asbestos.database.EstimatorItem
import com.example.asbestos.databinding.FragmentRoomBinding
import com.example.asbestos.estimatorTools.EstimatorItemListAdapter
import com.example.asbestos.viewModels.RoomViewModel
import com.example.asbestos.viewModels.RoomViewModelFactory

class RoomFragment: Fragment() {

    private val args: RoomFragmentArgs by navArgs()
    private lateinit var viewModel: RoomViewModel
    private lateinit var binding: FragmentRoomBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val dataSource = EstimatorDatabase.getInstance(context!!).dao()

        val materialTypeStrings = EstimatorItem.materialTypes

        val roomId = args.roomId

        val viewModelFactory = RoomViewModelFactory(dataSource, roomId)

        viewModel = ViewModelProviders.of(
            this, viewModelFactory
        ).get(RoomViewModel::class.java)

        val binding = FragmentRoomBinding.inflate(inflater)
        binding.viewModel = viewModel

        viewModel.room.observe(viewLifecycleOwner, Observer {
            binding.roomTypeText.text = it.type
        })


        val spinner = binding.chooseButtonSpinner
        spinner.adapter = ArrayAdapter<String>(
            activity!!, R.layout.support_simple_spinner_dropdown_item, materialTypeStrings
        )

        binding.lifecycleOwner = this

        var items = viewModel.items.value ?: mutableListOf()
        val listAdapter = EstimatorItemListAdapter(context!!,
            items,
            EstimatorItemListAdapter.EstimatorClickListener { item ->
                viewModel.removeItem(item)
            })

        viewModel.items.observe(viewLifecycleOwner, Observer {
            items = it
            listAdapter.updateDataSource(items)
        })


        binding.roomItemList.adapter = listAdapter

        binding.addRoomBtn.setOnClickListener {
            findNavController().navigate(
                RoomFragmentDirections.actionRoomFragmentToEstimatorFragment()
            )
        }

        binding.addItemBtn.setOnClickListener {
            viewModel.addItem(spinner.selectedItem as String)
        }

        /*Included as separate livedata in order to avoid setting textViews to
        zero before the viewmodel coroutine has inserted the item as was happening
        when it was setting to zero in the addItemBtn clickListener. This was only an issue
        due to the inverse data binding combined with the use of multi-threading.
         */
        viewModel.zeroLive.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.lengthText.setText("0")
                binding.widthText.setText("0")
            }
        })

        return binding.root
    }
}