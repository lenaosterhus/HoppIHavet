package com.example.badeapp.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.badeapp.models.OceanForecast


private const val DATABASE_NAME = "OceanForecast.db"

// Annotates class to be a Room Database with a table (entity)
@Database(entities = [OceanForecast::class], version = 1, exportSchema = false)
abstract class OceanForecastDB : RoomDatabase() {

    abstract fun oceanForecastDao(): OceanForecastDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: OceanForecastDB? = null

        fun getDatabase(context: Context): OceanForecastDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OceanForecastDB::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}