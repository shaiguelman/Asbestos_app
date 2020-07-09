package com.example.asbestos.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "Item", foreignKeys = [ForeignKey(
    childColumns = ["room_id"],
    entity = Room::class,
    parentColumns = ["id"],
    onDelete = CASCADE,
    onUpdate = CASCADE
)]
)
data class EstimatorItem(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "item_type")val itemType: String,
    var quantity: Int,
    @ColumnInfo(name = "room_id", index = true) val roomId: Long) {

    companion object {
        val materialTypes = listOf<String>("Tile", "Drywall", "Insulation", "Popcorn Ceiling")
    }
}

enum class EstimatorItemType() {

    TILE {
        override val price = 5.0
    },

    DRYWALL {
        override val price = 1.84
    },

    INSULATION {
        override val price = 0.26
    },

    POPCORN_CEILING {
        override val price = 5.0
    };

    abstract val price: Double
}