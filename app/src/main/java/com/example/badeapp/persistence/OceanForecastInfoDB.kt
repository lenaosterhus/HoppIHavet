package com.example.badeapp.persistence


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.badeapp.models.OceanForecastInfo

private const val DATABASE_NAME = "OceanForecastInfo.db"

// Annotates class to be a Room Database with a table (entity)
@Database(entities = [OceanForecastInfo::class], version = 1, exportSchema = false)
abstract class OceanForecastInfoDB : RoomDatabase() {

    abstract fun oceanForecastInfoDao(): OceanForecastInfoDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: OceanForecastInfoDB? = null

        fun getDatabase(context: Context): OceanForecastInfoDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OceanForecastInfoDB::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}