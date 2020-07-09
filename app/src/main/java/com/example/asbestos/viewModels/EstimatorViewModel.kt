package com.example.asbestos.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.asbestos.database.*
import kotlinx.coroutines.*
import kotlin.math.ceil
import kotlin.math.roundToInt

private const val ERROR_COEF = .2
private const val COST_OF_AIR_SCRUBBER = 100
private const val ROOMS_PER_AIR_SCRUBBER = 3.0

class EstimatorViewModel(
    val dao: EstimatorDao,
    val application: Application): ViewModel() {

    var rooms: LiveData<List<Room>> = dao.getRooms()

    val roomId = MutableLiveData(0L)

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    fun clearData() {
        uiScope.launch {
            val deferred: Deferred<Boolean> = uiScope.async {
                withContext(Dispatchers.IO) {
                    dao.clearRooms()
                    true
                }
            }
            deferred.await()
        }
    }

    fun getRoomItems(room: Room): LiveData<List<EstimatorItem>> {
        return dao.getItems(room.id)
    }

    private suspend fun insertRoom(roomId: Long, roomType: String) {
        withContext(Dispatchers.IO) {
            val room =
                Room(roomId, roomType, roomType + dao.getCountRoomOfType(roomType) + 1)
            dao.insertRoom(room)
        }
    }

    fun navToNewRoom(itemPosition: Int) {
        uiScope.launch {
            roomId.value = withContext(Dispatchers.IO) {
                val id = dao.countRooms() + 1
                insertRoom(id, roomTypes[itemPosition])
                id
            }
        }
    }

    private val _totPriceLiveData = MutableLiveData<Int?>()
    val totPriceLiveData: LiveData<Int?>
        get() = _totPriceLiveData

    //The total price and the range that gets outputted to the users
    fun calcRawPrice() {
        uiScope.launch {
            var tot = 0.0
            rooms.value?.forEach { room ->
                val items = dao.getItemsAsync(room.id)

                items.forEach {
                    tot += itemPrice(it)
                }
            }

            _totPriceLiveData.value = tot.roundToInt()
        }
    }

    var totPrice = 0

    fun calcFinalPrice(tot: Int) {
        val roomCount = rooms.value!!.size

        totPrice = (tot * (1.0 + .1 * roomCount) + COST_OF_AIR_SCRUBBER * ceil(roomCount / ROOMS_PER_AIR_SCRUBBER)).roundToInt()
    }

    val minValue: Int
        get() = (totPrice * (1 - ERROR_COEF)).toInt()

    val maxValue: Int
        get() = (totPrice * (1 + ERROR_COEF)).toInt()

    //The possible room types
    val roomTypes = RoomType.values().map {
        it.name
    }

    private fun itemPrice(item: EstimatorItem): Double {
        val price: Double = when (EstimatorItemType.valueOf(item.itemType.toUpperCase())) {

            EstimatorItemType.TILE -> 5.0

            EstimatorItemType.DRYWALL -> 1.84

            EstimatorItemType.INSULATION -> .26

            EstimatorItemType.POPCORN_CEILING -> 5.0
        }

        return price * item.quantity
    }
}