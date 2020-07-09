package com.example.asbestos.database

import androidx.room.Embedded
import androidx.room.Relation

data class RoomWithItems(
    @Embedded
    var Room: Room,

    @Relation(parentColumn = "id", entityColumn = "room_id")
    var items: List<EstimatorItem>
)