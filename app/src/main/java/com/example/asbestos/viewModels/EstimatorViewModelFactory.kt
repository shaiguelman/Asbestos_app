package com.example.asbestos.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.asbestos.database.EstimatorDao

class EstimatorViewModelFactory(
    private val database: EstimatorDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstimatorViewModel::class.java)) {
            return EstimatorViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}