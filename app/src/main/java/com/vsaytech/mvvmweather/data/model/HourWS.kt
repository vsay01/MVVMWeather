package com.vsaytech.mvvmweather.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HourWS(
    val chance_of_rain: Int?,
    val chance_of_snow: Int?,
    val cloud: Int?,
    val condition: ConditionXXWS?,
    val dewpoint_c: Double?,
    val dewpoint_f: Double?,
    val feelslike_c: Double?,
    val feelslike_f: Double?,
    val gust_kph: Double?,
    val gust_mph: Double?,
    val heatindex_c: Double?,
    val heatindex_f: Double?,
    val humidity: Int?,
    val is_day: Int?,
    val precip_in: Double?,
    val precip_mm: Double?,
    val pressure_in: Double?,
    val pressure_mb: Double?,
    val temp_c: Double?,
    val temp_f: Double?,
    val time: String?,
    val time_epoch: Int?,
    val uv: Double?,
    val vis_km: Double?,
    val vis_miles: Double?,
    val will_it_rain: Int?,
    val will_it_snow: Int?,
    val wind_degree: Int?,
    val wind_dir: String?,
    val wind_kph: Double?,
    val wind_mph: Double?,
    val windchill_c: Double?,
    val windchill_f: Double?
): Parcelable