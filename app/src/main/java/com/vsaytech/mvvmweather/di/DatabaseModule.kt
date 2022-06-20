package com.vsaytech.mvvmweather.di

import android.content.Context
import androidx.room.Room
import com.vsaytech.mvvmweather.data.database.CurrentWeatherDao
import com.vsaytech.mvvmweather.data.database.CurrentWeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): CurrentWeatherDatabase {
        return Room.databaseBuilder(
            appContext,
            CurrentWeatherDatabase::class.java,
            "currentWeatherDB"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideCurrentWeatherDao(currentWeatherDatabase: CurrentWeatherDatabase): CurrentWeatherDao {
        return currentWeatherDatabase.currentWeatherDao
    }
}