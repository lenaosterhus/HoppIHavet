package com.example.badeapp.persistence

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.badeapp.models.Badested
import com.example.badeapp.models.BadestedForecast
import com.example.badeapp.models.Forecast
import com.example.badeapp.models.alleBadesteder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val DATABASE_NAME = "Forecasts.db"
private const val TAG = "ForecastDB"

// Annotates class to be a Room Database with a table (entity)
@Database(
    entities = [Badested::class,Forecast::class],
    views = [BadestedForecast::class],
    version = 1,
    exportSchema = false
)

abstract class ForecastDB : RoomDatabase() {

    abstract fun forecastDao(): ForecastDao

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
                ) .addCallback(
                    object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            //On the creation of the DB for the first time, all the
                            // badesteder are inserted.
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getDatabase(context)
                                Log.d(TAG, "onCreate: Adding all badesteder to DB")
                                database.forecastDao().addAllBadesteder(alleBadesteder)
                            }
                        }
                    }
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}