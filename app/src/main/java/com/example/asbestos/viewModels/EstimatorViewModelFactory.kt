package com.example.asbestos.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.asbestos.database.EstimatorDao

class EstimatorViewModelFactory(
    private val database: EstimatorDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstimatorViewModel::class.java)) {
            return EstimatorViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}