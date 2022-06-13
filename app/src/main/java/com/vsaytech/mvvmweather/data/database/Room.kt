package com.vsaytech.mvvmweather.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDao {
    @Query("select * from currentweatherdb LIMIT 1")
    fun getCurrentWeather(): Flow<CurrentWeatherDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentWeather(currentWeatherDB: CurrentWeatherDB)
}

@Database(entities = [CurrentWeatherDB::class], version = 1)
@TypeConverters(Converters::class)
abstract class CurrentWeatherDatabase : RoomDatabase() {
    abstract val currentWeatherDao: CurrentWeatherDao
}

private lateinit var instance: CurrentWeatherDatabase

fun getDatabase(context: Context): CurrentWeatherDatabase {
    synchronized(CurrentWeatherDatabase::class.java) {
        if (!::instance.isInitialized) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                CurrentWeatherDatabase::class.java,
                "currentWeatherDB"
            ).build()
        }
    }
    return instance
}