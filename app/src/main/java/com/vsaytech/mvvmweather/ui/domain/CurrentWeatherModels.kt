package com.vsaytech.mvvmweather.ui.domain

import android.os.Parcelable
import com.vsaytech.mvvmweather.data.model.AstroWS
import com.vsaytech.mvvmweather.data.model.HourWS
import kotlinx.parcelize.Parcelize

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 *
 * @see database for objects that are mapped to the database
 * @see network for objects that parse or prepare network calls
 */
data class CurrentWeather(
    val name: String,
    val region: String,
    val country: String,
    val currentDayTime: String,
    val temp_c: Double,
    val temp_f: Double,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val wind_mph: Double,
    val wind_kph: Double,
    val humidity: Int,
    val conditionText: String,
    val conditionIcon: String
)

@Parcelize
data class CurrentWeatherDailyForecast(
    val day: String,
    val monthDay: String,
    val conditionText: String,
    val conditionIcon: String,
    val maxtemp_c: Double,
    val maxtemp_f: Double,
    val mintemp_c: Double,
    val mintemp_f: Double,
    val astro: AstroWS,
    val hourList: List<HourWS>
) : Parcelable