package com.example.asbestos.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Room::class, EstimatorItem::class], version = 10, exportSchema = false)
abstract class EstimatorDatabase: RoomDatabase() {

    abstract fun dao(): EstimatorDao

    companion object {

        @Volatile
        private var INSTANCE: EstimatorDatabase? = null

        fun getInstance(context: Context): EstimatorDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = androidx.room.Room.databaseBuilder(
                        context.applicationContext,
                        EstimatorDatabase::class.java,
                        "estimator_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}

