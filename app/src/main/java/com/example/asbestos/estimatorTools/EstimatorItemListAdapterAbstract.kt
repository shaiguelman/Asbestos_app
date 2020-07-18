package com.example.asbestos.estimatorTools

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.asbestos.database.EstimatorItem

abstract class EstimatorItemListAdapterAbstract(private var dataSource: List<EstimatorItem>): BaseAdapter() {

    abstract override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return dataSource[position].id
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    fun updateDataSource(newData: List<EstimatorItem>) {
        dataSource = newData
        notifyDataSetChanged()
    }
}
