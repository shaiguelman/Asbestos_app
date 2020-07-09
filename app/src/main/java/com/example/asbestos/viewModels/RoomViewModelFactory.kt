package com.example.asbestos.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.asbestos.database.EstimatorDao

class RoomViewModelFactory(
    private val database: EstimatorDao,
    private val application: Application,
    private val roomId: Long
    ) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
            return RoomViewModel(database, application, roomId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}