package com.vsaytech.mvvmweather.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vsaytech.mvvmweather.data.model.ConditionWS

/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */

@Entity
data class Condition constructor(
    @PrimaryKey
    val code: Int,
    val icon: String,
    val text: String
)

@Entity
data class Current constructor(
    @PrimaryKey
    val cloud: Int,
    val condition: ConditionWS,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val gust_kph: Double,
    val gust_mph: Double,
    val humidity: Int,
    val is_day: Int,
    val last_updated: String,
    val last_updated_epoch: Int,
    val precip_in: Double,
    val precip_mm: Double,
    val pressure_in: Double,
    val pressure_mb: Double,
    val temp_c: Double,
    val temp_f: Double,
    val uv: Double,
    val vis_km: Double,
    val vis_miles: Double,
    val wind_degree: Int,
    val wind_dir: String,
    val wind_kph: Double,
    val wind_mph: Double
)

@Entity
data class Location constructor(
    val country: String,
    val lat: Double,
    val localtime: String,
    val localtime_epoch: Int,
    val lon: Double,
    @PrimaryKey
    val name: String,
    val region: String,
    val tz_id: String
)