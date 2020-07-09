package com.example.asbestos.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.asbestos.database.EstimatorDao
import com.example.asbestos.database.EstimatorItem
import kotlinx.coroutines.*

class RoomViewModel(
    val dao: EstimatorDao,
    val application: Application,
    var roomId: Long
): ViewModel() {

    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val items = dao.getItems(roomId)

    var numItems = items.value?.size ?: 0

    val room = dao.getRoom(roomId)

    //Tells fragment to set text fields to zero. Is used as a form of semaphore.
    private val _zeroLive = MutableLiveData(false)
    val zeroLive: LiveData<Boolean>
        get() = _zeroLive

    var width = 0
        set(value) {
            field = value
            _area.value = field * length
        }
    var length = 0
        set(value) {
            field = value
            _area.value = field * width
        }

    private val _area = MutableLiveData(0)
    val area: LiveData<Int>
        get() = _area

    fun addItem(selectedMaterialType: String) {
        uiScope.launch {
            val item = EstimatorItem(
                0,
                selectedMaterialType,
                _area.value!!,
                roomId
            )
            insertItem(item)
            numItems++
            setZeroes()
        }
    }

    private suspend fun insertItem(item: EstimatorItem){
        withContext(Dispatchers.IO) {
            dao.insertItem(item)
        }
    }

    private fun setZeroes() {
        width = 0; length = 0
        _zeroLive.value = true
    }

    fun removeItem(item: EstimatorItem) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dao.removeItem(item)
            }
        }

    }
}