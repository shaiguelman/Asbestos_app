package com.example.asbestos.estimatorTools

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter

@BindingAdapter("android:text")
fun setFragmentDimensions(textView: TextView, value: Int) {
    textView.text = value.toString()
}

@InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
fun setViewModelDimensions(textView: TextView): Int {
    val newVal = textView.text.toString()
    if (newVal == "") {
        return 0
    }
    return newVal.toInt()
}

@BindingAdapter("totPrice")
fun setPrice(textView: TextView, value: Double) {
    textView.text = value.toString()
}
