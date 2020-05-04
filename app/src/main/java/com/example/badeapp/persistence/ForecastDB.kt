package com.example.badeapp.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.badeapp.models.BadestedConverter
import com.example.badeapp.models.BadestedSummary
import com.example.badeapp.models.LocationForecast
import com.example.badeapp.models.OceanForecast


private const val DATABASE_NAME = "Forecasts.db"

// Annotates class to be a Room Database with a table (entity)
@Database(
    entities = [OceanForecast::class, LocationForecast::class, BadestedSummary::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(BadestedConverter::class)
abstract class ForecastDB : RoomDatabase() {

    abstract fun oceanForecastDao(): OceanForecastDao
    abstract fun locationForecastDao(): LocationForecastDao
    abstract fun badestedSummaryDao(): BadestedSummaryDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: ForecastDB? = null

        fun getDatabase(context: Context): ForecastDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ForecastDB::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}