package com.example.asbestos.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Room(
    @PrimaryKey val id: Long,
    var type: String,
    var name: String)
