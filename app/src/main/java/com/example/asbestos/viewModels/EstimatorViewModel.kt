package com.example.asbestos.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.asbestos.database.EstimatorDao
import com.example.asbestos.database.EstimatorItem
import com.example.asbestos.database.Room
import com.example.asbestos.database.RoomType
import kotlinx.coroutines.*
import kotlin.math.ceil
import kotlin.math.roundToInt

private const val ERROR_COEF = .1
private const val COST_OF_AIR_SCRUBBER = 100
private const val ROOMS_PER_AIR_SCRUBBER = 3.0
private const val DISPOSAL = 500
private const val VARIABLE_COSTS = .075
private const val MINIMUM_COST = 1500

class EstimatorViewModel(
    private val dao: EstimatorDao): ViewModel() {

    val roomCount = dao.countRoomsLiveData()

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
            //Makes sure data is fully cleared before any further UI actions are taken.
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
                val id = dao.countRooms().toLong() + 1
                insertRoom(id, roomTypes[itemPosition])
                id
            }
        }
    }

    fun doneNavigating() {
        roomId.value = 0L
        _totPriceLiveData.value = null
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

        totPrice = ((1 + .1 * roomCount + VARIABLE_COSTS) * tot
                + (COST_OF_AIR_SCRUBBER
                * ceil(roomCount / ROOMS_PER_AIR_SCRUBBER)).roundToInt()
                + DISPOSAL).roundToInt()

        if (totPrice < MINIMUM_COST) {
            //.1 factor helps randomize the value.
            totPrice = (MINIMUM_COST - 100 + .1 * totPrice).roundToInt()
        }
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
        val price: Double = when (item.itemType) {

            "Tile" -> 5.0

            "Drywall" -> 1.84

            "Insulation" -> .26

            "Popcorn Ceiling" -> 7.0

            else -> throw Exception("Invalid item type")
        }

        return price * item.quantity
    }
}