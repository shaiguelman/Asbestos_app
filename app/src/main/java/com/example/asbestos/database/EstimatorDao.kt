package com.example.asbestos.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EstimatorDao {

    @Insert
    fun insertRoom(room: Room)

    @Insert()
    fun insertItem(item: EstimatorItem)

    @Update
    fun updateRoom(room: Room)

    @Delete
    fun deleteRoom(room: Room)

    @Delete
    fun removeItem(item: EstimatorItem)

    @Query("select * from ROOM")
    fun getRooms(): LiveData<List<Room>>

    @Query("select * from Room where id = :id")
    fun getRoom(id: Long): LiveData<Room>

    @Query("select count(id) from Room where type = :type")
    fun getCountRoomOfType(type: String): Int

    @Query("select * from ITEM where ROOM_ID = :id")
    fun getItems(id: Long): LiveData<List<EstimatorItem>>

    @Query("select * from ITEM where ROOM_ID = :id")
    suspend fun getItemsAsync(id: Long): List<EstimatorItem>

    @Query("select count(id) from Room")
    fun countRooms(): Long

    @Query("delete from Item")
    fun clearItems()

    @Query("delete from Room")
    fun clearRooms()
}