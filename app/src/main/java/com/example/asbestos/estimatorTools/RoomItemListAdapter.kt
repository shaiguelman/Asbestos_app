package com.example.asbestos.estimatorTools

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.asbestos.database.EstimatorItem
import com.example.asbestos.database.Room
import com.example.asbestos.databinding.EstimatorListItemBinding
import com.example.asbestos.databinding.RoomListItemBinding
import com.example.asbestos.viewModels.EstimatorViewModel

class RoomItemListAdapter(private val context: Context,
                          private var dataSource: List<Room>?,
                          private val viewModel: EstimatorViewModel,
                          private val lifecycleOwner: LifecycleOwner,
                          private val clickListener: RoomClickListener): BaseAdapter() {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = EstimatorListItemBinding.inflate(inflater)
        val item  = getItem(position) as Room

        val roomItems = viewModel.getRoomItems(item)
        val subListAdapter = NestedEstimatorItemListAdapter(context, listOf())
        roomItems.observe(lifecycleOwner, Observer {
            subListAdapter.updateDataSource(it)
        })

        rowView.roomItemSublist.adapter = subListAdapter
        rowView.estimatorRoomType.text = item.type

        rowView.editRoomBtn.setOnClickListener {
            clickListener.onClick(item)
        }

        return rowView.root
    }

    override fun getItem(position: Int): Any {
        return dataSource!![position]
    }

    override fun getItemId(position: Int): Long {
        return dataSource!![position].id
    }

    override fun getCount(): Int {
        return dataSource?.size ?: 0
    }

    fun updateList(list: List<Room>?) {
        dataSource = list
        notifyDataSetChanged()
    }

    class NestedEstimatorItemListAdapter(context: Context,
                                     private var items: List<EstimatorItem>):
        EstimatorItemListAdapterAbstract(items) {
        //Implementation of EstimatorItemListAdapter as a sub-list for each room on the estimator main page.

        private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowView = RoomListItemBinding.inflate(inflater)
            val item  = getItem(position) as EstimatorItem
            rowView.listItemMaterial.text = item.itemType
            rowView.listItemQuantity.text = item.quantity.toString()

            return rowView.root
        }
    }

    class RoomClickListener(val clickListener: (Room) -> Unit) {
        //Custom click listener class that takes an room.
        fun onClick(room: Room) = clickListener(room)
    }
}