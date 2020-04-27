package com.example.badeapp.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.badeapp.models.LocationForecastInfo

private const val DATABASE_NAME = "LocationForecastInfo.db"

// Annotates class to be a Room Database with a table (entity)
@Database(entities = [LocationForecastInfo::class], version = 1, exportSchema = false)
abstract class LocationForecastInfoDB : RoomDatabase() {

    abstract fun locationForecastInfoDao(): LocationForecastInfoDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: LocationForecastInfoDB? = null

        fun getDatabase(context: Context): LocationForecastInfoDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationForecastInfoDB::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}