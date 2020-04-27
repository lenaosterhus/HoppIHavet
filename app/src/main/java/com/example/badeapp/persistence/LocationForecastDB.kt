package com.example.badeapp.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.badeapp.models.LocationForecast

private const val DATABASE_NAME = "LocationForecast.db"

// Annotates class to be a Room Database with a table (entity)
@Database(entities = [LocationForecast::class], version = 1, exportSchema = false)
abstract class LocationForecastDB : RoomDatabase() {

    abstract fun locationForecastDao(): LocationForecastDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: LocationForecastDB? = null

        fun getDatabase(context: Context): LocationForecastDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationForecastDB::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}