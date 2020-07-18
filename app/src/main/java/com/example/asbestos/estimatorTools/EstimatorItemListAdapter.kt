package com.example.asbestos.estimatorTools

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asbestos.database.EstimatorItem
import com.example.asbestos.databinding.RoomListItemBinding

class EstimatorItemListAdapter(context: Context,
                               dataSource: List<EstimatorItem>,
                               private val clickListener: EstimatorClickListener):
    EstimatorItemListAdapterAbstract(dataSource) {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = RoomListItemBinding.inflate(inflater)
        val item  = getItem(position) as EstimatorItem
        rowView.listItemMaterial.text = item.itemType
        rowView.listItemQuantity.text = item.quantity.toString()

        rowView.clearBtn.setOnClickListener{
            clickListener.onClick(item)
        }

        return rowView.root
    }

    class EstimatorClickListener(val clickListener: (EstimatorItem) -> Unit) {
        //Custom click listener class that takes an estimator item.
        fun onClick(item: EstimatorItem) = clickListener(item)
    }
}