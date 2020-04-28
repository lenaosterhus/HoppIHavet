package com.example.badeapp.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.badeapp.models.BadestedForecast


private const val DATABASE_NAME = "BadestedForecast.db"

// Annotates class to be a Room Database with a table (entity)
@Database(entities = [BadestedForecast::class], version = 1, exportSchema = false)
abstract class BadestedForecastDB : RoomDatabase() {

    abstract fun badestedForecastDao(): BadestedForecastDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: BadestedForecastDB? = null

        fun getDatabase(context: Context): BadestedForecastDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BadestedForecastDB::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}