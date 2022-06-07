package com.vsaytech.mvvmweather.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vsaytech.mvvmweather.data.model.CurrentWS
import com.vsaytech.mvvmweather.data.model.ForecastWS
import com.vsaytech.mvvmweather.data.model.LocationWS

/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */

@Entity
data class CurrentWeatherDB(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    @Embedded val current: CurrentWS,
    @Embedded val forecast: ForecastWS,
    @Embedded val location: LocationWS
)